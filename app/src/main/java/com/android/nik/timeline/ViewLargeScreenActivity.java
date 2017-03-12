package com.android.nik.timeline;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Spinner;


import database.TimeLineDatabase;
import model.Settings;
import model.graphics.CompositionView;
import model.graphics.DataContainer;
import model.graphics.GraphicsContext;
import model.graphics.LineView;
import model.graphics.MapView;
import model.graphics.SwipeView;
import model.search.SearchContext;
import model.search.TimeListItem;

public class ViewLargeScreenActivity extends AppCompatActivity {

    private static final String STATE_DRAWER_LAYOUT = "drawer_layout";
    private static final String STATE_FRAMES_ORIENTATION = "frames_orientation";

    public RightMenu RightMenu;
    public DrawerLayout drawerLayout;
    public Spinner WindowSpinner[];
    public CompositionView view;
    public Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;

    boolean orientation = false;
    //LinearLayout.VERTICAL - false;
    //LinearLayout.HORIZONTAL - true;

    TimeListItem TimeList;
    GraphicsContext gContext[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_large_screen);

        int window = getWindowFromPreferences();
        gContext = new GraphicsContext[window];
        if(savedInstanceState == null) {
            for(int i = 0; i < window; i++) {
                gContext[i] = new GraphicsContext(new LineView(), new SwipeView(), new SwipeView(), new SwipeView(), new MapView(), GraphicsContext.LINE_VIEW);
                getIntent().putExtra(MainActivity.class.getPackage() + GraphicsContext.class.getSimpleName() + String.valueOf(i),gContext[i]);
            }
        }
        else{
            for(int i = 0; i < window; i++) {
                gContext[i] = getIntent().getParcelableExtra(MainActivity.class.getPackage() + GraphicsContext.class.getSimpleName() + String.valueOf(i));
            }
            orientation = savedInstanceState.getBoolean(STATE_FRAMES_ORIENTATION);
        }

        view = new CompositionView(this,R.id.windows_layout,orientation,window);

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

