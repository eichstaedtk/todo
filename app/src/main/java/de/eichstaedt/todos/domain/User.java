package de.eichstaedt.todos.domain;

import androidx.annotation.NonNull;
import java.util.Objects;
import java.util.UUID;
import org.parceler.Parcel;

@Parcel
public class User {

  public User() {
    this.id = UUID.randomUUID().toString();
  }

  public User(String name, String email, Integer passwort) {
    this.id = UUID.randomUUID().toString();
    this.name = name;
    this.email = email;
    this.passwort = passwort;
  }

  public User(String id, String name, String email, Integer passwort, String mobilnummer) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.passwort = passwort;
    this.mobilnummer = mobilnummer;
  }

  @NonNull
  private String id;

  private String name;

  private String email;

  private Integer passwort;

  private String mobilnummer;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Integer getPasswort() {
    return passwort;
  }

  public void setPasswort(Integer passwort) {
    this.passwort = passwort;
  }

  public String getMobilnummer() {
    return mobilnummer;
  }

  public void setMobilnummer(String mobilnummer) {
    this.mobilnummer = mobilnummer;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return id.equals(user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "User{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", email='" + email + '\'' +
        ", passwort='" + passwort + '\'' +
        ", mobilnummer='" + mobilnummer + '\'' +
        '}';
  }
}
