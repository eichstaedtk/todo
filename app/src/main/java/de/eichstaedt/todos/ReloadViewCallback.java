package de.eichstaedt.todos;

import java.util.concurrent.ExecutionException;

public interface ReloadViewCallback {

  void onComplete(String message) throws ExecutionException, InterruptedException;
}
