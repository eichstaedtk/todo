package de.eichstaedt.todos.infrastructure.persistence;

public interface RepositoryCallback<T> {

    void onComplete(T result);
}
