package de.eichstaedt.todos.infrastructure.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import androidx.room.Transaction;
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

    @Transaction
    @Insert
    void insertAll(List<ToDo> todos);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAllAsync(List<ToDo> todos);

    @Transaction
    @Insert
    void insert(ToDo toDo);

    @Transaction
    @Insert
    Completable insertAsync(ToDo toDo);

    @Transaction
    @Query("DELETE FROM todos")
    void deleteAll();

    @Delete
    Completable deleteTodos(List<ToDo> todos);
}
