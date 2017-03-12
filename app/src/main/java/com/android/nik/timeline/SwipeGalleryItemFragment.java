package com.android.nik.timeline;

import database.TimeLineDatabase;
import database.TimeLineDatabase.Constants;
import model.dialog.DialogContext;
import model.graphics.GraphicsContext;
import model.search.TimeListItem;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Fragment;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * A simple {@link android.app.Fragment} subclass.
 * 
 */
public class SwipeGalleryItemFragment extends Fragment {
	public static final String PREFIX = MainActivity.class.getPackage()+SwipeGalleryItemFragment.class.getSimpleName();
	public static final String ARG_SECTION_NUMBER = PREFIX+"_section_number";
    private static final String ARG_INDEX = "index";

    int index;
    int section_number;
    SimpleCursorAdapter Adapter;
    GraphicsContext gContext;

    public SwipeGalleryItemFragment() {
        // Required empty public constructor
    }

	public static SwipeGalleryItemFragment newInstance(int position, int index){
		SwipeGalleryItemFragment fragment = new SwipeGalleryItemFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER,position);
        args.putInt(ARG_INDEX,index);
		fragment.setArguments(args);
		return fragment;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            section_number = getArguments().getInt(ARG_SECTION_NUMBER);
            index = getArguments().getInt(ARG_INDEX);
        }
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        gContext = getActivity().getIntent().getParcelableExtra(
                MainActivity.class.getPackage() +
                GraphicsContext.class.getSimpleName() +
                String.valueOf(index));

        // Inflate the layout for this fragment
		View rootView =  inflater.inflate(R.layout.fragment_swipe_gallery_item,container, false);
		ListView List = (ListView) rootView.findViewById(R.id.GalleryList);

		TimeLineDatabase db = new TimeLineDatabase(getActivity().getApplicationContext());
		Cursor cursor;
		cursor = db.queryFromTimeLists(new int[] {TimeListItem.VISIBLE,TimeListItem.TEMPORARY});
		cursor.moveToPosition(section_number - 1);
		int id = cursor.getInt(cursor.getColumnIndex(Constants.TimeLists.id));
		int color = cursor.getInt(cursor.getColumnIndex(Constants.TimeLists.color));
		cursor = db.queryEventByTimeLists(id, false, false, false, false);

        Adapter = new SimpleCursorAdapter(
                rootView.getContext(),
                R.layout.gallery_list_item,
                cursor,
                new String[]{Constants.Event.title,Constants.Event.start_date,Constants.Event.body},
                new int[]{R.id.GalleryEventTitle,R.id.GalleryEventDateTime,R.id.GalleryEventBody},
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        Adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                switch (view.getId()){
                    case R.id.GalleryEventTitle:
                        ((TextView)view).setText(cursor.getString(columnIndex));
                        return true;
                    case R.id.GalleryEventDateTime:
                        ((TextView)view).setText(
                            makeTimeDate(
                            cursor.getString(columnIndex),
                            cursor.getString(
                            cursor.getColumnIndex(Constants.Event.end_date))));
                        return true;
                    case R.id.GalleryEventBody:
                        ((TextView)view).setText(cursor.getString(columnIndex));
                        return true;
                }
                return false;
            }
        });

        List.setAdapter(Adapter);
		List.setBackgroundColor(color);
		List.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
                TimeLineDatabase db = new TimeLineDatabase(getActivity().getApplicationContext());
                Cursor IdCursor = db.queryFromTimeLists(new int[]{TimeListItem.VISIBLE,TimeListItem.TEMPORARY});
                IdCursor.moveToFirst();
                long Id[] = new long [IdCursor.getCount()];
                for(int i=0;!IdCursor.isAfterLast();IdCursor.moveToNext(),i++){
                    Id[i] = IdCursor.getLong(IdCursor.getColumnIndex(Constants.TimeLists.id));
                }
                IdCursor.close();
                Cursor cursorEvent = db.queryEventByTimeListsMulti(Id, false, false, false, false);
                cursorEvent.moveToFirst();

                int CursorPosition = 0;
                Cursor c = Adapter.getCursor();
                c.moveToPosition(position);
                int ID = c.getInt(c.getColumnIndex(Constants.Event.id));
                for(int i=0;!cursorEvent.isAfterLast();cursorEvent.moveToNext(),i++){
                    if(ID == cursorEvent.getInt(cursorEvent.getColumnIndex(Constants.TimeLists.id))){
                        CursorPosition = cursorEvent.getPosition();
                        break;
                    }
                }
                db.close();
                gContext.getEventSwipeView().setPage(CursorPosition);
                SelectView(GraphicsContext.SWIPE_EVENT_VIEW);
			}
		});
        List.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = Adapter.getCursor();
                cursor.moveToPosition(position);
                DialogContext dialogContext = new DialogContext(DialogContext.EVENT,
                        cursor.getInt(cursor.getColumnIndex(Constants.Event.id)));
                Intent ActivityIntent = new Intent(getActivity(), DialogActivity.class);
                ActivityIntent.putExtra(
                        MainActivity.class.getPackage()+DialogContext.class.getSimpleName(),
                        dialogContext);
                startActivity(ActivityIntent);
                return true;
            }
        });
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
