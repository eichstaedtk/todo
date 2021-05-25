package de.eichstaedt.todos;

import static de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper.DATE_FORMAT;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import de.eichstaedt.todos.databinding.ActivityDetailviewBinding;
import de.eichstaedt.todos.domain.ToDo;
import de.eichstaedt.todos.domain.User;
import de.eichstaedt.todos.infrastructure.persistence.DataService;
import de.eichstaedt.todos.infrastructure.persistence.ToDoRepository;
import de.eichstaedt.todos.infrastructure.view.ToDoDetailView;
import de.eichstaedt.todos.infrastructure.view.UserArrayAdapter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;
import org.parceler.Parcels;

public class DetailViewActivity extends AppCompatActivity {

  public static final String TODO_BUNDLE = "TODO_BUNDLE";
  public static final String TODO_PARCEL = "TODO_PARCEL";

  private ToDoDetailView toDoDetailView;

  private ActivityDetailviewBinding binding;

  private DataService dataService;

  private UserArrayAdapter userArrayAdapter;

  protected static final String logger = DetailViewActivity.class.getName();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i(logger,"Creating DetailView Activity");

    dataService = DataService.instance(getApplicationContext());
    dataService.checkOfflineState();
    binding = DataBindingUtil.setContentView(this, R.layout.activity_detailview);
    Intent intent = getIntent();

    Bundle b = intent.getBundleExtra(TODO_BUNDLE);
    ToDo toDo = Parcels.unwrap(b.getParcelable(TODO_PARCEL));
    toDoDetailView = new ToDoDetailView(toDo.getId(), toDo.getName(),
        toDo.getBeschreibung(),toDo.isErledigt(),toDo.isWichtig(),toDo.getFaellig(), toDo.getKontakte());

    dataService.findAllUser((List<User> user) -> {
      userArrayAdapter = new UserArrayAdapter(this,user, toDo);
      binding.userSelectionListView.setAdapter(userArrayAdapter);
    });

    binding.setController(this);
  }

  public void onClickSaveToDoButton() {
    Log.i(logger,"Click on Save the Details of ToDo "+toDoDetailView.toString());
    Intent returnIntent = new Intent(getApplicationContext(), MainActivity.class);

    ToDo toDo = new ToDo(toDoDetailView);
    Bundle bundle = new Bundle();
    bundle.putString(MainActivity.RETURN_ACTION,MainActivity.RETURN_ACTION_SAVE);
    bundle.putParcelable(TODO_PARCEL, Parcels.wrap(toDo));
    returnIntent.putExtra(TODO_BUNDLE, bundle);

    this.setResult(Activity.RESULT_OK,returnIntent);
    this.finish();
  }

  public void onClickDeleteToDoButton() {
    Log.i(logger,"Click on Save the Details of ToDo "+toDoDetailView.toString());

    new MaterialAlertDialogBuilder(this).setTitle(R.string.confirm_delete)
        .setNegativeButton(R.string.no,(dialog, which ) -> {
          dialog.dismiss();
        })
        .setPositiveButton(R.string.yes,(dialog, which)->{
          Intent returnIntent = new Intent(getApplicationContext(), MainActivity.class);
          ToDo toDo = new ToDo(toDoDetailView);
          Bundle bundle = new Bundle();
          bundle.putParcelable(TODO_PARCEL, Parcels.wrap(toDo));
          bundle.putString(MainActivity.RETURN_ACTION,MainActivity.RETURN_ACTION_DELETE);
          returnIntent.putExtra(TODO_BUNDLE, bundle);

          this.setResult(Activity.RESULT_OK,returnIntent);
          this.finish();
        })
        .show();
  }

  public void showDatePicker() {

    MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
        .setSelection(System.currentTimeMillis())
        .build();

    datePicker.addOnPositiveButtonClickListener(date -> {
      LocalDateTime triggerTime =
          LocalDateTime.ofInstant(Instant.ofEpochMilli(date),
              TimeZone.getDefault().toZoneId());
      toDoDetailView.setFaellig(triggerTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
      dataService.updateToDo(new ToDo(toDoDetailView),(s) -> {binding.invalidateAll();});

    });
    datePicker.show(this.getSupportFragmentManager(),"DATE_PICKER");

  }

  public ToDoDetailView getToDoDetailView() {
    return toDoDetailView;
  }

  public void setToDoDetailView(ToDoDetailView toDoDetailView) {
    this.toDoDetailView = toDoDetailView;
  }
}
