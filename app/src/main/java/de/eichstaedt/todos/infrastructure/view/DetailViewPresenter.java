package de.eichstaedt.todos.infrastructure.view;

import static de.eichstaedt.todos.DetailViewActivity.TODO_BUNDLE;
import static de.eichstaedt.todos.DetailViewActivity.TODO_PARCEL;
import static de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper.DATE_FORMAT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import de.eichstaedt.todos.DetailViewActivity;
import de.eichstaedt.todos.MainActivity;
import de.eichstaedt.todos.R;
import de.eichstaedt.todos.databinding.ActivityDetailviewBinding;
import de.eichstaedt.todos.domain.ToDo;
import de.eichstaedt.todos.infrastructure.persistence.DataService;
import de.eichstaedt.todos.infrastructure.persistence.ToDoRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import org.parceler.Parcels;

public class DetailViewPresenter implements DetailViewBindingContract.Presenter{

  private DetailViewActivity activity;

  private Context context;

  private ActivityDetailviewBinding binding;

  private DataService dataService;

  protected static final String logger = DetailViewPresenter.class.getName();

  public DetailViewPresenter(
      DetailViewActivity activity, Context context, ActivityDetailviewBinding binding) {
    this.activity = activity;
    this.context = context;
    this.binding = binding;
    this.dataService = new DataService(ToDoRepository.getInstance(context));
  }

  @Override
  public void onClickSaveToDoButton(ToDoDetailView toDoDetailView) {
    Log.i(logger,"Click on Save the Details of ToDo "+toDoDetailView.toString());
    Intent returnIntent = new Intent(context, MainActivity.class);

    ToDo toDo = new ToDo(toDoDetailView);
    Bundle bundle = new Bundle();
    bundle.putString(MainActivity.RETURN_ACTION,MainActivity.RETURN_ACTION_SAVE);
    bundle.putParcelable(TODO_PARCEL, Parcels.wrap(toDo));
    returnIntent.putExtra(TODO_BUNDLE, bundle);

    activity.setResult(Activity.RESULT_OK,returnIntent);
    activity.finish();
  }

  @Override
  public void onClickDeleteToDoButton(ToDoDetailView toDoDetailView) {
    Log.i(logger,"Click on Save the Details of ToDo "+toDoDetailView.toString());

    new MaterialAlertDialogBuilder(activity).setTitle(R.string.confirm_delete)
        .setNegativeButton(R.string.no,(dialog, which ) -> {
            dialog.dismiss();
        })
        .setPositiveButton(R.string.yes,(dialog, which)->{
          Intent returnIntent = new Intent(context, MainActivity.class);
          ToDo toDo = new ToDo(toDoDetailView);
          Bundle bundle = new Bundle();
          bundle.putParcelable(TODO_PARCEL, Parcels.wrap(toDo));
          bundle.putString(MainActivity.RETURN_ACTION,MainActivity.RETURN_ACTION_DELETE);
          returnIntent.putExtra(TODO_BUNDLE, bundle);

          activity.setResult(Activity.RESULT_OK,returnIntent);
          activity.finish();
        })
        .show();
  }

  public void showDatePicker(ToDoDetailView toDoDetailView) {

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
    datePicker.show(activity.getSupportFragmentManager(),"DATE_PICKER");

 }

}
