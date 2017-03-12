package com.android.nik.timeline;


import database.TimeLineDatabase;
import database.TimeLineDatabase.Constants;
import model.dialog.DialogContext;
import model.search.SearchContext;
import model.search.SimpleSearch;
import model.search.TimeListItem;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.SimpleCursorAdapter.ViewBinder;




public class OpenDeleteFragment extends Fragment{

	public static final String PREFIX = MainActivity.class.getPackage()+OpenDeleteFragment.class.getSimpleName();
	public static final String TYPE_OF_FRAGMENT = PREFIX+"_type_of_fragment";
	public static final int OPEN = 1;
	public static final int DELETE = 2;
    public static final int RADIUS = 64;//радиус фигуры для списка.

	int Mode;
	Cursor cursor = null;
    SimpleCursorAdapter Adapter = null;

	public OpenDeleteFragment() {
	}
	static OpenDeleteFragment newInstanseState (int Mode){
		OpenDeleteFragment OpenSaveFragment = new OpenDeleteFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE_OF_FRAGMENT, Mode);
        OpenSaveFragment.setArguments(args);
        OpenSaveFragment.Mode = Mode ;
        return OpenSaveFragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Mode = getArguments().getInt(TYPE_OF_FRAGMENT);
		View rootView  = inflater.inflate(R.layout.fragment_open_delete, container,false);
		ListView List = (ListView) rootView.findViewById(R.id.OpenDeleteList);
		TimeLineDatabase db = new TimeLineDatabase(getActivity().getApplicationContext());
		
		switch (Mode) {
		case OPEN:
			cursor = db.queryFromTimeLists (new int []{TimeListItem.NOT_VISIBLE});
			break;
		case DELETE:
			cursor = db.queryFromTimeLists(new int[]{
                    TimeListItem.NOT_VISIBLE,
                    TimeListItem.VISIBLE,
                    TimeListItem.TEMPORARY});
			break;
		}
		Adapter = new SimpleCursorAdapter(
				getActivity().getApplicationContext(), 
    			R.layout.fragment_open_delete_item,
    			cursor, 
    			new String[]{Constants.TimeLists.title,Constants.TimeLists.about,Constants.TimeLists.color},
    			new int[]{R.id.title,R.id.about,R.id.color},
    			SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		Adapter.setViewBinder(new ViewBinder() {
			
			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {

                switch (view.getId()) {
				case R.id.title:
					((TextView)view).setText(cursor.getString(columnIndex)); return true;
				case R.id.about:
					((TextView)view).setText(cursor.getString(columnIndex)); return true;
				case R.id.color:
				    //возможно в дальнейшем заменить круг на другую фигуру или иконку логотипа,
				    //но покрашенную. дя каждого элемента.
                    ShapeDrawable ItemColorShape = new ShapeDrawable(new OvalShape());
                    ItemColorShape.setIntrinsicWidth(RADIUS);
                    ItemColorShape.setIntrinsicHeight(RADIUS);
                    ItemColorShape.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
                    ItemColorShape.getPaint().setColor(cursor.getInt(columnIndex));
                    ((ImageView)view).setImageDrawable(ItemColorShape);
					return true ;
                case R.layout.fragment_open_delete_item:
				    return true;
                }
				return false;
			}
		});
		List.setAdapter(Adapter);
		List.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
                // действия на открытие или удаление элемента из TimeList.
                TimeLineDatabase db = new TimeLineDatabase(getActivity().getApplicationContext());
				switch (Mode) {
				case OPEN:
					//открыть выбранный  TimeLists
					cursor.moveToPosition(position);
                    db.beginTransaction();
                    try {
                        db.updatetoTimeListsVisable(cursor.getInt(cursor.getColumnIndex(Constants.TimeLists.id)), true);
                        db.setTransactionSuccessful();
                    }finally {
                        db.endTransaction();
                    }
                    Adapter.swapCursor(
                            cursor = db.queryFromTimeLists(new int []{TimeListItem.NOT_VISIBLE}))
                            .close();
                    Adapter.notifyDataSetChanged();
					((MainActivity)getActivity()).LaunchScreenActivity(new SearchContext(new SimpleSearch("", 0, -1)));
					break;
				case DELETE:
					//удалить load from SQL Saves positions.
                    cursor.moveToPosition(position);
                    db.beginTransaction();
                    try {
                        db.deleteFromTimeLists(cursor.getInt(cursor.getColumnIndex(Constants.TimeLists.id)));
                        db.setTransactionSuccessful();
                    }finally {
                        db.endTransaction();
                    }
                    Adapter.swapCursor(
                        cursor = db.queryFromTimeLists(new int[]{
                                TimeListItem.NOT_VISIBLE,
                                TimeListItem.VISIBLE,
                                TimeListItem.TEMPORARY}))
                        .close();
                    Adapter.notifyDataSetChanged();
					break;
				}
                db.close();
			}
		});
		List.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent,
					View view, int position, long id) {
                // действие на редактирование записи
                cursor.moveToPosition(position);
                int IdPosition = cursor.getInt(cursor.getColumnIndex(Constants.TimeLists.id));
                DialogContext dialogContext;
                Intent ActivityIntent = new Intent(getActivity(), DialogActivity.class);
                dialogContext = new DialogContext(DialogContext.TIMELINE,IdPosition);
                ActivityIntent.putExtra(
                        MainActivity.class.getPackage()+DialogContext.class.getSimpleName(),
                        dialogContext);
                startActivity(ActivityIntent);
				return true;
			}
		});
		return rootView;
	}

    @Override
    public void onResume() {
        TimeLineDatabase db = new TimeLineDatabase(getActivity().getApplicationContext());
        Mode = getArguments().getInt(TYPE_OF_FRAGMENT);
        switch (Mode){
            case OPEN:
                cursor = db.queryFromTimeLists(new int []{TimeListItem.NOT_VISIBLE});
                break;
            case DELETE:
                cursor = db.queryFromTimeLists(new int[]{
                        TimeListItem.NOT_VISIBLE,
                        TimeListItem.VISIBLE,
                        TimeListItem.TEMPORARY});
                break;
        }
        Adapter.swapCursor(cursor).close();
        db.close();
        Adapter.notifyDataSetChanged();
        super.onResume();
    }
}
