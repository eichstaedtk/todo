package de.eichstaedt.todos.infrastructure.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import de.eichstaedt.todos.domain.ToDo;

@Dao
public interface ToDoDAO {

    @Query("SELECT * FROM todos")
    List<ToDo> getAll();

    @Insert
    void insertAll(List<ToDo> todos);

    @Insert
    void insert(ToDo toDo);

    @Query("DELETE FROM todos")
    void deleteAll();
}
