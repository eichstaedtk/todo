package de.eichstaedt.todos.infrastructure.persistence;

import java.util.concurrent.ExecutionException;

public interface ReloadViewCallback {

  void onComplete(String message) throws ExecutionException, InterruptedException;
}
