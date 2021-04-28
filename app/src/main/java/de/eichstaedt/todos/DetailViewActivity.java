package de.eichstaedt.todos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import de.eichstaedt.todos.domain.ToDo;
import de.eichstaedt.todos.infrastructure.persistence.ToDoDataService;
import de.eichstaedt.todos.infrastructure.persistence.ToDoRepository;
import java.time.LocalDateTime;

public class DetailViewActivity extends AppCompatActivity {

  public static final String ARG_NAME = "name";

  public static final String ARG_BESCHREIBUNG = "beschreibung";

  private EditText editTextName;

  private EditText editTextBeschreibung;

  private FloatingActionButton saveToDoButton;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detailview);

    this.saveToDoButton = findViewById(R.id.saveToDoButton);

    this.saveToDoButton.setOnClickListener((view)-> {
      onClickSaveToDoButton();
    });

    editTextName = findViewById(R.id.itemName);
    String textName = getIntent().getStringExtra(ARG_NAME);
    editTextName.setText(textName);

    editTextBeschreibung = findViewById(R.id.itemBeschreibung);
    String textBeschreibung = getIntent().getStringExtra(ARG_BESCHREIBUNG);
    editTextBeschreibung.setText(textBeschreibung);
  }

  private void onClickSaveToDoButton() {
    Intent returnIntent = new Intent(this, MainActivity.class);
    returnIntent.putExtra(ARG_NAME,editTextName.getText().toString());
    returnIntent.putExtra(ARG_BESCHREIBUNG,editTextBeschreibung.getText().toString());
    this.setResult(Activity.RESULT_OK,returnIntent);
    finish();
  }
}
