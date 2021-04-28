package net.sejongtelecom.backendapi.common.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Optional;
import net.sejongtelecom.backendapi.common.dto.AdminWalletDto;
import net.sejongtelecom.backendapi.common.dto.CdmasterDto;
import net.sejongtelecom.backendapi.common.dto.WalletDto;

import net.sejongtelecom.backendapi.common.entity.Cdmaster;
import net.sejongtelecom.backendapi.common.entity.Wallet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
public class WalletControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @DisplayName("Make Wallet test")
  @Test
  public void testCreateWallet() throws Exception {

    WalletDto walletDto = WalletDto.builder().userid("jk0001").build();

    String content =
        objectMapper.writeValueAsString(walletDto);
    mockMvc
        .perform(
            post("/v1/wallet/WAL_0100")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());

  }

  @DisplayName("Make Admin Wallet test")
  @Test
  public void testAdminCreateWallet() throws Exception {

    AdminWalletDto adminWalletDto = AdminWalletDto.builder().fundid("Admin").build();

    String content =
        objectMapper.writeValueAsString(adminWalletDto);
    mockMvc
        .perform(
            post("/v1/wallet/WAL_0150")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());

  }

  @DisplayName("Req Deploy ChainCode test")
  @Test
  public void testReqDeployChaincode() throws Exception {

    HashMap<String,String> depMap = new HashMap<String,String>();
    depMap.put("fundid","jk00001");

    String content =
        objectMapper.writeValueAsString(depMap);

    mockMvc
        .perform(
            post("/v1/wallet/WAL_0200")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());

  }
}
