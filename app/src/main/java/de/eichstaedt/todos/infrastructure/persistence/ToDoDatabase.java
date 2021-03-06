package de.eichstaedt.todos.infrastructure.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import de.eichstaedt.todos.domain.entities.ToDo;

@Database(entities = {ToDo.class}, version = 7, exportSchema = false)
public abstract class ToDoDatabase extends RoomDatabase {

    public abstract ToDoDAO toDoDAO();
}
