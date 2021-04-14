package de.eichstaedt.todos.infrastructure.persistence;

import static java.time.LocalDateTime.parse;
import androidx.room.TypeConverter;
import java.time.LocalDateTime;

public class LocalDateTimeConverter {

  @TypeConverter
  public static LocalDateTime toDate(String dateString) {
    if (dateString == null) {
      return null;
    } else {
      return parse(dateString);
    }
  }

  @TypeConverter
  public static String toDateString(LocalDateTime date) {
    if (date == null) {
      return null;
    } else {
      return date.toString();
    }
  }

}
