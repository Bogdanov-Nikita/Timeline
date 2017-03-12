package com.android.nik.timeline;

import database.TimeLineDatabase;
import model.search.SearchContext;
import model.search.SimpleSearch;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.Toast;


/**
 * Фрагмент упрощенного поиска
 */
public class SimpleSearchFragment extends Fragment {
	
	public static final String PREFIX = MainActivity.class.getPackage()+SimpleSearchFragment.class.getSimpleName();
	public static final String SEARCH_TEXT = PREFIX+"_search_text";
	
	
	public TimeLineDatabase Database;
	
	AutoCompleteTextView SearchAutoCompleteText;
	SimpleSearch Context;
	
    public SimpleSearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        View rootView = inflater.inflate(R.layout.fragment_s_search, container, false);
        Button AdvancedButton = (Button) rootView.findViewById(R.id.AdvancedButton);
        AdvancedButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getFragmentManager().beginTransaction()
				.replace(R.id.container,
						new AdvancedSearchFragment(), 
						AdvancedSearchFragment.class.getSimpleName())
				.commit();
                String Title = getResources().getStringArray(R.array.left_menu_list)[LeftMenu.ADVANCED_SEARCH_VIEW];
                ((MainActivity)getActivity()).getSupportActionBar().setTitle(Title);
			}
		});
        
        Database = new TimeLineDatabase(this.getActivity().getApplicationContext());
        
        Cursor cursor  = Database.query(
        		TimeLineDatabase.Constants.All_Keywords.TABLE_NAME, 
        		new String[]{
        				TimeLineDatabase.Constants.All_Keywords.id,
        				TimeLineDatabase.Constants.All_Keywords.table_id,
        				TimeLineDatabase.Constants.All_Keywords.word}, 
        		null, null, null, null, null);
        
        SimpleCursorAdapter SearchCursorAdapter = new SimpleCursorAdapter(
        		getActivity().getApplicationContext(), 
        		R.layout.s_search_item, 
        		cursor,
        		new String[]{TimeLineDatabase.Constants.All_Keywords.word,TimeLineDatabase.Constants.All_Keywords.table_id},
        		new int[]{R.id.Text_word,R.id.Text_table_id},
        		SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        
		SearchCursorAdapter.setViewBinder(new ViewBinder() {
			
			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				switch (view.getId()) {
				case R.id.Text_word: 
					((TextView)view).setText(cursor.getString(columnIndex)); return true;
				case R.id.Text_table_id: 
					((TextView)view).setText(convertToTableName(cursor.getInt(columnIndex))); return true;
				case R.layout.s_search_item:return true ;	
				}
				return false;
			}
		});
		
        SearchCursorAdapter.setCursorToStringConverter(new CursorToStringConverter() {
			@Override
			public CharSequence convertToString(Cursor cursor) {				
				int id = cursor.getInt(cursor.getColumnIndex(TimeLineDatabase.Constants.All_Keywords.id));
				int table_id = cursor.getInt(cursor.getColumnIndex(TimeLineDatabase.Constants.All_Keywords.table_id));
				String word = cursor.getString(cursor.getColumnIndex(TimeLineDatabase.Constants.All_Keywords.word));
				Context = new SimpleSearch(word, table_id,id);
				return word;
			}
		});
        
        SearchCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
			
			@Override
			public Cursor runQuery(CharSequence constraint) {
				if(constraint!=null){
				if(!constraint.toString().trim().equals("")){
				return Database.query(
		        		TimeLineDatabase.Constants.All_Keywords.TABLE_NAME, 
		        		new String[]{
		        				TimeLineDatabase.Constants.All_Keywords.id,
		        				TimeLineDatabase.Constants.All_Keywords.table_id,
		        				TimeLineDatabase.Constants.All_Keywords.word},
		        				TimeLineDatabase.Constants.All_Keywords.word + " LIKE ?",
		        				new String[] {constraint.toString().trim()+"%"}, null, null, null);
				}else{return null;}}else{return null;}
				}});
                
	    SearchAutoCompleteText = (AutoCompleteTextView)rootView.findViewById(R.id.SearchAutoCompleteText);
	    SearchAutoCompleteText.setThreshold(1);
	    SearchAutoCompleteText.setAdapter(SearchCursorAdapter);
	    SearchAutoCompleteText.setOnItemClickListener(
	    		new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
				    	Database.close();
				    	((MainActivity)getActivity()).LaunchScreenActivity(new SearchContext(Context));
					}
		});
	    
	    ImageButton SearchButton = (ImageButton) rootView.findViewById(R.id.SSearchButton);
	    SearchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String word = SearchAutoCompleteText.getText().toString().trim();
				if(word.equals("")){
					Toast.makeText(getActivity(),R.string.s_search_enmpty_text,Toast.LENGTH_SHORT).show();
				}else{
			    	Database.close();
                    Database.close();
                    //т.к. данный фрагмент привязан к MainActivity
                    //то для того чтобы не писать лишний раз запихнули метод LaunchScreenActivity в MainActivity.
					((MainActivity)getActivity()).LaunchScreenActivity(new SearchContext(new SimpleSearch(word, 0, -1)));
				}
			}
		});

        return rootView;
    }

    /**
     * возвращает intent от Activity
     * */
    /*Intent getActivityIntent(){
    	return getActivity().getIntent();
    }*/
    
    @Override
    public void onPause() {
    	super.onPause();
    }

    //ф-ия обратную convertToTableName времнно ненужна
    /*int convertTableToNumber(String Table){
    	String array[] = getResources().getStringArray(R.array.s_search_category);
    	for(int i=0;i<array.length;i++){
    		if (array[i].equals(Table)){return i;}
    	}
    	return -1;
    }*/
    //Функция приведения названия таблицы по его номеру.
    String convertToTableName(int i){
    	if(i>0){i--;};
		String array[] = getResources().getStringArray(R.array.s_search_category);
		if(i<array.length){
			return array[i];
		}else{
			return getResources().getString(R.string.s_search_not_find);
		}
	}   
}
