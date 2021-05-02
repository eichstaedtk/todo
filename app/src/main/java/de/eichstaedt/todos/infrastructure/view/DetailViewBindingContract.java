package de.eichstaedt.todos.infrastructure.view;

public interface DetailViewBindingContract {

  interface Presenter {
    void onShowData(ToDoDetailView toDoDetailView);
    void onClickSaveToDoButton(ToDoDetailView toDoDetailView);
  }

  interface View {
    void showData(ToDoDetailView toDoDetailView);
  }
}
