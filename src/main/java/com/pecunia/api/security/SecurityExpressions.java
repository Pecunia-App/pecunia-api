package com.pecunia.api.security;

import com.pecunia.api.model.User;
import com.pecunia.api.repository.CategoryRepository;
import com.pecunia.api.repository.ProviderRepository;
import com.pecunia.api.repository.TransactionRepository;
import com.pecunia.api.repository.UserRepository;
import com.pecunia.api.repository.WalletRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/** Bean pour la création d'annotation personnalisés pour les méthodes. */
@Component("authz")
public class SecurityExpressions {
  private final TransactionRepository transactionRepository;
  private final WalletRepository walletRepository;
  private final UserRepository userRepository;
  private final ProviderRepository providerRepository;
  private final CategoryRepository categoryRepository;

  public SecurityExpressions(
      TransactionRepository transactionRepository,
      WalletRepository walletRepository,
      UserRepository userRepository,
      ProviderRepository providerRepository,
      CategoryRepository categoryRepository) {
    this.transactionRepository = transactionRepository;
    this.walletRepository = walletRepository;
    this.userRepository = userRepository;
    this.providerRepository = providerRepository;
    this.categoryRepository = categoryRepository;
  }

  /**
   * Méthode pour voir si l'utilisateur peut accéder à une ressource.
   *
   * @param resourceId id of wallet or transaction
   * @param resourceType wallet or transaction
   * @return boolean
   */
  public boolean isOwner(Long resourceId, String resourceType) {
    Long currentUserId = getCurrentUserId();
    return switch (resourceType) {
      case "transaction" ->
          transactionRepository.existsByIdAndWalletUserId(resourceId, currentUserId);
      case "wallet" -> walletRepository.existsByIdAndUserId(resourceId, currentUserId);
      case "provider" -> providerRepository.existsByIdAndUserId(resourceId, currentUserId);
      case "category" -> categoryRepository.existsByIdAndUserId(resourceId, currentUserId);
      default -> throw new IllegalArgumentException("Uknown resource type: " + resourceType);
    };
  }

  private Long getCurrentUserId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth.getPrincipal() instanceof User user) {
      return user.getId();
    }
    throw new IllegalArgumentException("Cannot get current userid.");
  }
}
