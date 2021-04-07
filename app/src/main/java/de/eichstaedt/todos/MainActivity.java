package de.eichstaedt.todos;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import de.eichstaedt.todos.domain.ToDo;
import de.eichstaedt.todos.infrastructure.persistence.RepositoryCallback;
import de.eichstaedt.todos.infrastructure.persistence.ToDoRepository;

public class MainActivity extends AppCompatActivity implements RepositoryCallback<List<ToDo>> {

    protected static final String logger = MainActivity.class.getName();

    ExecutorService executorService = Executors.newFixedThreadPool(4);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToDo einkaufen = new ToDo("Einkaufen","Essen einkaufen", new Date());

        saveToDo(einkaufen);

        loadAllToDos(this);

        Log.i(logger,"Application successful started ...");
    }

    private void saveToDo(final ToDo toDo) {

        executorService.execute(() -> ToDoRepository.getInstance(getApplicationContext()).toDoDAO().insert(toDo));
    }

    private void loadAllToDos(final RepositoryCallback<List<ToDo>> callback) {
        executorService.execute(() -> callback.onComplete(ToDoRepository.getInstance(getApplicationContext()).toDoDAO().getAll()));
    }


    @Override
    public void onComplete(List<ToDo> result) {

        TextView start = findViewById(R.id.start);

        start.setText("Anzahl offener ToDos "+result.size());
    }
}