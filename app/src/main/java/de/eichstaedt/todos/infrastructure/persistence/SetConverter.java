package de.eichstaedt.todos.infrastructure.persistence;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class SetConverter {

  @TypeConverter
  public static Set<String> toSet(String setString) {
    if (setString == null) {
      throw new IllegalArgumentException("Wrong String date value");
    } else {
      Gson gson = new Gson();
      return gson.fromJson(setString,Set.class);
    }
  }

  @TypeConverter
  public static String toString(Set<String> set) {
    if (set == null) {
      throw new IllegalArgumentException("Wrong Set value");
    } else {
      Gson gson = new Gson();
      return gson.toJson(set);
    }
  }

}
