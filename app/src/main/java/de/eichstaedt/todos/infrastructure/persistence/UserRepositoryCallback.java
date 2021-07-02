package de.eichstaedt.todos.infrastructure.persistence;

import de.eichstaedt.todos.domain.entities.User;
import java.util.Optional;

public interface UserRepositoryCallback {

  void onComplete(Optional<User> user);

}
