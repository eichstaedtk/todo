package de.eichstaedt.todos;

import android.os.Bundle;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DetailViewActivity extends AppCompatActivity {

  public static final String ARG_NAME = "name";

  public static final String ARG_BESCHREIBUNG = "beschreibung";

  private EditText editTextName;

  private EditText editTextBeschreibung;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detailview);

    editTextName = findViewById(R.id.itemName);
    String textName = getIntent().getStringExtra(ARG_NAME);
    editTextName.setText(textName);

    editTextBeschreibung = findViewById(R.id.itemBeschreibung);
    String textBeschreibung = getIntent().getStringExtra(ARG_BESCHREIBUNG);
    editTextBeschreibung.setText(textBeschreibung);
  }
}
