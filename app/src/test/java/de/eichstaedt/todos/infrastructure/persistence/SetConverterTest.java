package de.eichstaedt.todos.infrastructure.persistence;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class SetConverterTest {

  @Test
  public void test_toSet() {
    assertEquals(new HashSet<>(),SetConverter.toSet("[]"));
  }

  @Test
  public void test_toString() {
    Set<String> testSet = new HashSet<>();
    testSet.add("1");

    assertEquals("[\"1\"]",SetConverter.toString(testSet));
  }
}
