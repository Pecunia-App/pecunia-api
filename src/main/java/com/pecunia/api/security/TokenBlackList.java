package com.pecunia.api.security;

import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class TokenBlackList {

  private Set<String> blacklist = new HashSet<>();

  public void addToBlacklist(String token) {
    blacklist.add(token);
  }

  public boolean isBlacklisted(String token) {
    return blacklist.contains(token);
  }
}
