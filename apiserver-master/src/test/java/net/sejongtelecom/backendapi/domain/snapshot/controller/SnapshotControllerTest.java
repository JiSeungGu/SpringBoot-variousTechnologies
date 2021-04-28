package net.sejongtelecom.backendapi.domain.snapshot.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootTest
class SnapshotControllerTest {
  private MockMvc mockMvc;

  @BeforeEach
  public void setUp(WebApplicationContext webApplicationContext) {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .addFilters(new CharacterEncodingFilter("UTF-8", true)).build();
  }

  // 투표 생성 테스트
  @Test
  void createSnapshot() throws Exception {
    String json = "{\n"
        + "    \"fundid\": \"jk0001\", \n"
        + "    \"snapshotid\": \"20210330\"\n"
        + "}";

    mockMvc.perform(post("/v1/snapshot/SNP_0100")
        .content(json)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
  }

  // 투표 메타 정보 가져오기 테스트 (실패케이스 : 해당 스냅샷이 존재하지 않음)
  @Test
  void getSnapshot_failure_NotExists() throws Exception {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("fundid", "Admin1");
    params.add("snapshotid", "20210330");
    mockMvc.perform(get("/v1/snapshot/SNP_0200").params(params)).andDo(print()).andExpect(status().isNotFound());
  }

  // 투표 메타 정보 가져오기 테스트 (성공케이스)
  @Test
  void getSnapshot() throws Exception {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("fundid", "Admin1");
    params.add("snapshotid", "20210330");
    mockMvc.perform(get("/v1/snapshot/SNP_0200").params(params)).andDo(print()).andExpect(status().isOk());
  }

}