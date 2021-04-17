package de.eichstaedt.todos;

import android.util.Log;

import android.widget.ListView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataService = new ToDoDataService(ToDoRepository.getInstance(getApplicationContext()));
        dataService.readToDos(this);

        /*
        ToDo einkaufen = new ToDo("Einkaufen","Wocheneinkauf Lebensmittel", LocalDateTime.now());

        Observable<List<ToDo>> toDoDBObservable = ToDoRepository.getInstance(getApplicationContext()).toDoDAO().getAllAsync().toObservable();

        saveToDo(einkaufen);


        toDoDBObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onComplete);
                */


        Log.i(logger,"Application successful started ...");


    }

    @Override
    public void onComplete(List<ToDo> result, String message) {

        TextView start = findViewById(R.id.start);

        Log.i(logger,"Subscribing to new ToDo List from Database ...");

        start.setText(message);

        todoList = findViewById(R.id.todoList);

        ToDoListAdapter adapter = new ToDoListAdapter(this,result);

        todoList.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }
}