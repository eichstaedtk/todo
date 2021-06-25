package de.eichstaedt.todos.infrastructure.persistence;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import org.junit.Test;

public class LocalDateTimeConverterTest {

  @Test
  public void test_toDate() {

    LocalDateTime date = LocalDateTime.of(2021,6,22,12,0,0);

    assertEquals(date,LocalDateTimeConverter.toDate("22.06.2021 12:00:00"));
  }

  @Test
  public void test_toDateString() {

    LocalDateTime date = LocalDateTime.of(2021,6,22,12,0,0);

    assertEquals("22.06.2021 12:00:00",LocalDateTimeConverter.toDateString(date));
  }
}
