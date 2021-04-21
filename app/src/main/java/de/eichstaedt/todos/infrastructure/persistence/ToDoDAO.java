package de.eichstaedt.todos.infrastructure.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
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
    void insertAll(List<ToDo> todos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAllAsync(List<ToDo> todos);

    @Insert
    void insert(ToDo toDo);

    @Insert
    Completable insertAsync(ToDo toDo);

    @Query("DELETE FROM todos")
    void deleteAll();

    @Query("DELETE FROM todos")
    Completable deleteAllAsync();
}
