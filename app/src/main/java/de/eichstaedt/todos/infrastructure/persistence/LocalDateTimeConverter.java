package de.eichstaedt.todos.infrastructure.persistence;

import androidx.room.TypeConverter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeConverter {

  @TypeConverter
  public static LocalDateTime toDate(String dateString) {
    if (dateString == null) {
      return null;
    } else {
      return LocalDateTime.parse(dateString,DateTimeFormatter.ofPattern(FirebaseDocumentMapper.DATE_FORMAT));
    }
  }

  @TypeConverter
  public static String toDateString(LocalDateTime date) {
    if (date == null) {
      return null;
    } else {
      return date.format(DateTimeFormatter.ofPattern(FirebaseDocumentMapper.DATE_FORMAT));
    }
  }

}
