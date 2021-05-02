package de.eichstaedt.todos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import de.eichstaedt.todos.databinding.ActivityDetailviewBinding;
import de.eichstaedt.todos.infrastructure.view.DetailViewBindingContract;
import de.eichstaedt.todos.infrastructure.view.DetailViewPresenter;
import de.eichstaedt.todos.infrastructure.view.ToDoDetailView;
import java.util.Date;

public class DetailViewActivity extends AppCompatActivity implements DetailViewBindingContract.View {

  public static final String ARG_ID = "id";

  public static final String ARG_NAME = "name";

  public static final String ARG_BESCHREIBUNG = "beschreibung";

  private FloatingActionButton saveToDoButton;

  private ToDoDetailView toDoDetailView;

  protected static final String logger = DetailViewActivity.class.getName();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ActivityDetailviewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detailview);
    DetailViewPresenter presenter = new DetailViewPresenter(this,getApplicationContext());
    toDoDetailView = new ToDoDetailView(getIntent().getStringExtra(ARG_ID),getIntent().getStringExtra(ARG_NAME),getIntent().getStringExtra(ARG_BESCHREIBUNG),false,false,new Date());
    binding.setTodo(toDoDetailView);
    binding.setPresenter(presenter);

  }


  @Override
  public void showData(ToDoDetailView toDoDetailView) {
    Log.i(logger,"Show Data on Detail View Activity");
  }
}
