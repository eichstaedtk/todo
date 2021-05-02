package de.eichstaedt.todos.infrastructure.view;

import static de.eichstaedt.todos.DetailViewActivity.ARG_BESCHREIBUNG;
import static de.eichstaedt.todos.DetailViewActivity.ARG_ID;
import static de.eichstaedt.todos.DetailViewActivity.ARG_NAME;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import de.eichstaedt.todos.DetailViewActivity;
import de.eichstaedt.todos.MainActivity;
import de.eichstaedt.todos.infrastructure.view.DetailViewBindingContract.View;

public class DetailViewPresenter implements DetailViewBindingContract.Presenter{

  private DetailViewBindingContract.View view;

  private Context context;

  protected static final String logger = DetailViewPresenter.class.getName();

  public DetailViewPresenter(
      View view, Context context) {
    this.view = view;
    this.context = context;
  }

  @Override
  public void onShowData(ToDoDetailView toDoDetailView) {
      view.showData(toDoDetailView);
  }

  @Override
  public void onClickSaveToDoButton(ToDoDetailView toDoDetailView) {
    Log.i(logger,"Click on Save the Details of ToDo "+toDoDetailView.getId());
    Intent returnIntent = new Intent(context, MainActivity.class);
    returnIntent.putExtra(ARG_ID,toDoDetailView.getId());
    returnIntent.putExtra(ARG_NAME,toDoDetailView.getName());
    returnIntent.putExtra(ARG_BESCHREIBUNG,toDoDetailView.getBeschreibung());
    ((DetailViewActivity)view).setResult(Activity.RESULT_OK,returnIntent);
    ((DetailViewActivity)view).finish();
  }

}
