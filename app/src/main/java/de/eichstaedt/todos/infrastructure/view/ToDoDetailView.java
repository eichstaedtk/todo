package de.eichstaedt.todos.infrastructure.view;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Class for Detail View View Model with Databinding
 */

public class ToDoDetailView extends BaseObservable {

  public ToDoDetailView(String id,String name, String beschreibung, boolean erledigt, boolean wichtig,
      LocalDateTime faellig) {
    this.id = id;
    this.name = name;
    this.beschreibung = beschreibung;
    this.erledigt = erledigt;
    this.wichtig = wichtig;
    this.faellig = faellig;
  }

  private String id;

  private String name;

  private String beschreibung;

  private boolean erledigt;

  private boolean wichtig;

  private LocalDateTime faellig;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Bindable
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
    this.notifyPropertyChanged(BR.name);
  }

  @Bindable
  public String getBeschreibung() {
    return beschreibung;
  }

  public void setBeschreibung(String beschreibung) {
    this.beschreibung = beschreibung;
    this.notifyPropertyChanged(BR.beschreibung);
  }

  @Bindable
  public boolean isErledigt() {
    return erledigt;
  }

  public void setErledigt(boolean erledigt) {
    this.erledigt = erledigt;
  }

  @Bindable
  public boolean isWichtig() {
    return wichtig;
  }

  public void setWichtig(boolean wichtig) {
    this.wichtig = wichtig;
  }

  @Bindable
  public LocalDateTime getFaellig() {
    return faellig;
  }

  public void setFaellig(LocalDateTime faellig) {
    this.faellig = faellig;
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
