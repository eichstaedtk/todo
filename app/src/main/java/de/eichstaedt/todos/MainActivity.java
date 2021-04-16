package de.eichstaedt.todos;

import android.util.Log;

import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import de.eichstaedt.todos.infrastructure.view.ToDoListAdapter;
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
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements RepositoryCallback<List<ToDo>> {

    protected static final String logger = MainActivity.class.getName();

    private ListView todoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestore
            firestore = FirebaseFirestore.getInstance();

        firestore.collection("todos")
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.i(logger,"Got Successful Documents from Google Firebase"+task.getResult().size());
                    MainActivity.this.onComplete(task.getResult().getDocuments().stream().map(d -> new ToDo(d.getId(),d.getString("name"),d.getString("beschreibung"),LocalDateTime.now())).collect(
                        Collectors.toList()));
                } else {
                    Log.e(logger,"Error Loading Data from Google Firebase ",task.getException());
                }
            });

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

    private void saveToDo(final ToDo toDo) {
        ToDoRepository.getInstance(getApplicationContext()).toDoDAO().insertAsync(toDo);
    }


    @Override
    public void onComplete(List<ToDo> result) {

        TextView start = findViewById(R.id.start);

        Log.i(logger,"Subscribing to new ToDo List from Database ...");

        start.setText("Anzahl offener ToDos "+result.size());

        todoList = findViewById(R.id.todoList);

        ToDoListAdapter adapter = new ToDoListAdapter(this,result);

        todoList.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }
}