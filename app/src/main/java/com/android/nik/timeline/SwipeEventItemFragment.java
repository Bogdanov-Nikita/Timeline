package com.android.nik.timeline;

import database.TimeLineDatabase;
import database.TimeLineDatabase.Constants;
import model.dialog.DialogContext;
import model.graphics.HtmlBilder;
import model.search.TimeListItem;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * A simple {@link android.app.Fragment} subclass.
 * 
 */
public class SwipeEventItemFragment extends Fragment {

    public static final String PREFIX = MainActivity.class.getPackage()+SwipeEventItemFragment.class.getSimpleName();
	public static final String ARG_SECTION_NUMBER = PREFIX+"_section_number";

	WebView WB;
    int id;

	public static SwipeEventItemFragment newInstance(int position){
		SwipeEventItemFragment fragment = new SwipeEventItemFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER,position);
		fragment.setArguments(args);
		return fragment;
	}
	
	public SwipeEventItemFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_swipe_event_item, container,false);
		int position = getArguments().getInt(ARG_SECTION_NUMBER);

        TimeLineDatabase db = new TimeLineDatabase(getActivity().getApplicationContext());
		db.beginTransaction();

        Cursor IdCursor = db.queryFromTimeLists(new int[]{TimeListItem.VISIBLE,TimeListItem.TEMPORARY});
        IdCursor.moveToFirst();
        long Id[] = new long [IdCursor.getCount()];
        for(int i=0;!IdCursor.isAfterLast();IdCursor.moveToNext(),i++){
            Id[i]=IdCursor.getLong(IdCursor.getColumnIndex(Constants.TimeLists.id));
        }
        IdCursor.close();
        Cursor cursorEvent = db.queryEventByTimeListsMulti(Id, false, false, false, false);

        cursorEvent.moveToPosition(position-1);
		db.setTransactionSuccessful();
		db.endTransaction();

		cursorEvent.moveToPosition(position-1);
		db.beginTransaction();
		Cursor cursorLinks = db.queryLinksByID(id = cursorEvent.getInt(cursorEvent.getColumnIndex(Constants.Event.id)));
		db.setTransactionSuccessful();
		db.endTransaction();
		
		String Links[] = new String [cursorLinks.getCount()];
		int iteration=0;
		for(cursorLinks.moveToFirst();!cursorLinks.isAfterLast();cursorLinks.moveToNext()){
			Links[iteration] = cursorLinks.getString(cursorLinks.getColumnIndex(Constants.Links.link));
			iteration++;
		}
		cursorLinks.close();
		
		String Start_Time = cursorEvent.getString(cursorEvent.getColumnIndex(Constants.Event.start_date)); 
		String End_Time = cursorEvent.getString(cursorEvent.getColumnIndex(Constants.Event.end_date));
		
		db.beginTransaction();
		Log.i("tag", "msg="+cursorEvent.getInt(cursorEvent.getColumnIndex(Constants.Event.geo_id)));
		Cursor cursorGEO = db.queryGeoByID(cursorEvent.getInt(cursorEvent.getColumnIndex(Constants.Event.geo_id)));
		db.setTransactionSuccessful();
		db.endTransaction();
		cursorGEO.moveToFirst();
		
		String GEO_Coordinates = cursorGEO.getString(cursorGEO.getColumnIndex(Constants.Geo.Coordinates)); 
		String GEO_Home = cursorGEO.getString(cursorGEO.getColumnIndex(Constants.Geo.home));
		
		db.beginTransaction();
		Cursor cursorNationality = db.queryNationalityByID(cursorGEO.getLong(cursorGEO.getColumnIndex(Constants.Geo.Nationality_id)));
		Cursor cursorContinent = db.queryContinentByID(cursorGEO.getLong(cursorGEO.getColumnIndex(Constants.Geo.Continent_id)));
		Cursor cursorCountry = db.queryCountryByID(cursorGEO.getLong(cursorGEO.getColumnIndex(Constants.Geo.Country_id)));
		Cursor cursorCity = db.queryCityByID(cursorGEO.getLong(cursorGEO.getColumnIndex(Constants.Geo.City_id)));
		Cursor cursorStreet = db.queryStreetByID(cursorGEO.getLong(cursorGEO.getColumnIndex(Constants.Geo.Street_id)));
		db.setTransactionSuccessful();
		db.endTransaction();
		
		cursorGEO.close();
		
		cursorNationality.moveToFirst();
		cursorContinent.moveToFirst();
		cursorCountry.moveToFirst();
		cursorCity.moveToFirst();
		cursorStreet.moveToFirst();
		
		String GEO_Nationality = cursorNationality.getString(cursorNationality.getColumnIndex(Constants.Nationality.Nationality));
		String GEO_Continent = cursorContinent.getString(cursorContinent.getColumnIndex(Constants.Continent.Continent));
		String GEO_Country = cursorCountry.getString(cursorCountry.getColumnIndex(Constants.Country.Country));
		String GEO_City = cursorCity.getString(cursorCity.getColumnIndex(Constants.City.City));
		String GEO_Street = cursorStreet.getString(cursorStreet.getColumnIndex(Constants.Street.Street));
		
		cursorNationality.close();
		cursorContinent.close();
		cursorCountry.close();
		cursorCity.close();
		cursorStreet.close();


		WB = (WebView) rootView.findViewById(R.id.EventWeb);
		WB.getSettings().setDefaultTextEncodingName("UTF-8");
		String html = HtmlBilder.bildHtml(
				cursorEvent.getString(cursorEvent.getColumnIndex(Constants.Event.title)), 
				getString(R.string.event_mark_edit), 
				Start_Time,End_Time, 
				HtmlBilder.bildGEO(GEO_Coordinates, GEO_Nationality, GEO_Continent, GEO_Country, GEO_City, GEO_Street,GEO_Home,
						new String[]{
						getResources().getString(R.string.event_mark_geo_geo),
						getResources().getString(R.string.event_mark_geo_nationality),
						getResources().getString(R.string.event_mark_geo_continent),
						getResources().getString(R.string.event_mark_geo_country),
						getResources().getString(R.string.event_mark_geo_city),
						getResources().getString(R.string.event_mark_geo_street),
						getResources().getString(R.string.event_mark_geo_home)}), 
				cursorEvent.getString(cursorEvent.getColumnIndex(Constants.Event.body)), 
				getString(R.string.event_mark_links),
				Links,
				new String[]{
					getResources().getString(R.string.event_mark_date),
					getResources().getString(R.string.event_mark_start),
					getResources().getString(R.string.event_mark_end)});
		WB.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
		WB.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Toast.makeText(view.getContext(), "Link Click detected " + url, Toast.LENGTH_SHORT).show();
				if (url.equals(HtmlBilder.EventEdit1)) {
					DialogContext dialogContext;
					Intent ActivityIntent = new Intent(getActivity(), DialogActivity.class);
					dialogContext = new DialogContext(DialogContext.EVENT, id);
					ActivityIntent.putExtra(
							MainActivity.class.getPackage() + DialogContext.class.getSimpleName(),
							dialogContext);
					view.getContext().startActivity(ActivityIntent);
					return true;
				} else if (url.equals(HtmlBilder.EventEdit2)) {
					DialogContext dialogContext;
					Intent ActivityIntent = new Intent(getActivity(), DialogActivity.class);
					dialogContext = new DialogContext(DialogContext.EVENT_LINKS, id);
					ActivityIntent.putExtra(
							MainActivity.class.getPackage() + DialogContext.class.getSimpleName(),
							dialogContext);
					view.getContext().startActivity(ActivityIntent);
					return true;
				}
				//WB.loadUrl(url);
				//return true;
				return super.shouldOverrideUrlLoading(view, url);
			}
		});

		cursorEvent.close();
		db.close();
		return rootView;
	}

}
