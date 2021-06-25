package de.eichstaedt.todos.infrastructure.persistence;

import androidx.room.TypeConverter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeConverter {

  @TypeConverter
  public static LocalDateTime toDate(String dateString) {
    if (dateString == null) {
      throw new IllegalArgumentException("Wrong String date value");
    } else {
      return LocalDateTime.parse(dateString,DateTimeFormatter.ofPattern(FirebaseDocumentMapper.DATE_FORMAT));
    }
  }

  @TypeConverter
  public static String toDateString(LocalDateTime date) {
    if (date == null) {
      throw new IllegalArgumentException("Wrong LocalDateTime value");
    } else {
      return date.format(DateTimeFormatter.ofPattern(FirebaseDocumentMapper.DATE_FORMAT));
    }
  }

}
