package com.android.nik.timeline;

import database.TimeLineDatabase;
import database.TimeLineDatabase.Constants;
import model.Settings;
import model.dialog.DialogContext;
import model.graphics.DataContainer;
import model.graphics.GraphicsContext;
import model.search.TimeListItem;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener; 
import android.widget.AdapterView;
import android.widget.GridView;
import android.app.Fragment;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Плитка с таймлайнами.
 * 
 */
public class GridViewFragment extends Fragment implements DataContainer{

    public static  final int DEFAULT_INDEX = Settings.DEFAULT_INDEX;
    private static final String ARG_INDEX = "index";

    int index;
    SimpleCursorAdapter Adapter;
    GraphicsContext gContext;

	public GridViewFragment() {
		// Required empty public constructor
	}

    public static GridViewFragment newInstance(int index) {
        GridViewFragment fragment = new GridViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_INDEX);
        }
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        gContext = getActivity()
                .getIntent()
                .getParcelableExtra(
                        MainActivity.class.getPackage() +
                        GraphicsContext.class.getSimpleName() +
                        String.valueOf(index));
		View rootView = inflater.inflate(R.layout.fragment_grid_view,
				container, false);

		GridView gv = (GridView) rootView.findViewById(R.id.gridView1);

        TimeLineDatabase db = new TimeLineDatabase(getActivity().getApplicationContext());
        db.beginTransaction();
        Cursor cursor;
        cursor = db.queryFromTimeLists(new int[]{TimeListItem.VISIBLE,TimeListItem.TEMPORARY});
        db.setTransactionSuccessful();
        db.endTransaction();
        Adapter = new SimpleCursorAdapter(
                rootView.getContext(),
                R.layout.grid_list_item,cursor,
                new String[]{Constants.TimeLists.color,Constants.TimeLists.title,Constants.TimeLists.about},
                new int[]{R.id.GridItemBackGround,R.id.GridItemTitle,R.id.GridItemAbout},
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        Adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                switch (view.getId()){
                    case R.id.GridItemBackGround:
                        view.getBackground().setColorFilter(cursor.getInt(columnIndex), PorterDuff.Mode.SRC);
                        return true;
                    case R.id.GridItemTitle:
                        ((TextView)view).setText(cursor.getString(columnIndex));
                        return true;
                    case R.id.GridItemAbout:
                        ((TextView)view).setText(cursor.getString(columnIndex));
                        return true;
                }
                return false;
            }
        });
        gv.setAdapter(Adapter);

		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id){
                gContext.getGallerySwipeView().setPage(position);
                SelectView(GraphicsContext.SWIPE_GALLERY_VIEW);
			}
		});

        gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = Adapter.getCursor();
                cursor.moveToPosition(position);
                DialogContext dialogContext = new DialogContext(DialogContext.TIMELINE,
                        cursor.getInt(cursor.getColumnIndex(Constants.TimeLists.id)));
                Intent ActivityIntent = new Intent(getActivity(), DialogActivity.class);
                ActivityIntent.putExtra(
                        MainActivity.class.getPackage()+DialogContext.class.getSimpleName(),
                        dialogContext);
                startActivity(ActivityIntent);
                return true;
            }
        });

        /*if(cursor != null && !cursor.isClosed()){
            cursor.close();
        }*/
		db.close();
		return rootView;
    }

    @Override
    public void onUpdateData() {
        TimeLineDatabase db = new TimeLineDatabase(getActivity().getApplicationContext());
        db.beginTransaction();
        Cursor cursor;
        cursor = db.queryFromTimeLists(new int[]{TimeListItem.VISIBLE,TimeListItem.TEMPORARY});
        db.setTransactionSuccessful();
        db.endTransaction();
        Adapter.swapCursor(cursor).close();
        db.close();
        Adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadingData() {

    }

    @Override
    public void onPause() {
        getActivity().getIntent().putExtra(
                MainActivity.class.getPackage() +
                        GraphicsContext.class.getSimpleName() +
                        String.valueOf(index), gContext);
        super.onPause();
    }

    void SelectView(int position){
        Configuration configuration = getResources().getConfiguration();
        switch (configuration.screenLayout&Configuration.SCREENLAYOUT_SIZE_MASK) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                ViewSmallScreenActivity activity = ((ViewSmallScreenActivity)getActivity());
                activity.ToolbarSpinner.setSelection(position);
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                ViewLargeScreenActivity activity2 = ((ViewLargeScreenActivity)getActivity());
                activity2.WindowSpinner[index].setSelection(position);
                break;
            default:
                break;
        }
    }
}
