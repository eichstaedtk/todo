package de.eichstaedt.todos.infrastructure.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class DataServiceTest {

  @Test
  public void testCreationAsSingleton() {

    Context context = Mockito.mock(Context.class);
    FirebaseFirestore firestore = Mockito.mock(FirebaseFirestore.class);

    DataService dataService = DataService.instance(context, firestore);

    assertNotNull(dataService);

    assertEquals(dataService,DataService.instance(context,firestore));
  }
}
