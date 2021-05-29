package de.eichstaedt.todos.domain;

import static de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper.DATE_FORMAT;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Junction;
import androidx.room.PrimaryKey;
import androidx.room.Relation;
import androidx.room.TypeConverters;
import de.eichstaedt.todos.infrastructure.persistence.LocalDateTimeConverter;
import de.eichstaedt.todos.infrastructure.persistence.SetConverter;
import de.eichstaedt.todos.infrastructure.view.ToDoDetailView;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.parceler.Parcel;

@Parcel
@Entity(tableName = "todos")
@TypeConverters({LocalDateTimeConverter.class, SetConverter.class})
public class ToDo {

    public ToDo() {
        this.id = UUID.randomUUID().toString();
        this.faellig = LocalDateTime.now();
        this.kontakte = new HashSet<>();
    }

    public ToDo(@NonNull ToDoDetailView toDoDetailView) {
        this.id = toDoDetailView.getId();
        this.name = toDoDetailView.getName();
        this.beschreibung = toDoDetailView.getBeschreibung();
        this.erledigt = toDoDetailView.isErledigt();
        this.wichtig = toDoDetailView.isWichtig();
        this.faellig = LocalDateTime.parse(toDoDetailView.getFaellig(),DateTimeFormatter.ofPattern(DATE_FORMAT));
        this.kontakte = toDoDetailView.getKontakte();
    }

    @Ignore
    public ToDo(@NonNull String id, String name, String beschreibung) {
        this.id = id;
        this.name = name;
        this.beschreibung = beschreibung;
        this.kontakte = new HashSet<>();
    }

    @Ignore
    public ToDo(String name, String beschreibung, LocalDateTime faellig, boolean wichtig) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.beschreibung = beschreibung;
        this.faellig = faellig;
        this.wichtig = wichtig;
        this.erledigt = false;
        this.kontakte = new HashSet<>();
    }

    @Ignore
    public ToDo(String id,String name, String beschreibung, LocalDateTime faellig, boolean wichtig, boolean erledigt, Set<String> kontakte) {
        this.id = id;
        this.name = name;
        this.beschreibung = beschreibung;
        this.faellig = faellig;
        this.wichtig = wichtig;
        this.erledigt = erledigt;
        this.kontakte = kontakte;
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
    private LocalDateTime faellig;

    @ColumnInfo(name = "KONTAKTE")
    private Set<String> kontakte;

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

    public LocalDateTime getFaellig() {
        return faellig;
    }

    public void setFaellig(LocalDateTime faellig) {
        this.faellig = faellig;
    }

    public Set<String> getKontakte() {
        return kontakte;
    }

    public void setKontakte(Set<String> kontakte) {
        this.kontakte = kontakte;
    }

    @Override
    public String toString() {
        return "ToDo{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", beschreibung='" + beschreibung + '\'' +
            ", erledigt=" + erledigt +
            ", wichtig=" + wichtig +
            ", faellig=" + faellig +
            ", kontakte=" + kontakte.size() +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ToDo toDo = (ToDo) o;
        return id.equals(toDo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
