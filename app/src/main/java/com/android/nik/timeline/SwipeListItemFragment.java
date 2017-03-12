package com.android.nik.timeline;

import database.TimeLineDatabase;
import database.TimeLineDatabase.Constants;
import model.graphics.GraphicsContext;

import android.os.Bundle;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * A simple {@link android.app.Fragment} subclass.
 * 
 */
public class SwipeListItemFragment extends Fragment {

	public static final String PREFIX = MainActivity.class.getPackage()+SwipeListItemFragment.class.getSimpleName();
	public static final String ARG_SECTION_NUMBER = PREFIX+"_section_number";
	public static final String LIST_STATE = PREFIX +"_list";
	private static final String ARG_INDEX = "index";

	int index;
	int section_number;
	GraphicsContext gContext;
	
	ListView List;

	public SwipeListItemFragment() {
		// Required empty public constructor
	}

	public static SwipeListItemFragment newInstance(int position, int index){
		SwipeListItemFragment fragment = new SwipeListItemFragment();
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
		View rootView = inflater.inflate(R.layout.fragment_swipe_list_item, container,false);
        List = (ListView) rootView.findViewById(R.id.GalleryList2);
        if(savedInstanceState != null){
            Parcelable array;
            if( (array = getArguments().getParcelable(LIST_STATE)) != null){
                List.onRestoreInstanceState(array);
            }
		}
		TimeLineDatabase db = new TimeLineDatabase(getActivity().getApplicationContext());
		Cursor cursor;
		cursor = db.query(Constants.Event.TABLE_NAME,new String[]{
				Constants.Event.id,
				Constants.Event.geo_id,
				Constants.Event.body,
				Constants.Event.title,								
				Constants.Event.start_date,
				Constants.Event.end_date,
				Constants.Event.author_id,
				Constants.Event.category_id 
				}, 
				null, null, null, null, null);
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(
				getActivity().getApplicationContext(), 
				R.layout.gallery_list_item, 
				cursor, 
				new String[] {
					Constants.Event.title,
					Constants.Event.body
				}, 
				new int[]{
					R.id.GalleryEventTitle,
					R.id.GalleryEventBody
				},SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		List.setAdapter(adapter);
		//добавим потом когда будет готова логика
		/*List.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
                // нажатие на эту кнопку должно переносить в соответствующий задел на "Swipe View" с позицей
				// вычисление соответстыующей позиции курсора поочерёдно проходя все позиции
				//float scrollY = ( List.getFirstVisiblePosition()  * List.getChildAt(0).getHeight()) + (- List.getChildAt(0).getY());
				//Log.i("scroll","scroll:  "+scrollY);
				//Log.i("scroll","size:  "+gContext.getListSwipeView().size());
				//Log.i("Gallery View", "On Item Click position="+position);
				
				//Toast.makeText(getActivity().getApplicationContext() , "On Item Click position="+position, Toast.LENGTH_SHORT).show();
			}
		});*/
		
		List.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				float scrollY = ( List.getFirstVisiblePosition()  * List.getChildAt(0).getHeight()) + (- List.getChildAt(0).getY());
				Log.i("scroll","scroll: "+scrollY + " position: " + section_number);
				
				gContext.getListSwipeView().setScrollPosition(section_number - 1 , (int)scrollY);
				getActivity().getIntent().putExtra(
						MainActivity.class.getPackage() +
						GraphicsContext.class.getSimpleName() +
						String.valueOf(index),gContext);
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
		return rootView;
	}
	

	@Override
	public void onPause() {
		getArguments().putParcelable(LIST_STATE , List.onSaveInstanceState());
		float scrollY = ( List.getFirstVisiblePosition()  * List.getChildAt(0).getHeight()) + (- List.getChildAt(0).getY());
		gContext.getListSwipeView().setScrollPosition(section_number - 1 , (int)scrollY);
		getActivity().getIntent().putExtra(
				MainActivity.class.getPackage() +
				GraphicsContext.class.getSimpleName() +
				String.valueOf(index),gContext);
		super.onPause();
	}

}
