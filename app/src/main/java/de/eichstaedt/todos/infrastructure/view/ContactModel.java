package de.eichstaedt.todos.infrastructure.view;

import de.eichstaedt.todos.domain.ToDo;

public class ContactModel {

  public ContactModel(String id, String name, String email,String mobil, ToDo toDo) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.mobil = mobil;
    this.toDo = toDo;
  }

  private final String id;

  private String name;

  private String email;

  private String mobil;

  private final ToDo toDo;

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

  public String getMobil() {
    return mobil;
  }

  public void setMobil(String mobil) {
    this.mobil = mobil;
  }

  public ToDo getToDo() {
    return toDo;
  }

  public String getId() {
    return id;
  }

  @Override
  public String toString() {
    return "UserSelectionModel{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", email='" + email + '\'' +
        '}';
  }
}
