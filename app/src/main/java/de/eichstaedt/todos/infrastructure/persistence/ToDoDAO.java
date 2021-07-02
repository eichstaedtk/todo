package de.eichstaedt.todos.infrastructure.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Flowable;
import java.util.List;

import de.eichstaedt.todos.domain.entities.ToDo;

@Dao
public interface ToDoDAO {

    @Query("SELECT * FROM todos WHERE id = :id")
    Flowable<ToDo> findById(String id);

    @Query("SELECT * FROM todos")
    List<ToDo> getAll();

    @Query("SELECT * FROM todos")
    Flowable<List<ToDo>> getAllAsync();

    @Insert
    void insertTodos(List<ToDo> todos);

    @Insert
    void insertToDo(ToDo toDo);

    @Query("DELETE FROM todos")
    void deleteAll();

    @Delete
    void delete(ToDo toDo);

    @Update
    void update(ToDo toDo);
}
