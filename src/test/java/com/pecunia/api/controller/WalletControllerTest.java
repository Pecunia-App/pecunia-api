package com.pecunia.api.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pecunia.api.dto.wallet.WalletDto;
import com.pecunia.api.security.JwtAuthenticationFilter;
import com.pecunia.api.service.WalletService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = WalletController.class,
    excludeFilters = {
      @ComponentScan.Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          classes = JwtAuthenticationFilter.class)
    })
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class WalletControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockitoBean private WalletService walletService;

  @Test
  void shouldReturnAllWallets_thenIsCalling() throws Exception {

    WalletDto dto1 = new WalletDto();
    dto1.setName("Wallet de Jane");
    dto1.setAmount(new BigDecimal("1000"));
    dto1.setCurrency("EUR");

    WalletDto dto2 = new WalletDto();
    dto2.setName("Wallet de John");
    dto2.setAmount(new BigDecimal("25"));
    dto2.setCurrency("USD");
    List<WalletDto> wallets = List.of(dto1, dto2);
    when(walletService.getAllWallets()).thenReturn(wallets);

    mockMvc
        .perform(get("/wallets"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].name").value("Wallet de Jane"))
        .andExpect(jsonPath("$[1].name").value("Wallet de John"));
  }
}
