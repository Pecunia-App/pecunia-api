package com.pecunia.api.dto;

public class ProfilePictureDTO {
  private Long id;
  private String picture;
  private Long userId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public String getPicture() {
    return picture;
  }
}
