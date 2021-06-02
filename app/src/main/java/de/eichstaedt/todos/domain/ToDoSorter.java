package de.eichstaedt.todos.domain;

import java.util.Collections;
import java.util.List;

public class ToDoSorter {

  public static void sortByErledigt(List<ToDo> toDos) {

    Collections.sort(toDos, (o1, o2) -> {
      if(o1.isErledigt() && o2.isErledigt()) return 1;
      else if(!o1.isErledigt() && o2.isErledigt()) return -1; else
      {
        return 0;
      }
    });
  }

  public static List<ToDo> sortByErledigtAndWichtigDatum(List<ToDo> toDos) {

    Collections.sort(toDos, (o1, o2) -> {
      int c = ((Boolean)o1.isErledigt()).compareTo(o2.isErledigt());
      if(c == 0)
      {
        c = ((Boolean)o2.isWichtig()).compareTo(o1.isWichtig());
      }

      if(c == 0)
      {
        c = o1.getFaellig().compareTo(o2.getFaellig());
      }

      return c;
    });

    return toDos;
  }

  public static List<ToDo> sortByErledigtAndDatumWichtig(List<ToDo> toDos) {

    Collections.sort(toDos, (o1, o2) -> {
      int c = ((Boolean)o1.isErledigt()).compareTo(o2.isErledigt());

      if(c == 0)
      {
        c = o1.getFaellig().compareTo(o2.getFaellig());
      }

      if(c == 0)
      {
        c = ((Boolean)o2.isWichtig()).compareTo(o1.isWichtig());
      }

      return c;
    });

    return toDos;
  }

}
