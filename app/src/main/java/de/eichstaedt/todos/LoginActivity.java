package de.eichstaedt.todos;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import de.eichstaedt.todos.databinding.ActivityLoginBinding;
import de.eichstaedt.todos.domain.entities.User;
import de.eichstaedt.todos.infrastructure.persistence.DataService;
import de.eichstaedt.todos.infrastructure.persistence.UserRepositoryCallback;
import java.util.Optional;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements UserRepositoryCallback,
    OnFocusChangeListener {

  private String email;

  private String passwort;

  private ActivityLoginBinding binding;

  private DataService dataService;

  private MaterialButton anmelden;

  private TextView errorText;

  private TextInputEditText emailInput;

  private TextInputEditText passwordInput;

  private LinearProgressIndicator progress;

  private final boolean developmentMode = false;

  private static final String logger = LoginActivity.class.getName();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    dataService = ((Application)this.getApplication()).getDataService();
    dataService.checkOfflineState().isDone();
    binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
    binding.setController(this);
    anmelden = findViewById(R.id.LoginButton);
    errorText = findViewById(R.id.loginErrorText);

    emailInput = findViewById(R.id.emailTextInput);
    emailInput.setOnFocusChangeListener(this);

    passwordInput = findViewById(R.id.passwordTextInput);
    passwordInput.setOnFocusChangeListener(this);

    progress = findViewById(R.id.loginProgress);
    progress.setVisibility(View.INVISIBLE);

    if(developmentMode | dataService.isOffline())
    {
      showOfflineMessageAndProceedWithMainActivity();
    }
  }

  public void login() {
    Log.i(logger,"Starting Login ...");
    progress.setVisibility(View.VISIBLE);
    if(!dataService.isOffline()) {
      dataService.findUserByEmailAndPasswort(email, passwort, this);
    }else {
      showOfflineMessageAndProceedWithMainActivity();
    }
  }

  private void showOfflineMessageAndProceedWithMainActivity() {
    Toast.makeText(getApplicationContext(), "Keine Internetverbindung verfügbar", Toast.LENGTH_LONG)
        .show();
    startToDoActivity();
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
    anmelden.setEnabled(loginActive());
    addOfflineMessage();
  }

  public String getPasswort() {
    return passwort;
  }

  public void setPasswort(String passwort) {
    this.passwort = passwort;
    anmelden.setEnabled(loginActive());
    errorText.setText("");
    addOfflineMessage();
  }

  @Override
  public void onComplete(Optional<User> user) {

    progress.setVisibility(View.INVISIBLE);

    if(user.isPresent()) {
      startToDoActivity();
    }else {
      Log.i(logger,"Login fehlgeschlagen");
      errorText.setText("Anmeldung gescheitert!");
    }
  }

  private void startToDoActivity() {
    Intent openmain = new Intent(LoginActivity.this, MainActivity.class);
    this.startActivity(openmain);
  }

  public boolean loginActive(){
    if(dataService.isOffline())
    {
      return true;
    }

    return email != null && !email.isEmpty() && passwort != null && !passwort.isEmpty();
  }

  @Override
  public void onFocusChange(View v, boolean hasFocus) {

    Log.i(logger,"Focus changed on "+v.getId()+" "+hasFocus);
    if(v.getId() == R.id.emailTextInput && !hasFocus){
      if(!isValidEmail(emailInput.getText())) {
        emailInput.setError("E-Mail Adresse ungültig!");
      }
    }

    if(v.getId() == R.id.passwordTextInput && !hasFocus){
      if(!isValidPassword(passwordInput.getText())) {
        passwordInput.setError("Passwort ungültig!");
      }
    }

  }

  private void addOfflineMessage() {
    if(dataService.isOffline())
    {
      errorText.setText("Offline bitte ohne Dateneingabe anmelden ...");
    }else {
      errorText.setText("");
    }
  }

  public static boolean isValidPassword(CharSequence target) {

    Pattern pattern = Pattern.compile("\\d{6}");

    return (!TextUtils.isEmpty(target) && pattern.matcher(target).matches());
  }

  public static boolean isValidEmail(CharSequence target) {
    return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
  }
}
