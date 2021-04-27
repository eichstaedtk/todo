package de.eichstaedt.todos.infrastructure.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Completable;
import io.reactivex.Single;
import java.util.List;

import de.eichstaedt.todos.domain.ToDo;

@Dao
public interface ToDoDAO {

    @Query("SELECT * FROM todos")
    List<ToDo> getAll();

    @Query("SELECT * FROM todos")
    Single<List<ToDo>> getAllAsync();

    @Insert
    Completable insertTodos(List<ToDo> todos);

    @Insert
    Completable insertAsync(ToDo toDo);

    @Query("DELETE FROM todos")
    void deleteAll();
}
