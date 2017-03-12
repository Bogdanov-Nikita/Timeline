package com.android.nik.timeline;

import database.TimeLineDatabase;
import model.search.Date;
import model.search.GeoAreas;
import model.search.GeoLocation;
import model.search.SearchContext;
import model.search.SearchItemContainer;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;

/**
 * Фрагмент расширенного поиска
 */
public class AdvancedSearchFragment extends Fragment {
	
	static final int AUTHOR = 1;
	static final int CATEGORY = 2;
	static final int TIMELINE = 3;
	static final int PERSON = 4;
	
	public TimeLineDatabase Database;
	
	GeoAreas geoAreas;
	
	SearchItemContainer authors;
	SearchItemContainer timeLine;
	SearchItemContainer categorys;
	SearchItemContainer persons;
	
	Date timeDate;
	
	ToggleButton CategoryButton;
	ToggleButton TimelineButton;
	ToggleButton PersonButton;
	ToggleButton AuthorButton;
	ToggleButton NationalityButton;
	ToggleButton ContinentButton;
	ToggleButton CountryButton;
	ToggleButton CityButton;
	ToggleButton StreetButton;

	MultiAutoCompleteTextView CategoryAutoComplete;
	MultiAutoCompleteTextView TimelineAutoComplete;
	MultiAutoCompleteTextView PersonsAutoComplete;
	MultiAutoCompleteTextView AuthorsAutoComplete;
	MultiAutoCompleteTextView NationalityAutoComplete;
	MultiAutoCompleteTextView ContinentAutoComplete;
	MultiAutoCompleteTextView CountryAutoComplete;
	MultiAutoCompleteTextView CityAutoComplete;
	MultiAutoCompleteTextView StreetAutoComplete;
	
	EditText StartDate;
	EditText EndDate;
	
    public AdvancedSearchFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	Database = new TimeLineDatabase(this.getActivity().getApplicationContext());
        View rootView = inflater.inflate(R.layout.fragment_a_search, container, false);
        //кнопки для определения использования OR или AND операторов.
        CategoryButton = (ToggleButton)rootView.findViewById(R.id.CategoryAndOrButton);
        TimelineButton = (ToggleButton)rootView.findViewById(R.id.TimelineAndOrButton);
        PersonButton = (ToggleButton)rootView.findViewById(R.id.PersonsAndOrButton);
        AuthorButton = (ToggleButton)rootView.findViewById(R.id.AuthorsAndOrButton);
        NationalityButton = (ToggleButton)rootView.findViewById(R.id.NationalityAndOrButton);
        ContinentButton = (ToggleButton)rootView.findViewById(R.id.ContinentAndOrButton);
        CountryButton = (ToggleButton)rootView.findViewById(R.id.CountryAndOrButton);
        CityButton = (ToggleButton)rootView.findViewById(R.id.CityAndOrButton);
        StreetButton = (ToggleButton)rootView.findViewById(R.id.StreetsAndOrButton);
        
        SimpleCursorAdapter CategoryAdapter = initSimpleCursorAdapter(CATEGORY);
        SimpleCursorAdapter TimelineAdapter = initSimpleCursorAdapter(TIMELINE);
        SimpleCursorAdapter PrsonAdapter = initSimpleCursorAdapter(PERSON);
        SimpleCursorAdapter AutorAdapter = initSimpleCursorAdapter(AUTHOR);        
        SimpleCursorAdapter NationalityAdapter = initGeoCursorAdapter(
        		TimeLineDatabase.Constants.Nationality.TABLE_NAME,
        		TimeLineDatabase.Constants.Nationality.id,
        		TimeLineDatabase.Constants.Nationality.Nationality);
        SimpleCursorAdapter ContinentAdapter = initGeoCursorAdapter(
        		TimeLineDatabase.Constants.Continent.TABLE_NAME,
        		TimeLineDatabase.Constants.Continent.id,
        		TimeLineDatabase.Constants.Continent.Continent);
        SimpleCursorAdapter CountryAdapter = initGeoCursorAdapter(
        		TimeLineDatabase.Constants.Country.TABLE_NAME,
        		TimeLineDatabase.Constants.Country.id,
        		TimeLineDatabase.Constants.Country.Country);
        SimpleCursorAdapter CityAdapter = initGeoCursorAdapter(
        		TimeLineDatabase.Constants.City.TABLE_NAME,
        		TimeLineDatabase.Constants.City.id,
        		TimeLineDatabase.Constants.City.City);
        SimpleCursorAdapter StreetAdapter = initGeoCursorAdapter(
        		TimeLineDatabase.Constants.Street.TABLE_NAME,
        		TimeLineDatabase.Constants.Street.id,
        		TimeLineDatabase.Constants.Street.Street);
        
