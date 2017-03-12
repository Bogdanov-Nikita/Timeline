package model.graphics;


/**
 * Implementation event for updating data.
 * Used in:
 * {@link  com.android.nik.timeline.LineViewFragment}
 * {@link com.android.nik.timeline.GridViewFragment}
 * {@link com.android.nik.timeline.SwipeListFragment}
 */
public interface DataContainer {
    void onUpdateData();
    void onLoadingData();
}
