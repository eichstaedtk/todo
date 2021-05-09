package de.eichstaedt.todos.domain;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.UUID;
import org.parceler.Parcel;

@Parcel
@Entity
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

  @PrimaryKey
  @NonNull
  private String id;

  @ColumnInfo(name = "NAME")
  private String name;

  @ColumnInfo(name = "EMAIL")
  private String email;

  @ColumnInfo(name = "PASSWORD")
  private Integer passwort;

  @ColumnInfo(name = "MOBIL")
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
