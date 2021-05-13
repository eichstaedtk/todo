package de.eichstaedt.todos;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import de.eichstaedt.todos.databinding.ActivityLoginBinding;
import de.eichstaedt.todos.domain.User;
import de.eichstaedt.todos.infrastructure.persistence.DataService;
import de.eichstaedt.todos.infrastructure.persistence.ToDoRepository;
import de.eichstaedt.todos.infrastructure.persistence.UserRepositoryCallback;
import java.util.Optional;

public class LoginActivity extends AppCompatActivity implements UserRepositoryCallback,
    OnFocusChangeListener {

  private String email;

  private String passwort;

  private ActivityLoginBinding binding;

  private DataService dataService;

  private MaterialButton anmelden;

  private TextView errorText;

  private TextInputEditText emailInput;

  private static final String logger = LoginActivity.class.getName();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    dataService = DataService.instance(ToDoRepository.getInstance(getApplicationContext()));
    binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
    binding.setController(this);
    anmelden = findViewById(R.id.LoginButton);
    errorText = findViewById(R.id.loginErrorText);
    emailInput = findViewById(R.id.emailTextInput);
    emailInput.setOnFocusChangeListener(this);
  }

  public void login() {
    Log.i(logger,"Starting Login ...");
    dataService.findUserByEmail(email,passwort,this);
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

    if(user.isPresent()) {
      Intent openmain = new Intent(LoginActivity.this, MainActivity.class);
      this.startActivity(openmain);
    }else {
      Log.i(logger,"Login fehlgeschlagen");
      errorText.setText("Anmeldung gescheitert!");
    }
  }

  public boolean loginActive(){
    return email != null && !email.isEmpty() && passwort != null && !passwort.isEmpty();
  }

  @Override
  public void onFocusChange(View v, boolean hasFocus) {
    Log.i(logger,"Fovus changed on "+v.getId()+" "+hasFocus);
    if(v.getId() == R.id.emailTextInput && !hasFocus){
      if(!isValidEmail(emailInput.getText())) {
        errorText.setText("E-Mail Adresse ungültig!");
      }
    }else {
      errorText.setText("");
    }
  }

  public static boolean isValidEmail(CharSequence target) {
    return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
  }


}
