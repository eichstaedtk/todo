package de.eichstaedt.todos;

/**
 * Created by dannyroa on 5/9/15.
 */
public class TestUtils {

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

}
