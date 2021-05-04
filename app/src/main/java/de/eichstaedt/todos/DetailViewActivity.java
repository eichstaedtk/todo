package de.eichstaedt.todos;

import static de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper.DATE_FORMAT;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import de.eichstaedt.todos.databinding.ActivityDetailviewBinding;
import de.eichstaedt.todos.domain.ToDo;
import de.eichstaedt.todos.infrastructure.view.DetailViewPresenter;
import de.eichstaedt.todos.infrastructure.view.ToDoDetailView;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import org.parceler.Parcels;

public class DetailViewActivity extends AppCompatActivity {

  public static final String TODO_BUNDLE = "TODO_BUNDLE";
  public static final String TODO_PARCEL = "TODO_PARCEL";

  private ToDoDetailView toDoDetailView;

  protected static final String logger = DetailViewActivity.class.getName();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i(logger,"Creating DetailView Activity");

    ActivityDetailviewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detailview);
    DetailViewPresenter presenter = new DetailViewPresenter(this,getApplicationContext(),binding);
    Intent intent = getIntent();

    Bundle b = intent.getBundleExtra(TODO_BUNDLE);
    ToDo toDo = Parcels.unwrap(b.getParcelable(TODO_PARCEL));
    toDoDetailView = new ToDoDetailView(toDo.getId(), toDo.getName(),
        toDo.getBeschreibung(),toDo.isErledigt(),toDo.isWichtig(),toDo.getFaellig());


    binding.setTodo(toDoDetailView);
    binding.setPresenter(presenter);

  }
}
