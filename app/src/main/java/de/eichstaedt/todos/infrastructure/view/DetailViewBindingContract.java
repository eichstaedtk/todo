package de.eichstaedt.todos.infrastructure.view;

public interface DetailViewBindingContract {

  interface Presenter {
    void onClickSaveToDoButton(ToDoDetailView toDoDetailView);
  }
}
