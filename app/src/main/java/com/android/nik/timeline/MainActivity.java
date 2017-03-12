package com.android.nik.timeline;

import model.Settings;
import model.search.SearchContext;

import android.content.res.Configuration;
import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    //static final String FIRST_RUN = "is_first_run";//0-yes, 1-no
    //static final String SEARCH_TYPE = "search_type";//0 - simple search, 1 - advanced search

    static final String TITLE="title";
    static final String SUB_TITLE="sub_title";

    ActionBarDrawerToggle actionBarDrawerToggle;

    public DrawerLayout drawerLayout;
    public LeftMenu LeftMenu;
    public SimpleSearchFragment SimpleSearchView;
    public AdvancedSearchFragment AdvancedSearchView;
    public HelpFragment HelpView;

    SharedPreferences Preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.ActionToolbar);
        toolbar.setSubtitle(R.string.app_name);
        String title,subtitle;
        if( (title = getIntent(). getStringExtra(TITLE)) != null){
            toolbar.setTitle(title);
        }
        if( (subtitle = getIntent().getStringExtra(SUB_TITLE)) != null) {
            toolbar.setSubtitle(subtitle);
        }
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayoutMain);
        actionBarDrawerToggle =
                new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        Preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.left_menu ,LeftMenu = new LeftMenu(),LeftMenu.class.getSimpleName())
                    .commit();
            if(Preferences.getInt(Settings.FIRST_RUN,0)==0){
                Editor ed = Preferences.edit();
                ed.putInt(Settings.FIRST_RUN, 1);
                ed.apply();//аналог коммита. но работает в фоне
                // Первый запуск, открываем help
                if(getSupportActionBar() != null) {
                    int index = com.android.nik.timeline.LeftMenu.HELP_VIEW;
                    getSupportActionBar().setTitle(getResources().getStringArray(R.array.left_menu_list)[index]);
                }
                getFragmentManager().beginTransaction()
                        .add(R.id.container, HelpView = new HelpFragment(),HelpFragment.class.getSimpleName())
                        .commit();
                }
            else{
                // Рядовой(Обычный) запуск, прописываем фрагмент для обычного запуска.второй аргумент значение по умолчанию
                int SearchType;
                try {
                    SearchType = Integer.valueOf(Preferences.getString(Settings.SEARCH_TYPE,Settings.DEFAULT_SEARCH));
                }catch(NumberFormatException e){SearchType = Settings.SIMPLE_SEARCH;}

                if(SearchType == Settings.SIMPLE_SEARCH){
                    //запуск простого поиска
                    if(getSupportActionBar() != null) {
                        int index = com.android.nik.timeline.LeftMenu.SIMPLE_SEARCH_VIEW;
                        getSupportActionBar().setTitle(getResources().getStringArray(R.array.left_menu_list)[index]);
                    }
                    getFragmentManager().beginTransaction()
                            .add(R.id.container, SimpleSearchView = new SimpleSearchFragment(), SimpleSearchFragment.class.getSimpleName())
                            .commit();
                }
                else{
                    //запуск расширеннного поиска
                    if(getSupportActionBar() != null) {
                        int index = com.android.nik.timeline.LeftMenu.ADVANCED_SEARCH_VIEW;
                        getSupportActionBar().setTitle(getResources().getStringArray(R.array.left_menu_list)[index]);
                    }
                    getFragmentManager().beginTransaction()
                            .add(R.id.container, AdvancedSearchView = new AdvancedSearchFragment(), AdvancedSearchFragment.class.getSimpleName())
                            .commit();
                }
            }
        }
        else{
            Log.i("state", "Main is not null");
            LeftMenu = (LeftMenu)getFragmentManager().findFragmentByTag(LeftMenu.class.getSimpleName());
        }
    }

    @Override
    protected void onPause() {
        CharSequence tempTitle =  null;
        CharSequence tempSubTitle = null;
        if(getSupportActionBar() != null){
            tempTitle =  getSupportActionBar().getTitle();
            tempSubTitle = getSupportActionBar().getSubtitle();
        }
        String Title = (tempTitle != null) ? tempTitle.toString():getString(R.string.app_name);
        String SubTitle = (tempSubTitle != null) ? tempSubTitle.toString():getString(R.string.app_name);

        getIntent().putExtra(TITLE,Title);
        getIntent().putExtra(SUB_TITLE,SubTitle);
        super.onPause();
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

    public void LaunchScreenActivity(SearchContext Context){

        Intent ActivityIntent;
        Configuration configuration= getResources().getConfiguration();

        switch (configuration.screenLayout&Configuration.SCREENLAYOUT_SIZE_MASK) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                ActivityIntent = new Intent(this, ViewSmallScreenActivity.class);
                ActivityIntent.putExtra(this.getPackageName()+SearchContext.class.getSimpleName(), Context);
                startActivity(ActivityIntent);
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                ActivityIntent = new Intent(this, ViewLargeScreenActivity.class);
                ActivityIntent.putExtra(this.getPackageName()+SearchContext.class.getSimpleName(), Context);
                startActivity(ActivityIntent);
                break;
            default:
                break;
        }
    }

}
