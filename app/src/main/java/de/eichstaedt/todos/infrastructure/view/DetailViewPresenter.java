package de.eichstaedt.todos.infrastructure.view;

import static de.eichstaedt.todos.DetailViewActivity.TODO_BUNDLE;
import static de.eichstaedt.todos.DetailViewActivity.TODO_PARCEL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import de.eichstaedt.todos.DetailViewActivity;
import de.eichstaedt.todos.MainActivity;
import de.eichstaedt.todos.domain.ToDo;
import org.parceler.Parcels;

public class DetailViewPresenter implements DetailViewBindingContract.Presenter{

  private DetailViewActivity activity;

  private Context context;

  protected static final String logger = DetailViewPresenter.class.getName();

  public DetailViewPresenter(
      DetailViewActivity activity, Context context) {
    this.activity = activity;
    this.context = context;
  }

  @Override
  public void onClickSaveToDoButton(ToDoDetailView toDoDetailView) {
    Log.i(logger,"Click on Save the Details of ToDo "+toDoDetailView.toString());
    Intent returnIntent = new Intent(context, MainActivity.class);

    ToDo toDo = new ToDo(toDoDetailView);
    Bundle bundle = new Bundle();
    bundle.putParcelable(TODO_PARCEL, Parcels.wrap(toDo));
    returnIntent.putExtra(TODO_BUNDLE, bundle);

    activity.setResult(Activity.RESULT_OK,returnIntent);
    activity.finish();
  }

}
