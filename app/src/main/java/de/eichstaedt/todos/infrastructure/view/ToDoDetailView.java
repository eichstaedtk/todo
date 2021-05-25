package de.eichstaedt.todos.infrastructure.view;

import static de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper.DATE_FORMAT;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * Class for Detail View View Model with Databinding
 */

public class ToDoDetailView {

  public ToDoDetailView(String id,String name, String beschreibung, boolean erledigt, boolean wichtig,
      LocalDateTime faellig, Set<String> kontakte) {
    this.id = id;
    this.name = name;
    this.beschreibung = beschreibung;
    this.erledigt = erledigt;
    this.wichtig = wichtig;
    this.faellig = faellig.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    this.kontakte = kontakte;
  }

  private String id;

  private String name;

  private String beschreibung;

  private boolean erledigt;

  private boolean wichtig;

  private String faellig;

  private final Set<String> kontakte;

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

  public String getBeschreibung() {
    return beschreibung;
  }

  public void setBeschreibung(String beschreibung) {
    this.beschreibung = beschreibung;
  }

  public boolean isErledigt() {
    return erledigt;
  }

  public void setErledigt(boolean erledigt) {
    this.erledigt = erledigt;
  }

  public boolean isWichtig() {
    return wichtig;
  }

  public void setWichtig(boolean wichtig) {
    this.wichtig = wichtig;
  }

  public String getFaellig() {
    return faellig;
  }

  public void setFaellig(String faellig) {
    this.faellig = faellig;
  }

  public Set<String> getKontakte() {
    return kontakte;
  }

  @Override
  public String toString() {
    return "ToDoDetailView{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", beschreibung='" + beschreibung + '\'' +
        ", erledigt=" + erledigt +
        ", wichtig=" + wichtig +
        ", faellig=" + faellig +
        '}';
  }
}
