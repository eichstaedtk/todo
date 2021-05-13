package de.eichstaedt.todos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.adapters.TextViewBindingAdapter.OnTextChanged;
import com.google.android.material.button.MaterialButton;
import de.eichstaedt.todos.databinding.ActivityLoginBinding;
import de.eichstaedt.todos.domain.User;
import de.eichstaedt.todos.infrastructure.persistence.DataService;
import de.eichstaedt.todos.infrastructure.persistence.ToDoRepository;
import de.eichstaedt.todos.infrastructure.persistence.UserRepositoryCallback;
import java.util.Optional;

public class LoginActivity extends AppCompatActivity implements UserRepositoryCallback {

  private String email;

  private String passwort;

  private ActivityLoginBinding binding;

  private DataService dataService;

  private MaterialButton anmelden;

  private static final String logger = LoginActivity.class.getName();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    dataService = DataService.instance(ToDoRepository.getInstance(getApplicationContext()));
    binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
    binding.setController(this);
    anmelden = findViewById(R.id.LoginButton);
  }

  public void login() {
    Log.i(logger,"Starting Login ...");
    dataService.findUserByEmail(email,this);
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
    anmelden.setEnabled(loginActive());
  }

  public String getPasswort() {
    return passwort;
  }

  public void setPasswort(String passwort) {
    this.passwort = passwort;
    anmelden.setEnabled(loginActive());
  }

  @Override
  public void onComplete(Optional<User> user) {

    if(user.isPresent() && Integer.parseInt(passwort) == user.get().getPasswort()) {
      Intent openmain = new Intent(LoginActivity.this, MainActivity.class);
      this.startActivity(openmain);
    }else {
      Log.i(logger,"Login fehlgeschlagen");
    }
  }

  public boolean loginActive(){
    return email != null && !email.isEmpty() && passwort != null && !passwort.isEmpty();
  }
}
