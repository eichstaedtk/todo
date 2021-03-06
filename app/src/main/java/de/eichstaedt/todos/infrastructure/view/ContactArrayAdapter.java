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
import de.eichstaedt.todos.Application;
import de.eichstaedt.todos.R;
import de.eichstaedt.todos.databinding.ContactElementBinding;
import de.eichstaedt.todos.domain.entities.ToDo;
import de.eichstaedt.todos.infrastructure.persistence.DataService;
import java.util.List;

public class ContactArrayAdapter extends ArrayAdapter<ContactModel> {

  private final List<ContactModel> contacts;

  private final Activity context;

  private final ToDo toDo;

  private ContactElementBinding binding;

  private final DataService dataService;

  protected static final String logger = ContactArrayAdapter.class.getName();

  public ContactArrayAdapter(@NonNull Activity context,
      List<ContactModel> contacts, ToDo toDo) {
    super(context, R.layout.contact_element);

    this.contacts = contacts;
    this.context = context;
    this.toDo = toDo;
    this.dataService = ((Application)context.getApplication()).getDataService();
    Log.i(logger,"Creating user array adapter ... "+ contacts.size());
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    View result = convertView;

    if (convertView == null) {
      LayoutInflater inflater = LayoutInflater.from(
          parent.getContext());
      binding = DataBindingUtil.inflate(inflater, R.layout.contact_element,parent,false);
      result = binding.getRoot();
      result.setTag(binding);

    } else {
      binding = (ContactElementBinding) result.getTag();
    }

    binding.setAdapter(this);

    if(contacts != null && contacts.size() > 0) {
      ContactModel user = contacts.get(position);
      ContactModel model = new ContactModel(user.getId(),user.getName(),
          user.getEmail(), user.getMobil(),toDo);
      binding.setContact(model);
    }

    return result;
  }

  public void sendSmsMessage(ContactModel user) {
    Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+user.getMobil()));
    smsIntent.putExtra("sms_body", user.getToDo().getName()+": "+user.getToDo().getBeschreibung());
    if (smsIntent.resolveActivity(context.getPackageManager()) != null) {
      context.startActivity(smsIntent);
    }
  }

  public void sendEmailMessage(ContactModel user) {

    Intent emailIntent = new Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:"+user.getEmail()));
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, user.getToDo().getName());
    emailIntent.putExtra(Intent.EXTRA_TEXT, user.getToDo().getBeschreibung());
    if (emailIntent.resolveActivity(context.getPackageManager()) != null) {
      context.startActivity(emailIntent);
    }
  }

  public void removeContact(ContactModel contact) {
    toDo.getKontakte().remove(contact.getId());
    dataService.updateToDo(toDo,(result)->{
      this.getContacts().removeIf(user -> user.getId().equals(contact.getId()));
      this.notifyDataSetChanged();
      binding.invalidateAll();
    });
  }


  @Override
  public int getCount() {
    return contacts.size();
  }

  public List<ContactModel> getContacts() {
    return contacts;
  }
}