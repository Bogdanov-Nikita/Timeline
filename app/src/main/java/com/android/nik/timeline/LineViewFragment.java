package com.android.nik.timeline;

import database.TimeLineDatabase;
import database.TimeLineDatabase.Constants;
import model.Settings;
import model.dialog.DialogContext;
import model.graphics.DataContainer;
import model.graphics.GraphicsContext;
import model.search.TimeListItem;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class LineViewFragment extends Fragment implements DataContainer {

	public static  final int DEFAULT_INDEX = Settings.DEFAULT_INDEX;
	private static final String ARG_INDEX = "index";

	int index;
	TimeListItem TimeList;
	GraphicsContext gContext;
	SimpleCursorAdapter Adapter;

    public LineViewFragment() {
	}

	public static LineViewFragment newInstance(int index) {
		LineViewFragment fragment = new LineViewFragment();
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
		
		TimeList = getActivity().getIntent().getParcelableExtra(
				MainActivity.class.getPackage() +
				TimeListItem.class.getSimpleName());
		gContext = getActivity().getIntent().getParcelableExtra(
				MainActivity.class.getPackage() +
				GraphicsContext.class.getSimpleName() +
				String.valueOf(index));
		
		View rootView = inflater.inflate(R.layout.fragment_line_view, container, false);
		GridView gv = (GridView) rootView.findViewById(R.id.LineGrid);

		TimeLineDatabase db = new TimeLineDatabase(getActivity().getApplicationContext()); 
		db.beginTransaction();
		Cursor IdCursor = db.queryFromTimeLists(new int[]{TimeListItem.VISIBLE,TimeListItem.TEMPORARY});
		IdCursor.moveToFirst();
		long id[] = new long [IdCursor.getCount()];
		for(int i=0;!IdCursor.isAfterLast();IdCursor.moveToNext(),i++){
			id[i]=IdCursor.getLong(IdCursor.getColumnIndex(Constants.TimeLists.id));
		}
		IdCursor.close();
		Cursor cursor = db.queryEventByTimeListsMulti(id, false, false, false, false);
		db.setTransactionSuccessful();
		db.endTransaction();

		Adapter = new SimpleCursorAdapter(
				rootView.getContext(),
				R.layout.grid_list_item,
				cursor,
				new String[]{
						Constants.TimeLists.color,
						Constants.Event.title,
						Constants.Event.start_date,
						Constants.Event.body},
				new int[]{
						R.id.GridItemBackGround,
						R.id.GridItemTitle,
						R.id.GridItemDateTime,
						R.id.GridItemAbout},
				SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		Adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				switch (view.getId()) {
					case R.id.GridItemBackGround:
						view.getBackground().setColorFilter(cursor.getInt(columnIndex), PorterDuff.Mode.SRC);
						return true;
					case R.id.GridItemTitle:
						((TextView) view).setText(cursor.getString(columnIndex));
						return true;
					case R.id.GridItemDateTime:
						((TextView) view).setText(
								makeTimeDate(
										cursor.getString(columnIndex),
										cursor.getString(
												cursor.getColumnIndex(Constants.Event.end_date))));
						return true;
					case R.id.GridItemAbout:
						((TextView) view).setText(cursor.getString(columnIndex));
						return true;
				}
				return false;
			}
		});
		gv.setAdapter(Adapter);
		
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
									long id) {
				gContext.getEventSwipeView().setPage(position);
				SelectView(GraphicsContext.SWIPE_EVENT_VIEW);
			}
		});
        gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				Cursor cursor = Adapter.getCursor();
				cursor.moveToPosition(position);
				DialogContext dialogContext = new DialogContext(DialogContext.EVENT,
						cursor.getInt(cursor.getColumnIndex(Constants.TimeLists.id)));
				Intent ActivityIntent = new Intent(getActivity(), DialogActivity.class);
				ActivityIntent.putExtra(
						MainActivity.class.getPackage() + DialogContext.class.getSimpleName(),
						dialogContext);
				startActivity(ActivityIntent);
				return true;
			}
		});
		//cursor.close();
		db.close();
		return rootView;
	}

	String makeTimeDate(String start,String end){
		String str = "";
		if(start!=null){
			str=str + getResources().getString(R.string.a_search_start_date)+":"+start;
			if(end!=null){
				str=str+"\r\n" + getResources().getString(R.string.a_search_end_date)+":"+end;
			}
		}else{
			if(end!=null){
				str=str + getResources().getString(R.string.a_search_end_date)+":"+end;
			}
		}
		return str;
	}

	public void onUpdateData(){
		TimeLineDatabase db = new TimeLineDatabase(getActivity().getApplicationContext());
		db.beginTransaction();
		Cursor IdCursor = db.queryFromTimeLists(new int[]{TimeListItem.VISIBLE,TimeListItem.TEMPORARY});
		IdCursor.moveToFirst();
		long id[] = new long [IdCursor.getCount()];
		for(int i=0;!IdCursor.isAfterLast();IdCursor.moveToNext(),i++){
			id[i]=IdCursor.getLong(IdCursor.getColumnIndex(Constants.TimeLists.id));
		}
		IdCursor.close();
		Cursor cursor = db.queryEventByTimeListsMulti(id, false, false, false, false);
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
				MainActivity.class.getPackage()+
				TimeListItem.class.getSimpleName(), TimeList);
		getActivity().getIntent().putExtra(
				MainActivity.class.getPackage() +
				GraphicsContext.class.getSimpleName() +
				String.valueOf(index),gContext);
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
