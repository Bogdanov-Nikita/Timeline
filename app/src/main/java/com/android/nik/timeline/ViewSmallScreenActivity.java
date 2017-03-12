package com.android.nik.timeline;

import database.TimeLineDatabase;
import model.Settings;
import model.graphics.GraphicsContext;
import model.graphics.LineView;
import model.graphics.MapView;
import model.graphics.SwipeView;
import model.search.SearchContext;
import model.search.TimeListItem;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

public class ViewSmallScreenActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    /**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    private static final String STATE_DRAWER_LAYOUT = "drawer_layout";
    private static final String INDEX = String.valueOf(Settings.DEFAULT_INDEX);

    public RightMenu RightMenu;
    public Spinner ToolbarSpinner;
    public DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    TimeListItem TimeList;
    GraphicsContext gContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_small_screen);

        drawerLayout = (DrawerLayout)findViewById(R.id.DrawerLayoutSmallScreen);

        if(savedInstanceState == null) {
            gContext = new GraphicsContext(new LineView(), new SwipeView(),new SwipeView(), new SwipeView(), new MapView(),GraphicsContext.LINE_VIEW);
            getIntent().putExtra(
                    MainActivity.class.getPackage() +
                    GraphicsContext.class.getSimpleName() +
                    INDEX,gContext);
        }
        else{
            Log.i("state","View Small is not null");
            gContext = getIntent().getParcelableExtra(
                    MainActivity.class.getPackage() +
                    GraphicsContext.class.getSimpleName() +
                    INDEX);
        }

        getFragmentManager().beginTransaction()
                .add(R.id.left_menu,
                        new ContentAddMenu(),
                        ContentAddMenu.class.getSimpleName())
                .commit();
        RightMenu = (RightMenu)getFragmentManager().findFragmentByTag(RightMenu.class.getSimpleName());
        getFragmentManager().beginTransaction()
                .add(R.id.right_menu,
                        (RightMenu != null )? RightMenu:(RightMenu = new RightMenu()),
                        RightMenu.class.getSimpleName())
                .commit();

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getApplicationContext(),
                        R.layout.item,
                        getResources().getStringArray(R.array.view_activity));

        Toolbar toolbar = (Toolbar) findViewById(R.id.ScreenToolbar);
        toolbar.setLogo(R.drawable.ic_launcher);
        ToolbarSpinner = (Spinner)toolbar.findViewById(R.id.ToolbarSpinner);
        ToolbarSpinner.setAdapter(adapter);
        ToolbarSpinner.setOnItemSelectedListener(this);

        ImageButton button = (ImageButton) toolbar.findViewById(R.id.RightMenuButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(drawerLayout.isDrawerOpen(GravityCompat.END)){
                        drawerLayout.closeDrawer(GravityCompat.END);
                    }else{
                        drawerLayout.openDrawer(GravityCompat.END);
                    }
                }
        });

        actionBarDrawerToggle =
                new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        setSupportActionBar(toolbar);

		SearchContext searchContext = getIntent().getParcelableExtra(this.getPackageName()+SearchContext.class.getSimpleName());

		TimeLineDatabase db = new TimeLineDatabase(getApplicationContext());
		db.beginTransaction();
        //только сдесь Id=-1 т.к. он ещё не задан и создаёться в других случая реальный id
        //так же отправляеться
		long id = -1;
		switch (searchContext.getTypeOfSearch()) {
		case SearchContext.SIMPLE_SEARCH:
            //TODO: продолжить
			//Scontext.getSimpleSearch().getWord();
			//db.insertTimeLists(title, about, color, visible);
			//db.queryFromAllKeywords(selectionArgs, Table, collumName, KeywordCollum, collumTable_N);
			TimeList = new TimeListItem(id,TimeListItem.DEFAULT_TITLE,TimeListItem.DEFAULT_ABOUT,
					TimeListItem.DEFAULT_COLOR,TimeListItem.TEMPORARY);
			break;
		case SearchContext.EXPANDED_SEARCH:
			id = db.SaveSearchQuery(searchContext,
					new TimeListItem(id, 
							TimeListItem.DEFAULT_TITLE, 
							TimeListItem.DEFAULT_ABOUT, 
							TimeListItem.DEFAULT_COLOR, 
							TimeListItem.TEMPORARY));
			TimeList = new TimeListItem(id,TimeListItem.DEFAULT_TITLE,TimeListItem.DEFAULT_ABOUT,
					TimeListItem.DEFAULT_COLOR,TimeListItem.TEMPORARY);
			break;				
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
		getIntent().putExtra(MainActivity.class.getPackage()+TimeListItem.class.getSimpleName(), TimeList);
	}

	@Override
	public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
        if (savedInstanceState.containsKey(STATE_DRAWER_LAYOUT)) {
            SparseArray<Parcelable> container;
            container = savedInstanceState.getSparseParcelableArray(STATE_DRAWER_LAYOUT);
            drawerLayout.restoreHierarchyState(container);

        }
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            ToolbarSpinner.setSelection(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
		TimeList = getIntent().getParcelableExtra(
                MainActivity.class.getPackage() +
                TimeListItem.class.getSimpleName());
		gContext = getIntent().getParcelableExtra(
                MainActivity.class.getPackage() +
                GraphicsContext.class.getSimpleName() +
                INDEX);
	}

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if( id == android.R.id.home){
            if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START);
            }else{
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
        SparseArray<Parcelable> container = new SparseArray<>();
        drawerLayout.saveHierarchyState(container);
        outState.putSparseParcelableArray(STATE_DRAWER_LAYOUT,container);

        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, ToolbarSpinner.getSelectedItemPosition());

        getIntent().putExtra(MainActivity.class.getPackage()+TimeListItem.class.getSimpleName(), TimeList);
		getIntent().putExtra(MainActivity.class.getPackage()+GraphicsContext.class.getSimpleName(),gContext);
	}

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// запуск фрагмента по выбору позиции Spinner из Toolbar
		Fragment fragment;
		switch(position){
		case GraphicsContext.LINE_VIEW:			
			fragment  = getFragmentManager().findFragmentByTag(LineViewFragment.class.getSimpleName());
			gContext.setTypeOfView(GraphicsContext.LINE_VIEW);
			getFragmentManager().beginTransaction()
			.replace(R.id.container,
					(fragment != null) ? fragment : LineViewFragment.newInstance(LineViewFragment.DEFAULT_INDEX),
					LineViewFragment.class.getSimpleName())
			.commit();
			break;
		case GraphicsContext.GRID_VIEW:
			fragment  = getFragmentManager().findFragmentByTag(GridViewFragment.class.getSimpleName());
			gContext.setTypeOfView(GraphicsContext.GRID_VIEW);
			getFragmentManager().beginTransaction()
			.replace(R.id.container,
                    (fragment != null) ? fragment : GridViewFragment.newInstance(GridViewFragment.DEFAULT_INDEX),
                    GridViewFragment.class.getSimpleName())
			.commit();
			break;		
		case GraphicsContext.SWIPE_GALLERY_VIEW:
			fragment  = getFragmentManager().findFragmentByTag(SwipeListFragment.class.getSimpleName()+SwipeListFragment.GALLERY_TYPE);
			gContext.setTypeOfView(GraphicsContext.SWIPE_GALLERY_VIEW);
			getFragmentManager().beginTransaction()
			.replace(R.id.container,
					(fragment != null) ? fragment : SwipeListFragment.newInstance(SwipeListFragment.GALLERY_TYPE, SwipeListFragment.DEFAULT_INDEX),
					SwipeListFragment.class.getSimpleName()+SwipeListFragment.GALLERY_TYPE)
			.commit();
			break;
		case GraphicsContext.SWIPE_LIST_VIEW:
			fragment  = getFragmentManager().findFragmentByTag(SwipeListFragment.class.getSimpleName()+SwipeListFragment.LIST_TYPE);
			gContext.setTypeOfView(GraphicsContext.SWIPE_LIST_VIEW);
			getFragmentManager().beginTransaction()
			.replace(R.id.container,
					(fragment != null) ? fragment : SwipeListFragment.newInstance(SwipeListFragment.LIST_TYPE, SwipeListFragment.DEFAULT_INDEX),
					SwipeListFragment.class.getSimpleName()+SwipeListFragment.LIST_TYPE)
			.commit();
			break;					
		case GraphicsContext.SWIPE_EVENT_VIEW:
			fragment  = getFragmentManager().findFragmentByTag(SwipeListFragment.class.getSimpleName()+SwipeListFragment.EVENT_TYPE);
			gContext.setTypeOfView(GraphicsContext.SWIPE_EVENT_VIEW);
			getFragmentManager().beginTransaction()
			.replace(R.id.container,
					(fragment != null) ? fragment : SwipeListFragment.newInstance(SwipeListFragment.EVENT_TYPE, SwipeListFragment.DEFAULT_INDEX),
					SwipeListFragment.class.getSimpleName()+SwipeListFragment.EVENT_TYPE)
			.commit();
			break;	
		case GraphicsContext.MAP_VIEW:
			fragment  = getFragmentManager().findFragmentByTag(MapFragment.class.getSimpleName());
			gContext.setTypeOfView(GraphicsContext.MAP_VIEW);
			getFragmentManager().beginTransaction()
			.replace(R.id.container,
					(fragment != null) ? fragment : MapFragment.newInstance(MapFragment.DEFAULT_INDEX),
					MapFragment.class.getSimpleName())
			.commit();
			break;	     		     		     		     			
		}
	}

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // добавить действие по умолчанию, не обязательно
    }
}
