package de.eichstaedt.todos.infrastructure.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import de.eichstaedt.todos.DetailViewActivity;
import de.eichstaedt.todos.MainActivity;
import de.eichstaedt.todos.R;
import de.eichstaedt.todos.domain.ToDo;
import de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper;
import de.eichstaedt.todos.infrastructure.persistence.ToDoDataService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ToDoListAdapter extends ArrayAdapter<String> {

  protected static final String logger = ToDoListAdapter.class.getName();

  private List<ToDo> toDoList;

  private ToDoDataService dataService;

  public ToDoListAdapter(@NonNull Context context, List<ToDo> toDos,
      ToDoDataService dataService) {
    super(context, toDos.size());
    this.toDoList = toDos;
    this.dataService = dataService;
  }

  @Override
  public int getCount() {
    return this.toDoList.size();
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

    Log.i(logger,"Creating new view for todo ... "+toDoList.get(position));

    if(convertView == null) {
      LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
      convertView = inflater.inflate(R.layout.todo_element,null);
    }

    TextView name = convertView.findViewById(R.id.todoNameText);
    name.setText(toDoList.get(position).getName());

    if(!toDoList.get(position).isErledigt()) {
      if (toDoList.get(position).getFaellig().isBefore(LocalDateTime.now())) {
        name.setTextColor(ContextCompat.getColor(getContext(), R.color.todoUrgent));
      } else {
        name.setTextColor(ContextCompat.getColor(getContext(), R.color.todoNotUrgent));
      }
    }else {
      name.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }
    name.setOnClickListener((v) -> onItemSelected(toDoList.get(position),(MainActivity) getContext()));

    CheckBox erledigt = convertView.findViewById(R.id.todoErledigt);
    erledigt.setChecked(toDoList.get(position).isErledigt());
    erledigt.setOnClickListener((v) -> {this.onClick(v,toDoList.get(position));});

    CheckBox wichtig = convertView.findViewById(R.id.todoWichtig);
    wichtig.setChecked(toDoList.get(position).isWichtig());
    wichtig.setOnClickListener((v) -> {this.onClick(v,toDoList.get(position));});

    TextView faellig = convertView.findViewById(R.id.todoFaelligText);
    faellig.setText(toDoList.get(position).getFaellig().format(DateTimeFormatter.ofPattern(
        FirebaseDocumentMapper.DATE_FORMAT)));

    return convertView;
  }

  protected void onItemSelected(ToDo toDo, MainActivity context) {

    Intent openDetailView = new Intent(context,DetailViewActivity.class);
    openDetailView.putExtra(DetailViewActivity.ARG_NAME,toDo.getName());
    openDetailView.putExtra(DetailViewActivity.ARG_BESCHREIBUNG,toDo.getBeschreibung());

    context.startActivityForResult(openDetailView,MainActivity.RETURN_SAVE_TODO);
  }


  public void onClick(View v, ToDo toDo) {

    if(v.getId() ==  R.id.todoWichtig) {
      toDo.setWichtig(((CheckBox)v).isChecked());
      dataService.updateToDo(toDo, (s)-> this.notifyDataSetChanged());
    }

    if(v.getId() ==  R.id.todoErledigt) {
      toDo.setErledigt(((CheckBox)v).isChecked());
      dataService.updateToDo(toDo, (s)-> this.notifyDataSetChanged());
    }

  }
}
