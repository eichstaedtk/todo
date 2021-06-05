package de.eichstaedt.todos;

import static de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper.DATE_FORMAT;

import android.Manifest.permission;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import de.eichstaedt.todos.databinding.ActivityDetailviewBinding;
import de.eichstaedt.todos.domain.ToDo;
import de.eichstaedt.todos.domain.User;
import de.eichstaedt.todos.infrastructure.persistence.DataService;
import de.eichstaedt.todos.infrastructure.view.ToDoDetailView;
import de.eichstaedt.todos.infrastructure.view.UserArrayAdapter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Collectors;
import org.parceler.Parcels;

public class DetailViewActivity extends AppCompatActivity {

  public static final String TODO_BUNDLE = "TODO_BUNDLE";
  public static final String TODO_PARCEL = "TODO_PARCEL";

  private ToDoDetailView toDoDetailView;

  private ActivityDetailviewBinding binding;

  private DataService dataService;

  private UserArrayAdapter userArrayAdapter;

  private static final int PICK_CONTACT = 0;

  protected static final String logger = DetailViewActivity.class.getName();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i(logger,"Creating DetailView Activity");

    dataService = ((Application)this.getApplication()).getDataService();
    dataService.checkOfflineState();
    binding = DataBindingUtil.setContentView(this, R.layout.activity_detailview);
    Intent intent = getIntent();

    Bundle b = intent.getBundleExtra(TODO_BUNDLE);
    if(b != null) {
      ToDo toDo = Parcels.unwrap(b.getParcelable(TODO_PARCEL));
      toDoDetailView = new ToDoDetailView(toDo.getId(), toDo.getName(),
          toDo.getBeschreibung(), toDo.isErledigt(), toDo.isWichtig(), toDo.getFaellig(),
          toDo.getKontakte());

      if(checkPermission()) {
        userArrayAdapter = new UserArrayAdapter(this,
            toDo.getKontakte().stream().map(id -> readContactAsUser(id))
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
            , toDo, false);
        binding.userSelectionListView.setAdapter(userArrayAdapter);
      }
    }

    binding.setController(this);
  }

  public User readContactAsUser(String id) {

    Cursor c = getContentResolver().query(Contacts.CONTENT_URI, null, ContactsContract.Contacts._ID + " = ?",
        new String[]{id}, null);

    if(c != null) {

      String name = "";
      String phNo = "";
      String mail = "";

      while (c.moveToNext()) {

        name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

        Cursor phoneCursor = getContentResolver().query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
            new String[]{id}, null);

        Cursor EmailCursor = getContentResolver().query(
            Email.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
            new String[]{id}, null);

        while (phoneCursor.moveToNext()) {
          phNo = phoneCursor.getString(phoneCursor.getColumnIndex(Phone.NUMBER));
        }

        while (EmailCursor.moveToNext()) {
          mail = EmailCursor.getString(EmailCursor.getColumnIndex(Email.ADDRESS));
        }
      }

      if(id != null && name != null) {
        return new User(id, name, mail, 123456, phNo);
      }
    }

    return null;
  }

  public void onClickKontakteVerknuepfen() {

    if(checkPermission()) {
      Intent contactIntent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
      this.startActivityForResult(contactIntent, PICK_CONTACT);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    Log.i(logger,"Getting Intent Result of Contact App "+resultCode+" RequestCode"+requestCode);

    if ((requestCode == PICK_CONTACT) && (resultCode == RESULT_OK)) {
      Uri contactUri = data.getData();
      Cursor c = getContentResolver().query(contactUri, null, null, null, null);

      if(c != null && c.moveToFirst()) {
        Log.i(logger,"Contact Fields"+ Arrays.asList(c.getColumnNames()).stream().collect(Collectors.joining(",")));
        String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
        toDoDetailView.getKontakte().add(id);
        ToDo todo = new ToDo(toDoDetailView);

        dataService.updateToDo(todo,(result)->{
          userArrayAdapter.getUsers().clear();
          userArrayAdapter.getUsers().addAll(todo.getKontakte().stream().map(contactid -> readContactAsUser(contactid)).collect(Collectors.toList()));
          userArrayAdapter.notifyDataSetChanged();
          binding.invalidateAll();
        });

      }
    }
  }

  private boolean checkPermission() {
    int hasPermission = checkSelfPermission(permission.READ_CONTACTS);

    if(hasPermission != PackageManager.PERMISSION_GRANTED)
    {
      requestPermissions(new String[]{permission.READ_CONTACTS},PICK_CONTACT);
    }

    return hasPermission == PackageManager.PERMISSION_GRANTED;
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
        .setNegativeButton(R.string.no,(dialog, which ) -> dialog.dismiss())
        .setPositiveButton(R.string.yes,(dialog, which)-> {
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

  public void showTimePicker() {

    MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
        .setTimeFormat(TimeFormat.CLOCK_24H)
        .setHour(12)
        .setMinute(0)
        .setTitleText("Zeit auswählen")
        .build();

    timePicker.addOnPositiveButtonClickListener(dialog -> {
        LocalDateTime faellig = LocalDateTime.parse(toDoDetailView.getFaellig(),DateTimeFormatter.ofPattern(DATE_FORMAT));
        Log.i(logger,"TimePicket Get Hour : "+timePicker.getHour());
        LocalDateTime faelligAdjusted = faellig.withHour(timePicker.getHour()).withMinute(timePicker.getMinute());
        Log.i(logger,"TimePicket Set Faellig: "+faelligAdjusted.format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
        toDoDetailView.setFaellig(faelligAdjusted.format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
        dataService.updateToDo(new ToDo(toDoDetailView),(s) -> {binding.invalidateAll();});
    });

    timePicker.show(this.getSupportFragmentManager(),"TIME_PICKER");
  }

  public ToDoDetailView getToDoDetailView() {
    return toDoDetailView;
  }

  public void setToDoDetailView(ToDoDetailView toDoDetailView) {
    this.toDoDetailView = toDoDetailView;
  }
}