        CategoryAutoComplete = initMultiAutoComplete(R.id.CategoryAutoComplete, CategoryAdapter, rootView);
        TimelineAutoComplete = initMultiAutoComplete(R.id.TimelineAutoComplete, TimelineAdapter, rootView);
        PersonsAutoComplete = initMultiAutoComplete(R.id.PersonsAutoComplete, PrsonAdapter, rootView);
        AuthorsAutoComplete = initMultiAutoComplete(R.id.AuthorsAutoComplete, AutorAdapter, rootView);       
        NationalityAutoComplete = initMultiAutoComplete(R.id.NationalityAutoComplete,NationalityAdapter,rootView);
        ContinentAutoComplete = initMultiAutoComplete(R.id.ContinentAutoComplete,ContinentAdapter,rootView);
    	CountryAutoComplete = initMultiAutoComplete(R.id.CountryAutoComplete,CountryAdapter,rootView);
    	CityAutoComplete = initMultiAutoComplete(R.id.CityAutoCompleteText,CityAdapter,rootView);
    	StreetAutoComplete = initMultiAutoComplete(R.id.StreetsAutoComplete,StreetAdapter,rootView);
        
    	StartDate = (EditText)rootView.findViewById(R.id.EditCoordinates);
    	EndDate = (EditText)rootView.findViewById(R.id.EditTextEnd);
    	
