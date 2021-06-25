package de.eichstaedt.todos.infrastructure.persistence;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import org.junit.Test;

public class SetConverterTest {

  @Test
  public void test_toSet() {

    assertEquals(new HashSet<>(),SetConverter.toSet("[]"));
  }
}
