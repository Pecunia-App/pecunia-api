package com.pecunia.api.model;

import jakarta.persistence.*;

@Entity
public class ProfilePicture {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Lob
  @Column(nullable = false, columnDefinition = "MEDIUMBLOB")
  private byte[] picture;

  @OneToOne(mappedBy = "profilePicture")
  private User user;

  // getter and setter

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public byte[] getPicture() {
    return picture;
  }

  public void setPicture(byte[] picture) {
    this.picture = picture;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
