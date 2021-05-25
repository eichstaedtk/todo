package de.eichstaedt.todos.infrastructure.view;

public class UserSelectionModel {

  public UserSelectionModel(String id, String name, String email, boolean selected) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.selected = selected;
  }

  private final String id;

  private String name;

  private String email;

  private boolean selected;

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

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
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
        ", selected=" + selected +
        '}';
  }
}
