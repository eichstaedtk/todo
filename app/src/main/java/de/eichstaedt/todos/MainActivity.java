package de.eichstaedt.todos;

import static de.eichstaedt.todos.DetailViewActivity.TODO_BUNDLE;
import static de.eichstaedt.todos.DetailViewActivity.TODO_PARCEL;
import static de.eichstaedt.todos.R.id.*;
import static de.eichstaedt.todos.domain.ToDoSorter.sortByErledigtAndDatumWichtig;
import static de.eichstaedt.todos.domain.ToDoSorter.sortByErledigtAndWichtigDatum;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.idling.CountingIdlingResource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import de.eichstaedt.todos.domain.User;
import de.eichstaedt.todos.infrastructure.persistence.DataService;
import de.eichstaedt.todos.infrastructure.persistence.UserRepositoryCallback;
import de.eichstaedt.todos.infrastructure.view.ToDoRecyclerViewAdapter;
import de.eichstaedt.todos.infrastructure.view.ToDoRecyclerViewAdapter.Sorting;
import java.util.List;
import de.eichstaedt.todos.domain.ToDo;
import de.eichstaedt.todos.infrastructure.persistence.RepositoryCallback;
import java.util.Optional;
import org.parceler.Parcels;

public class MainActivity extends AppCompatActivity implements RepositoryCallback<List<ToDo>>,
    UserRepositoryCallback, ReloadViewCallback {

    protected static final String logger = MainActivity.class.getName();

    private RecyclerView todoList;

    private DataService dataService;

    private ToDoRecyclerViewAdapter adapter;

    private GridLayoutManager gridLayoutManager;

    private FloatingActionButton addNewToDoButton;

    public static final int RETURN_SAVE_TODO = 42;

    public static final int RETURN_UPDATE_TODO = 43;

    public static final String RETURN_ACTION = "ACTION";

    public static final String RETURN_ACTION_SAVE = "SAVE";

    public static final String RETURN_ACTION_DELETE = "DELETE";

    private MenuItem sortByWichtigAndDatum;

    private MenuItem sortByDatumAndWichtig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataService = ((Application)this.getApplication()).getDataService();
        dataService.readToDos(this);

        this.addNewToDoButton = findViewById(R.id.addNewToDoButton);
        this.addNewToDoButton.setOnClickListener((view) -> onClickAddToDoButton());

        Log.i(logger,"Application successful started ...");
    }

    private void onClickAddToDoButton() {
        Intent openDetailView = new Intent(this, DetailViewActivity.class);
        ToDo newToDo = new ToDo();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TODO_PARCEL, Parcels.wrap(newToDo));
        openDetailView.putExtra(TODO_BUNDLE, bundle);
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

                Bundle b = data.getBundleExtra(TODO_BUNDLE);
                ToDo toDo = Parcels.unwrap(b.getParcelable(TODO_PARCEL));
                dataService.saveToDo(toDo, this);
            }
        }

        if(requestCode == RETURN_UPDATE_TODO) {
            Log.i(logger,"Return to update to Do");

            if (resultCode == Activity.RESULT_OK) {

                Bundle b = data.getBundleExtra(TODO_BUNDLE);
                Log.i(logger,"Return with Action: "+b.getString(RETURN_ACTION));

                if(RETURN_ACTION_SAVE.equals(b.getString(RETURN_ACTION))) {

                    ToDo toDo = Parcels.unwrap(b.getParcelable(TODO_PARCEL));
                    Log.i(logger, "Update Todo with dataservice: " + toDo.getName());
                    dataService.updateToDo(toDo, this);
                }

                if(RETURN_ACTION_DELETE.equals(b.getString(RETURN_ACTION)))
                {
                     ToDo toDo = Parcels.unwrap(b.getParcelable(TODO_PARCEL));
                     Log.i(logger,"Delete Todo with dataservice: "+toDo.getName());
                     dataService.deleteToDo(toDo,this);
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_options,menu);
        sortByWichtigAndDatum = menu.findItem(sortWichtigDatum);
        sortByDatumAndWichtig = menu.findItem(sortDatumWichtig);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case lokaleDelete : dataService.deleteAllLokalToDos(this);break;
            case remoteDelete : dataService.deleteAllFirebaseToDos();break;
            case load: dataService.readToDos(this);break;
            case sortWichtigDatum: sortByErledigtAndWichtigDatum(adapter.getValues());
                                    sortByWichtigAndDatum.setIcon(R.drawable.outline_done_black_18);
                                    sortByDatumAndWichtig.setIcon(null);
                                    adapter.setSortDecision(Sorting.WICHTIG_DATUM);
                                    adapter.notifyDataSetChanged();
                                    break;
            case sortDatumWichtig: sortByErledigtAndDatumWichtig(adapter.getValues());
                                    sortByWichtigAndDatum.setIcon(null);
                                    sortByDatumAndWichtig.setIcon(R.drawable.outline_done_black_18);
                                    adapter.notifyDataSetChanged();
                                    adapter.setSortDecision(Sorting.DATUM_WICHTIG);
                break;
        }

        return false;
    }

    @Override
    public void onComplete(List<ToDo> result, String message) {

        TextView headline = findViewById(R.id.headline);

        Log.i(logger,"Subscribing to new ToDo List from Database ...");

        headline.setText(message);

        todoList = findViewById(R.id.todoList);

        gridLayoutManager = new GridLayoutManager(this, result.size() == 0 ? 1 : result.size(), GridLayoutManager.HORIZONTAL, false);
        todoList.setLayoutManager(gridLayoutManager);
        sortByErledigtAndWichtigDatum(result);
        adapter = new ToDoRecyclerViewAdapter(result, dataService);
        adapter.setSortDecision(Sorting.WICHTIG_DATUM);
        sortByWichtigAndDatum.setIcon(R.drawable.outline_done_black_18);
        todoList.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onComplete(String message) {

        TextView headline = findViewById(R.id.headline);

        Log.i(logger,"Save ToDos finished");

        headline.setText(message);

        dataService.readToDos(this);
    }

    @Override
    public void onComplete(Optional<User> user) {

        Log.i(logger,"Callback User Repository "+user.isPresent());
        if(!user.isPresent()) {
            User konrad = new User("Konrad", "konrad.eichstaedt@gmx.de", 123456);
            User nicole = new User("Nicole", "nicole.eichstaedt@gmx.de", 123456);
            dataService.insertUserInFirebase(konrad);
            dataService.insertUserInFirebase(nicole);
        }

    }
}