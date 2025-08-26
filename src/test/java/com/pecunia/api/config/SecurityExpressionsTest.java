package com.pecunia.api.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pecunia.api.model.User;
import com.pecunia.api.repository.TransactionRepository;
import com.pecunia.api.repository.WalletRepository;
import com.pecunia.api.security.SecurityExpressions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
public class SecurityExpressionsTest {

  @Mock private TransactionRepository transactionRepository;

  @Mock private WalletRepository walletRepository;

  @InjectMocks SecurityExpressions securityExpressions;

  @Test
  public void shouldReturnTrueWhenUserIsOwnerOfWallet() {
    final Long userId = 1L;
    final Long walletId = 123L;

    User conectedUser = new User();
    conectedUser.setId(userId);
    conectedUser.setFirstname("name");
    conectedUser.setLastname("lastname");

    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(conectedUser, null);

    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);

    when(walletRepository.existsByIdAndUserId(walletId, userId)).thenReturn(true);

    boolean result = securityExpressions.isOwner(walletId, "wallet");
    assertTrue(result);
    verify(walletRepository).existsByIdAndUserId(walletId, userId);
  }

  @Test
  public void shouldReturnFalseWhenUserIsNotOwnerOfWallet() {
    final Long userId = 1L;
    final Long walletId = 123L;

    User conectedUser = new User();
    conectedUser.setId(userId);
    conectedUser.setFirstname("name");
    conectedUser.setLastname("lastname");

    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(conectedUser, null);
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);

    when(walletRepository.existsByIdAndUserId(walletId, userId)).thenReturn(false);
    boolean result = securityExpressions.isOwner(walletId, "wallet");
    assertFalse(result);
    verify(walletRepository).existsByIdAndUserId(walletId, userId);
  }
}
