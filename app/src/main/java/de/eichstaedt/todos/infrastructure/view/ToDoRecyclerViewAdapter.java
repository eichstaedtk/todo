package de.eichstaedt.todos.infrastructure.view;

import static de.eichstaedt.todos.DetailViewActivity.TODO_BUNDLE;
import static de.eichstaedt.todos.DetailViewActivity.TODO_PARCEL;
import static de.eichstaedt.todos.domain.entities.ToDoSorter.sortByErledigt;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import de.eichstaedt.todos.DetailViewActivity;
import de.eichstaedt.todos.MainActivity;
import de.eichstaedt.todos.R;
import de.eichstaedt.todos.domain.entities.ToDo;
import de.eichstaedt.todos.domain.entities.ToDoSorter;
import de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper;
import de.eichstaedt.todos.infrastructure.persistence.DataService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.parceler.Parcels;

public class ToDoRecyclerViewAdapter extends RecyclerView.Adapter<ToDoRecyclerViewAdapter.ViewHolder> {

  private List<ToDo> values;

  private final DataService dataService;

  public enum Sorting {WICHTIG_DATUM, DATUM_WICHTIG}

  private Sorting sortDecision;

  protected static final String logger = ToDoRecyclerViewAdapter.class.getName();

  public ToDoRecyclerViewAdapter(List<ToDo> values, DataService dataService) {
    this.values = values;
    this.dataService = dataService;
  }

  public List<ToDo> getValues() {
    return values;
  }

  public void setValues(List<ToDo> values) {
    this.values = values;
  }

  public Sorting getSortDecision() {
    return sortDecision;
  }

  public void setSortDecision(
      Sorting sortDecision) {
    this.sortDecision = sortDecision;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(
        parent.getContext());
    View v = inflater.inflate(R.layout.todo_element, parent, false);
    ViewHolder vh = new ViewHolder(v);
    return vh;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    holder.name.setText(values.get(position).getName());

    if(!values.get(position).isErledigt()) {
      holder.name.setPaintFlags(0);
      if(values.get(position).getFaellig() != null) {
        if (values.get(position).getFaellig().isBefore(LocalDateTime.now())) {
          holder.name.setTextColor(ContextCompat.getColor(holder.layout.getContext(), R.color.todoUrgent));
        } else {
          holder.name.setTextColor(ContextCompat.getColor(holder.layout.getContext(), R.color.todoNotUrgent));
        }
      }
    }else {
      holder.name.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }
    holder.name.setOnClickListener((v) -> onItemSelected(values.get(position),(MainActivity) holder.layout.getContext()));

    holder.erledigt.setChecked(values.get(position).isErledigt());
    holder.erledigt.setOnClickListener((v) -> {this.onClick(v,values.get(position));});

    holder.wichtig.setChecked(values.get(position).isWichtig());
    holder.wichtig.setOnClickListener((v) -> {this.onClick(v,values.get(position));});

    if(values.get(position).getFaellig() != null) {
      holder.faellig.setText(values.get(position).getFaellig().format(DateTimeFormatter.ofPattern(
          FirebaseDocumentMapper.DATE_FORMAT)));
    }

  }

  public void onClick(View v, ToDo toDo) {

    if(v.getId() ==  R.id.todoWichtig) {
      toDo.setWichtig(((CheckBox)v).isChecked());
      updateToDo(toDo);
    }

    if(v.getId() ==  R.id.todoErledigt) {
      toDo.setErledigt(((CheckBox)v).isChecked());
      updateToDo(toDo);
    }

  }

  private void updateToDo(ToDo toDo) {
    dataService.updateToDo(toDo, (s)-> {

      if(Sorting.WICHTIG_DATUM.equals(sortDecision))
      {
        ToDoSorter.sortByErledigtAndWichtigDatum(values);
      }

      if(Sorting.DATUM_WICHTIG.equals(sortDecision)){
        ToDoSorter.sortByErledigtAndDatumWichtig(values);
      }

      if(sortDecision == null) {
        sortByErledigt(values);
      }

      this.notifyDataSetChanged();
    });
  }

  protected void onItemSelected(ToDo toDo, MainActivity context) {

    Intent openDetailView = new Intent(context, DetailViewActivity.class);
    Bundle bundle = new Bundle();
    bundle.putParcelable(TODO_PARCEL, Parcels.wrap(toDo));
    openDetailView.putExtra(TODO_BUNDLE, bundle);
    context.startActivityForResult(openDetailView,MainActivity.RETURN_UPDATE_TODO);
  }

  @Override
  public int getItemCount() {
    return values.size();
  }

  public void add(int position, ToDo item) {
    values.add(position, item);
    notifyItemInserted(position);
  }

  public void remove(int position) {
    values.remove(position);
    notifyItemRemoved(position);
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    public View layout;
    public TextView name;
    public CheckBox erledigt;
    public CheckBox wichtig;
    public TextView faellig;

    public ViewHolder(View v) {
      super(v);
      layout = v;
      name =  v.findViewById(R.id.todoNameText);
      erledigt = v.findViewById(R.id.todoErledigt);
      wichtig = v.findViewById(R.id.todoWichtig);
      faellig = v.findViewById(R.id.todoFaelligText);
    }
  }

}