        Button StartSearch = (Button) rootView.findViewById(R.id.ASearchButton);
        StartSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if( 
				isEmpty(CategoryAutoComplete.getText().toString())&
				isEmpty(TimelineAutoComplete.getText().toString())&
				isEmpty(PersonsAutoComplete.getText().toString())&
				isEmpty(AuthorsAutoComplete.getText().toString())&
				isEmpty(AuthorsAutoComplete.getText().toString())&
				isEmpty(NationalityAutoComplete.getText().toString())&
				isEmpty(ContinentAutoComplete.getText().toString())&
				isEmpty(CountryAutoComplete.getText().toString())&
				isEmpty(CityAutoComplete.getText().toString())&
				isEmpty(StreetAutoComplete.getText().toString())&
				isEmpty(StartDate.getText().toString())&
				isEmpty(EndDate.getText().toString())){
					Toast.makeText(getActivity(), R.string.s_search_enmpty_text, Toast.LENGTH_SHORT).show();
				}else{
								
				String Category[] = StringSeparateParse(CategoryAutoComplete.getText().toString());
				categorys = new SearchItemContainer((Category==null)?new String[]{""}:Category,CategoryButton.isChecked());				
				String Timeline[] = StringSeparateParse(TimelineAutoComplete.getText().toString());
				timeLine = new SearchItemContainer((Timeline==null)?new String[]{""}:Timeline,TimelineButton.isChecked());								
				String Persons[] = StringSeparateParse(PersonsAutoComplete.getText().toString());				
				persons = new SearchItemContainer((Persons==null)?new String[]{""}:Persons, PersonButton.isChecked());												
				String Authors[] = StringSeparateParse(AuthorsAutoComplete.getText().toString());
				authors = new SearchItemContainer((Authors==null)?new String[]{""}:Authors, AuthorButton.isChecked());
				
				String Nationality[] = StringSeparateParse(NationalityAutoComplete.getText().toString());								
				String Continent[] = StringSeparateParse(ContinentAutoComplete.getText().toString());
				String Country[] = StringSeparateParse(CountryAutoComplete.getText().toString());				
				String City[] = StringSeparateParse(CityAutoComplete.getText().toString());				
				String Street[] = StringSeparateParse(StreetAutoComplete.getText().toString());
				
				if(isEmpty(new String[][]{Category,Timeline,Persons,Authors,Nationality,Continent,Country,City,Street})){
					Toast.makeText(getActivity(), R.string.s_search_enmpty_text, Toast.LENGTH_SHORT).show();
				}else{				
					int a[] = new int[5];
					a[0]=(Nationality==null)?-1:Nationality.length;
					a[1]=(Continent==null)?-1:Continent.length;
					a[2]=(Country==null)?-1:Country.length;
					a[3]=(City==null)?-1:City.length;
					a[4]=(Street==null)?-1:Street.length;
					int Maxlength = Math.max(Math.max(Math.max(a[0], a[1]),Math.max(a[2], a[3])),a[4]);
					GeoLocation	geoLocation[];
					if(Maxlength > 0){
						geoLocation = new GeoLocation[Maxlength];
						//������������� geoLocation
						for(int i = 0; i < Maxlength; i++){
							String temp[]=new String[5];
							temp[0]=initTemp(Nationality, i);
							temp[1]=initTemp(Continent, i);
							temp[2]=initTemp(Country, i);
							temp[3]=initTemp(City, i);
							temp[4]=initTemp(Street, i);
							geoLocation[i] = new GeoLocation(temp[0], temp[1], temp[2], temp[3], temp[4], null);
						}
					}else{
						geoLocation = new GeoLocation[]{new GeoLocation()};
					}				
					geoAreas = new GeoAreas(
							geoLocation,
							NationalityButton.isChecked(),
							ContinentButton.isChecked(),
							CountryButton.isChecked(),
							CityButton.isChecked(),
							StreetButton.isChecked());			
					timeDate = new Date(StartDate.toString(), EndDate.toString());
					((MainActivity)getActivity()).LaunchScreenActivity(new SearchContext(geoAreas, authors, timeLine, categorys, persons, timeDate));
					}
				}
			}
		});
        return rootView;
    }
    boolean isEmpty(String array[][]){
    	for(String tempArr[] : array){
    		if(!isEmpty(tempArr)){return false;}
    	}
    	return true;
    }
    boolean isEmpty(String array[]){
    	if(array!=null){
    		for(String tempStr : array){
    			if(!tempStr.equals("")){return false;}
    		}
    		return true;
    	}else{return true;}
    }    
    boolean isEmpty(String str){
    	return (str == null || str.trim().equals(""));
    }
    
    String initTemp(String array[],int i){
    	String temp;
    	if(array != null){
			if(i < array.length){
				temp = array[i];
			}else{
				temp = null;
			}
		}else{
			temp = null;
		}
    	return temp;
    }
    
    String[] StringSeparateParse(String AutoCompliteText){
    	if(AutoCompliteText!=null){
    		AutoCompliteText = AutoCompliteText.trim();
    		if(!AutoCompliteText.equals("")){
    			String array[] = AutoCompliteText.split(",");
    			for(int i=0;i<array.length;i++){array[i]= array[i].trim();}
    			return array;
    		}
    	}
    	return null;
    }
    
    MultiAutoCompleteTextView initMultiAutoComplete(int id,SimpleCursorAdapter Adapter,View rootView){
    	MultiAutoCompleteTextView MultiAutoCompleteTextView = (MultiAutoCompleteTextView) rootView.findViewById(id);
    	MultiAutoCompleteTextView.setAdapter(Adapter);
    	MultiAutoCompleteTextView.setThreshold(1);
    	MultiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    	return MultiAutoCompleteTextView;
    }
       
    SimpleCursorAdapter initSimpleCursorAdapter(final int Table_id){    	
    	SimpleCursorAdapter CursorAdapter = new SimpleCursorAdapter(
    			getActivity().getApplicationContext(), 
    			R.layout.a_search_item, 
    			Database.query(
		        		TimeLineDatabase.Constants.All_Keywords.TABLE_NAME, 
		        		new String[]{
		        				TimeLineDatabase.Constants.All_Keywords.id,
		        				TimeLineDatabase.Constants.All_Keywords.table_id,
		        				TimeLineDatabase.Constants.All_Keywords.word},
		        				TimeLineDatabase.Constants.All_Keywords.table_id + "=?",
		        				new String[] {String.valueOf(Table_id)}, null, null, null), 
    			new String[]{TimeLineDatabase.Constants.All_Keywords.word}, 
    			new int[]{R.id.text1}, 
    			SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    	CursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {			
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
		        				TimeLineDatabase.Constants.All_Keywords.table_id + "=? AND "+
		        				TimeLineDatabase.Constants.All_Keywords.word + " LIKE ?",
		        				new String[] {String.valueOf(Table_id),constraint.toString().trim()+"%"}, null, null, null);
				}else{return null;}}else{return null;}
				}});
    	CursorAdapter.setCursorToStringConverter(new CursorToStringConverter() {
			@Override
			public CharSequence convertToString(Cursor cursor) {
				return cursor.getString(cursor.getColumnIndex(TimeLineDatabase.Constants.All_Keywords.word));
			}
		});
    	return CursorAdapter;
    }
    
    SimpleCursorAdapter initGeoCursorAdapter(final String Table,final String id,final String collum){
    	SimpleCursorAdapter CursorAdapter = new SimpleCursorAdapter(
    			getActivity().getApplicationContext(), 
    			R.layout.a_search_item, 
    			Database.query(Table,new String[]{id,collum},
		        				null,null, null, null, null), 
    			new String[]{collum}, 
    			new int[]{R.id.text1}, 
    			SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    	CursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {			
			@Override
			public Cursor runQuery(CharSequence constraint) {
				if(constraint!=null){
				if(!constraint.toString().trim().equals("")){
				return Database.query(Table, new String[]{id,collum},collum + " LIKE ?",
		        		new String[] {constraint.toString().trim()+"%"}, null, null, null);
				}else{return null;}}else{return null;}
				}});
    	CursorAdapter.setCursorToStringConverter(new CursorToStringConverter() {
			@Override
			public CharSequence convertToString(Cursor cursor) {
				return cursor.getString(cursor.getColumnIndex(collum));
			}
		});
    	return CursorAdapter;
    }

}
