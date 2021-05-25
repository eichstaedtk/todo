package de.eichstaedt.todos.infrastructure.persistence;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import de.eichstaedt.todos.domain.ToDo;
import de.eichstaedt.todos.domain.User;

@Database(entities = {ToDo.class}, version = 7, exportSchema = false)
public abstract class ToDoDatabase extends RoomDatabase {

    public abstract ToDoDAO toDoDAO();
}
