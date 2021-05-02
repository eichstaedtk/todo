package de.eichstaedt.todos.infrastructure.view;

import android.content.Context;
import de.eichstaedt.todos.infrastructure.view.DetailViewBindingContract.View;

public class DetailViewPresenter implements DetailViewBindingContract.Presenter{

  private DetailViewBindingContract.View view;

  private Context context;

  public DetailViewPresenter(
      View view, Context context) {
    this.view = view;
    this.context = context;
  }

  @Override
  public void onShowData(ToDoDetailView toDoDetailView) {
      view.showData(toDoDetailView);
  }
}
