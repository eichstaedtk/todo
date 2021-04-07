package de.eichstaedt.todos.infrastructure.persistence;

import android.content.Context;

import androidx.room.Room;

public class ToDoRepository {

    private static ToDoDatabase toDoDatabaseInstance;

    private ToDoRepository() {
    }

    public static ToDoDatabase getInstance(Context context) {

        if(toDoDatabaseInstance == null) {
            toDoDatabaseInstance = Room.databaseBuilder(context,
                    ToDoDatabase.class, "todo_db").build();
        }

        return toDoDatabaseInstance;

    }
}
