package de.eichstaedt.todos.domain;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.UUID;

import de.eichstaedt.todos.infrastructure.persistence.DateConverter;

@Entity(tableName = "todos")
@TypeConverters({DateConverter.class})
public class ToDo {

    public ToDo() {
    }

    public ToDo(String name, String beschreibung, Date faellig) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.beschreibung = beschreibung;
        this.faellig = faellig;
    }

    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "NAME")
    private String name;

    @ColumnInfo(name = "BESCHREIBUNG")
    private String beschreibung;

    @ColumnInfo(name = "ERLEDIGT")
    private boolean erledigt;

    @ColumnInfo(name = "WICHTIG")
    private boolean wichtig;

    @ColumnInfo(name = "FAELLIG_DATUM")
    private Date faellig;

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

    public Date getFaellig() {
        return faellig;
    }

    public void setFaellig(Date faellig) {
        this.faellig = faellig;
    }
}
