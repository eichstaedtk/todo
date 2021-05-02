package de.eichstaedt.todos;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import de.eichstaedt.todos.infrastructure.persistence.ToDoDataService;
import de.eichstaedt.todos.infrastructure.view.ToDoListAdapter;

import java.time.LocalDateTime;
import java.util.List;
import de.eichstaedt.todos.domain.ToDo;
import de.eichstaedt.todos.infrastructure.persistence.RepositoryCallback;
import de.eichstaedt.todos.infrastructure.persistence.ToDoRepository;

public class MainActivity extends AppCompatActivity implements RepositoryCallback<List<ToDo>>, ReloadViewCallback {

    protected static final String logger = MainActivity.class.getName();

    private ListView todoList;

    private ToDoDataService dataService;

    private ToDoListAdapter adapter;

    private FloatingActionButton addNewToDoButton;

    public static final int RETURN_SAVE_TODO = 42;

    public static final int RETURN_UPDATE_TODO = 43;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataService = new ToDoDataService(ToDoRepository.getInstance(getApplicationContext()));
        dataService.readToDos(this);

        this.addNewToDoButton = findViewById(R.id.addNewToDoButton);
        this.addNewToDoButton.setOnClickListener((view) -> onClickAddToDoButton());

        Log.i(logger,"Application successful started ...");
    }

    private void onClickAddToDoButton() {
        Intent openDetailView = new Intent(this, DetailViewActivity.class);
        this.startActivityForResult(openDetailView, MainActivity.RETURN_SAVE_TODO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(logger,"On Activity Result "+requestCode+" "+resultCode);

        if(requestCode == RETURN_SAVE_TODO)
        {
            Log.i(logger,"Return to save to Do");

            if(resultCode == Activity.RESULT_OK) {
                String todoName = data.getStringExtra(DetailViewActivity.ARG_NAME);
                String todoBeschreibung = data.getStringExtra(DetailViewActivity.ARG_BESCHREIBUNG);

                ToDo toDo = new ToDo(todoName,todoBeschreibung, LocalDateTime.now().plusDays(7),false);

                dataService.saveToDo(toDo,this);
            }
        }

        if(requestCode == RETURN_UPDATE_TODO) {
            Log.i(logger,"Return to update to Do");

            if(resultCode == Activity.RESULT_OK) {
                String todoid = data.getStringExtra(DetailViewActivity.ARG_ID);
                String todoName = data.getStringExtra(DetailViewActivity.ARG_NAME);
                String todoBeschreibung = data.getStringExtra(DetailViewActivity.ARG_BESCHREIBUNG);

                ToDo toDo = new ToDo(todoid,todoName,todoBeschreibung);

                dataService.updateToDo(toDo,this);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_options,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.lokaleDelete : dataService.deleteAllLokalToDos(this);break;
            case R.id.remoteDelete : dataService.deleteAllFirebaseToDos();break;
            case R.id.load: dataService.readToDos(this);break;
        }

        return false;
    }

    @Override
    public void onComplete(List<ToDo> result, String message) {

        TextView start = findViewById(R.id.start);

        Log.i(logger,"Subscribing to new ToDo List from Database ...");

        start.setText(message);

        todoList = findViewById(R.id.todoList);

        if(todoList.getHeaderViewsCount() == 0) {
            TextView name = new TextView(this);
            name.setText("Aktuelle Aufgaben");
            todoList.addHeaderView(name);
        }

        adapter = new ToDoListAdapter(this,result, dataService);

        todoList.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onComplete(String message) {

        TextView start = findViewById(R.id.start);

        Log.i(logger,"Save ToDos finished");

        start.setText(message);

        dataService.readToDos(this);
    }
}