package de.eichstaedt.todos.infrastructure.persistence;

public interface ToDoRepositoryCallback<T> {

    void onComplete(T result, String message);
}
