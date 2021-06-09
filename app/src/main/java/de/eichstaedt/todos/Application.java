package de.eichstaedt.todos;

import android.util.Log;
import de.eichstaedt.todos.infrastructure.persistence.DataService;

public class Application extends android.app.Application {

  private DataService dataService;

  private static final String logger = Application.class.getName();

  @Override
  public void onCreate() {
    super.onCreate();
    dataService = DataService.instance(getApplicationContext());
    try {
      dataService.checkOfflineState().get();
    } catch (Exception e) {
      Log.e(logger,"Error during check the offline state",e);
    }

  }

  public DataService getDataService() {
    return dataService;
  }
}
