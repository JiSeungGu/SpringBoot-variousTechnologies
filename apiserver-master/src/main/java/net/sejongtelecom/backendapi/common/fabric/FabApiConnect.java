package net.sejongtelecom.backendapi.common.fabric;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.net.ssl.SSLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sejongtelecom.backendapi.common.dto.FabQueryDto;
import net.sejongtelecom.backendapi.common.exception.CResourceNotExistException;
import net.sejongtelecom.backendapi.common.response.FabricResult;
import org.hyperledger.fabric.protos.peer.FabricTransaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class FabApiConnect {

  private final WebClient.Builder webClientBuilder;

  @Value("${call-base-url}")
  private String baseUrl;

  @Value("${channel}")
  private String channel;

  public Optional<FabricResult> sendFabApi(FabQueryDto fabQueryDto, String apiType)
      throws SSLException {

    String sendType = "POST";
    if (apiType.equals("query")) {
      sendType = "GET";
    }

    SslContext sslContext =
        SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();

    HttpClient httpClient = HttpClient.create().secure(builder -> builder.sslContext(sslContext));
    ClientHttpConnector clientHttpConnector = new ReactorClientHttpConnector(httpClient);

    Optional<FabricResult> fabricResultMono = null;

    if (sendType == "POST") {

      fabricResultMono = webClientBuilder
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .baseUrl(baseUrl)
          .clientConnector(clientHttpConnector)
          .build()
          .post()
          .uri(apiType)
          .body(Mono.just(fabQueryDto), FabQueryDto.class)
          .retrieve()
          .bodyToMono(FabricResult.class)
          .log()
          .flux()
          .toStream()
          .findFirst();

    } else {

      HashMap<Object, Object> paramMap = new HashMap<Object, Object>();
      paramMap.put("channel", channel);
      paramMap.put("codenm", fabQueryDto.getCodenm());
      paramMap.put("funcnm", fabQueryDto.getFuncnm());
      if (fabQueryDto.getParams() != null) {
        paramMap.put("params", fabQueryDto.getParams());
      } else {
        paramMap.put("params", "");
      }

      log.info("REALPARAM:" + getParamStr(paramMap));
      String uri = apiType+getParamStr(paramMap);

      fabricResultMono = webClientBuilder
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .baseUrl(baseUrl)
          .clientConnector(clientHttpConnector)
          .build()
          .get()
          .uri(uri)
          .retrieve()
          .bodyToMono(FabricResult.class)
          .log()
          .flux()
          .toStream()
          .findFirst();

      log.info(uri);
    }

    return fabricResultMono;
  }

  public HashMap<Object, Object> connectFabricAndSetResult(FabQueryDto fabQueryDto, String apiType) {

    HashMap<Object, Object> resultMap = new HashMap<>();

    try {
      FabricResult fabricResult = sendFabApi(fabQueryDto, apiType)
          .orElseThrow(CResourceNotExistException::new);

      resultMap.put("rescd", fabricResult.getBodydata().getRescd());
      resultMap.put("resmsg", fabricResult.getBodydata().getResmsg());

      if (apiType == "query"){
        resultMap.put("value", fabricResult.getBodydata().getValue());
      }else {
        resultMap.put("vaildmsg", FabricTransaction.TxValidationCode.forNumber(fabricResult.getBodydata().getVaildcd()));
        resultMap.put("vaildcd", fabricResult.getBodydata().getVaildcd());
        resultMap.put("blockno", fabricResult.getBodydata().getBlockno());
        resultMap.put("txid", fabricResult.getBodydata().getTxid());
      }

    }catch (Exception e){

    }

    return  resultMap;

  }

  private String getParamStr(HashMap<Object, Object> paramMap) {
    String params = "";

    int loopcnt = 0;
    for (Map.Entry<Object, Object> entry : paramMap.entrySet()) {

      if (loopcnt == 0) {
        params += "?";
      } else {
        params += "&";
      }
      String val = entry.getValue().toString();
      params += entry.getKey() + "=" + val;
      loopcnt++;
    }
    return params;
  }

}
