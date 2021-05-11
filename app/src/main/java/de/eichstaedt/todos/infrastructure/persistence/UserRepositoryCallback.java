package de.eichstaedt.todos.infrastructure.persistence;

import de.eichstaedt.todos.domain.User;
import java.util.Optional;

public interface UserRepositoryCallback {

  void onComplete(Optional<User> user);

}
