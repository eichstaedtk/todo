package de.eichstaedt.todos;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import io.reactivex.schedulers.Schedulers;

import java.time.LocalDateTime;
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

        ToDo einkaufen = new ToDo("Einkaufen","Essen einkaufen", LocalDateTime.now());

        Observable<List<ToDo>> toDoDBObservable = ToDoRepository.getInstance(getApplicationContext()).toDoDAO().getAllAsync().toObservable();

        saveToDo(einkaufen);


        toDoDBObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onComplete);

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

        Log.i(logger,"Subscribing to new ToDo List from Database ...");

        start.setText("Anzahl offener ToDos "+result.size());
    }
}