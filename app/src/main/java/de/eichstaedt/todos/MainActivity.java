package de.eichstaedt.todos;

import android.content.Intent;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import de.eichstaedt.todos.infrastructure.persistence.ToDoDataService;
import de.eichstaedt.todos.infrastructure.view.ToDoListAdapter;

import java.util.List;
import de.eichstaedt.todos.domain.ToDo;
import de.eichstaedt.todos.infrastructure.persistence.RepositoryCallback;
import de.eichstaedt.todos.infrastructure.persistence.ToDoRepository;

public class MainActivity extends AppCompatActivity implements RepositoryCallback<List<ToDo>> {

    protected static final String logger = MainActivity.class.getName();

    private ListView todoList;

    private ToDoDataService dataService;

    private ToDoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataService = new ToDoDataService(ToDoRepository.getInstance(getApplicationContext()));
        dataService.readToDos(this);

        Log.i(logger,"Application successful started ...");


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

        adapter = new ToDoListAdapter(this,result);

        todoList.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }
}