        toolbar = (Toolbar) findViewById(R.id.ScreenToolbar);
        toolbar.setLogo(R.drawable.ic_launcher);
        drawerLayout = (DrawerLayout)findViewById(R.id.DrawerLayoutLargeScreen);
        actionBarDrawerToggle =
                new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        setSupportActionBar(toolbar);
        SpinnerInit(window);

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
        getIntent().putExtra(MainActivity.class.getPackage() + TimeListItem.class.getSimpleName(), TimeList);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_large_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
            case R.id.action_right_menu:
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
                return true;
            case R.id.action_window_type:
                PopupMenu popup = new PopupMenu(
                        toolbar.getContext(),//тема.
                        getWindow().findViewById(R.id.action_window_type));//расположение.
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return onPopupMenuItemClick(item);
                    }
                });
                popup.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    boolean onPopupMenuItemClick(MenuItem item){
        switch (item.getItemId()){//поясненние: разделение противоположно расположению
            case R.id.action_vertical:
                orientation = true;
                view.setOrientation(true);
                return true;
            case R.id.action_horizontal:
                orientation = false;
                view.setOrientation(false);
                return true;
        }
        return false;
    }

    int getWindowFromPreferences(){
        SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        int window;
        try {
            window = Integer.valueOf(Preferences.getString(Settings.WINDOWS, Settings.DEFAULT_WINDOWS));
        }catch(NumberFormatException e){window = Settings.TWO_WINDOWS;}
        return window;
    }

    void SpinnerInit(int window){
        WindowSpinner = new Spinner[window];
        for(int i=0; i < window; i++){
            final int Number = i;
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(
                            getApplicationContext(),
                            R.layout.item,
                            getResources().getStringArray(R.array.view_activity));
            WindowSpinner[i] = (Spinner)findViewById(CompositionView.WindowSpinnerId[i]);
            WindowSpinner[i].setAdapter(adapter);
            WindowSpinner[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    onSpinnerItemSelected(position, CompositionView.ContainerId[Number], Number);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    //добавить действие по умолчанию, не обязательно
                }
            });
        }
    }

    void onSpinnerItemSelected(int position,int ContainerId,int number){
        // запуск фрагмента по выбору позиции Spinner из Toolbar
        Fragment fragment;
        String Tag;
        switch(position){
            case GraphicsContext.LINE_VIEW:
                Tag = LineViewFragment.class.getSimpleName() + String.valueOf(number);
                fragment = getFragmentManager().findFragmentByTag(Tag);
                fragment = (fragment != null) ? fragment : LineViewFragment.newInstance(number);
                gContext[number].setTypeOfView(GraphicsContext.LINE_VIEW);
                break;
            case GraphicsContext.GRID_VIEW:
                Tag = GridViewFragment.class.getSimpleName() + String.valueOf(number);
                fragment = getFragmentManager().findFragmentByTag(Tag);
                fragment = (fragment != null) ? fragment : GridViewFragment.newInstance(number);
                gContext[number].setTypeOfView(GraphicsContext.GRID_VIEW);
                break;
            case GraphicsContext.SWIPE_GALLERY_VIEW:
                Tag = SwipeListFragment.class.getSimpleName() + String.valueOf(SwipeListFragment.GALLERY_TYPE) + String.valueOf(number);
                fragment = getFragmentManager().findFragmentByTag(Tag);
                fragment = (fragment != null) ? fragment : SwipeListFragment.newInstance(SwipeListFragment.GALLERY_TYPE, number);
                gContext[number].setTypeOfView(GraphicsContext.SWIPE_GALLERY_VIEW);
                break;
            case GraphicsContext.SWIPE_LIST_VIEW:
                Tag = SwipeListFragment.class.getSimpleName() + String.valueOf(SwipeListFragment.LIST_TYPE) + String.valueOf(number);
                fragment = getFragmentManager().findFragmentByTag(Tag);
                fragment = (fragment != null) ? fragment : SwipeListFragment.newInstance(SwipeListFragment.LIST_TYPE, number);
                gContext[number].setTypeOfView(GraphicsContext.SWIPE_LIST_VIEW);
                break;
            case GraphicsContext.SWIPE_EVENT_VIEW:
                Tag = SwipeListFragment.class.getSimpleName()+SwipeListFragment.EVENT_TYPE + String.valueOf(number);
                fragment = getFragmentManager().findFragmentByTag(Tag);
                fragment = (fragment != null) ? fragment : SwipeListFragment.newInstance(SwipeListFragment.EVENT_TYPE, number);
                gContext[number].setTypeOfView(GraphicsContext.SWIPE_EVENT_VIEW);
                break;
            case GraphicsContext.MAP_VIEW:
                Tag = MapFragment.class.getSimpleName() + String.valueOf(number);
                fragment = getFragmentManager().findFragmentByTag(Tag);
                fragment = (fragment != null) ? fragment : MapFragment.newInstance(number);
                gContext[number].setTypeOfView(GraphicsContext.MAP_VIEW);
                break;
            default:
                Tag = "default";
                fragment = LineViewFragment.newInstance(LineViewFragment.DEFAULT_INDEX);
        }
        getFragmentManager().beginTransaction()
                .replace(ContainerId,fragment,Tag)
                .commit();
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        // Restore the previously serialized current dropdown position.
        if (savedInstanceState.containsKey(STATE_DRAWER_LAYOUT)) {
            SparseArray<Parcelable> container;
            container = savedInstanceState.getSparseParcelableArray(STATE_DRAWER_LAYOUT);
            drawerLayout.restoreHierarchyState(container);
        }
        if(savedInstanceState.containsKey(STATE_FRAMES_ORIENTATION)){
            orientation = savedInstanceState.getBoolean(STATE_FRAMES_ORIENTATION);
        }
        TimeList = getIntent().getParcelableExtra(MainActivity.class.getPackage()+TimeListItem.class.getSimpleName());
        for(int i = 0; i < getWindowFromPreferences(); i++) {
            gContext[i] = getIntent().getParcelableExtra(MainActivity.class.getPackage() + GraphicsContext.class.getSimpleName() + String.valueOf(i));
        }
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
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current dropdown position.
        SparseArray<Parcelable> container = new SparseArray<>();
        drawerLayout.saveHierarchyState(container);
        outState.putSparseParcelableArray(STATE_DRAWER_LAYOUT, container);
        outState.putBoolean(STATE_FRAMES_ORIENTATION, orientation);

        getIntent().putExtra(MainActivity.class.getPackage()+TimeListItem.class.getSimpleName(), TimeList);
        for(int i = 0; i < getWindowFromPreferences(); i++) {
            getIntent().putExtra(MainActivity.class.getPackage() + GraphicsContext.class.getSimpleName() + String.valueOf(i), gContext[i]);
        }
    }

}
