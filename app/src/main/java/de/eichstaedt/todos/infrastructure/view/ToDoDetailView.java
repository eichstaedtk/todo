package de.eichstaedt.todos.infrastructure.view;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;
import java.util.Date;

public class ToDoDetailView extends BaseObservable {

  public ToDoDetailView(String name, String beschreibung, boolean erledigt, boolean wichtig,
      Date faellig) {
    this.name = name;
    this.beschreibung = beschreibung;
    this.erledigt = erledigt;
    this.wichtig = wichtig;
    this.faellig = faellig;
  }

  private String name;

  private String beschreibung;

  private boolean erledigt;

  private boolean wichtig;

  private Date faellig;

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
  public Date getFaellig() {
    return faellig;
  }

  public void setFaellig(Date faellig) {
    this.faellig = faellig;
  }
}
