package de.eichstaedt.todos.infrastructure.view;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import de.eichstaedt.todos.R;
import de.eichstaedt.todos.databinding.UserElementBinding;
import de.eichstaedt.todos.domain.ToDo;
import de.eichstaedt.todos.domain.User;
import java.util.List;

public class UserArrayAdapter extends ArrayAdapter<UserSelectionModel> {

  private final List<User> users;

  private final Activity context;

  private final ToDo toDo;

  private UserElementBinding binding;

  protected static final String logger = UserArrayAdapter.class.getName();

  public UserArrayAdapter(@NonNull Activity context,
      List<User> users, ToDo toDo) {
    super(context, R.layout.user_element);
    this.users = users;
    this.context = context;
    this.toDo = toDo;
    Log.i(logger,"Creating user array adapter ... "+ users.size());
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    View result = convertView;

    if (convertView == null) {
      LayoutInflater inflater = LayoutInflater.from(
          parent.getContext());
      binding = DataBindingUtil.inflate(inflater, R.layout.user_element,parent,false);
      result = binding.getRoot();
      result.setTag(binding);

    } else {
      binding = (UserElementBinding) result.getTag();
    }

    binding.setAdapter(this);

    if(users != null && users.size() > 0) {
      User user = users.get(position);
      UserSelectionModel model = new UserSelectionModel(user.getId(),user.getName(),
          user.getEmail(), toDo.getKontakte().contains(user.getId()));
      binding.setUser(model);
    }

    return result;
  }


  public void onSelectUser() {
    Log.i(logger,"Selecting User: "+binding.userselected.isChecked());
    if(binding.userselected.isChecked()){
      toDo.getKontakte().add(binding.getUser().getId());
    }else {
      toDo.getKontakte().remove(binding.getUser().getId());
    }
  }

  public boolean userIsSelected() {
    return toDo.getKontakte().contains(binding.getUser().getId());
  }

  @Override
  public int getCount() {
    return users.size();
  }
}