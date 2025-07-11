package com.pecunia.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "User Login DTO", description = "Represents a user for login")
public class UserLoginDTO {

  private String email;

  private String password;

  public String getEmail() {
      return email;
  }

  public void setEmail(String email) {
      this.email = email;
  }

  public String getPassword() {
      return password;
  }

  public void setPassword(String password) {
        this.password = password;
    }
}
