package de.eichstaedt.todos;

import de.eichstaedt.todos.infrastructure.persistence.DataService;

public class Application extends android.app.Application {

  private DataService dataService;

  @Override
  public void onCreate() {
    super.onCreate();
    dataService = DataService.instance(getApplicationContext());
    dataService.checkOfflineState();
  }

  public DataService getDataService() {
    return dataService;
  }
}
