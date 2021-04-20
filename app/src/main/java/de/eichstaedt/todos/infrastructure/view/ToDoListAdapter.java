package de.eichstaedt.todos.infrastructure.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import de.eichstaedt.todos.R;
import de.eichstaedt.todos.domain.ToDo;
import de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ToDoListAdapter extends ArrayAdapter<String> {

  private List<ToDo> toDoList;

  public ToDoListAdapter(@NonNull Context context, List<ToDo> toDos) {
    super(context, toDos.size());
    this.toDoList = toDos;
  }

  @Override
  public int getCount() {
    return this.toDoList.size();
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

    if(convertView == null) {
      LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
      convertView = inflater.inflate(R.layout.todo_element,null);
    }

    TextView name = convertView.findViewById(R.id.todoNameText);
    name.setText(toDoList.get(position).getName());

    TextView beschreibung = convertView.findViewById(R.id.todoBeschreibungText);
    beschreibung.setText(toDoList.get(position).getBeschreibung());

    TextView faellig = convertView.findViewById(R.id.todoFaelligText);
    faellig.setText(toDoList.get(position).getFaellig().format(DateTimeFormatter.ofPattern(
        FirebaseDocumentMapper.DATE_FORMAT)));

    return convertView;
  }
}
