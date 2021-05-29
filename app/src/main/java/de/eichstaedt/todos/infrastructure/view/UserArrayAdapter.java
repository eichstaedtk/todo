package de.eichstaedt.todos.infrastructure.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import java.util.stream.Collectors;

public class UserArrayAdapter extends ArrayAdapter<UserSelectionModel> {

  private final List<User> users;

  private final Activity context;

  private final ToDo toDo;

  private UserElementBinding binding;

  private boolean showAll = false;

  protected static final String logger = UserArrayAdapter.class.getName();

  public UserArrayAdapter(@NonNull Activity context,
      List<User> users, ToDo toDo, boolean showAll) {
    super(context, R.layout.user_element);

    this.showAll = showAll;

    if(showAll){
      this.users = users;
    }else {
      this.users = users.stream().filter(u -> toDo.getKontakte().contains(u.getId())).collect(
          Collectors.toList());
    }

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
          user.getEmail(), user.getMobilnummer(),toDo);
      binding.setUser(model);
    }

    return result;
  }

  public void sendSmsMessage(UserSelectionModel user) {
    Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+user.getMobil()));
    smsIntent.putExtra("sms_body", "Hello Master 2022");
    if (smsIntent.resolveActivity(context.getPackageManager()) != null) {
      context.startActivity(smsIntent);
    }
  }

  public void sendEmailMessage(UserSelectionModel user) {

    Intent emailIntent = new Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:"+user.getEmail()));
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello Master 2022");
    if (emailIntent.resolveActivity(context.getPackageManager()) != null) {
      context.startActivity(emailIntent);
    }
  }


  @Override
  public int getCount() {
    return users.size();
  }

  public boolean isShowAll() {
    return showAll;
  }

  public void setShowAll(boolean showAll) {
    this.showAll = showAll;
  }

  public List<User> getUsers() {
    return users;
  }
}