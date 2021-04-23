package de.eichstaedt.todos.infrastructure.persistence;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase.Callback;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class ToDoRepository {

    private static ToDoDatabase toDoDatabaseInstance;

    private ToDoRepository() {
    }

    public static ToDoDatabase getInstance(Context context) {

        if(toDoDatabaseInstance == null) {
            toDoDatabaseInstance = Room.databaseBuilder(context,
                    ToDoDatabase.class, "todo_db")
                .fallbackToDestructiveMigration().build();
        }

        return toDoDatabaseInstance;

    }
}
