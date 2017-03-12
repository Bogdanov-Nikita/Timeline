package com.android.nik.timeline;

import java.util.Locale;

import database.TimeLineDatabase;
import database.TimeLineDatabase.Constants;
import model.Settings;
import model.graphics.DataContainer;
import model.graphics.GraphicsContext;
import model.search.TimeListItem;

import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.database.Cursor;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// #33b5e5 - синий.

public class SwipeListFragment extends Fragment implements DataContainer{

    public static final String PREFIX = MainActivity.class.getPackage()+SwipeListFragment.class.getSimpleName();
    public static final String TYPE_OF_FRAGMENT=PREFIX+"_type_of_fragment";
    public static final int LIST_TYPE = 1;
    public static final int GALLERY_TYPE = 2;
    public static final int EVENT_TYPE = 3;

    public static final int LIST_BG_COLOR = 0xFF858585;
    public static final int GALLERY_BG_COLOR = 0xFF000000;
    public static final int EVENT_BG_COLOR = 0xFF33b5e5;//В будущем возможно что таймлайн задаст этот цвет.
    public static final int TEXT_COLOR = 0xFFFFFFFF;

    public static  final int DEFAULT_INDEX = Settings.DEFAULT_INDEX;
    private static final String ARG_INDEX = "index";

    int index;
    GraphicsContext gContext;

    int type;
    ViewPager mViewPager;
    PagerTitleStrip Title;
    SectionsPagerAdapter mSectionsPagerAdapter;

    public SwipeListFragment() {
        // Required empty public constructor
    }
    //newInstance
    public static SwipeListFragment newInstance(int FragmentType, int index){
        SwipeListFragment fragment = new SwipeListFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE_OF_FRAGMENT,FragmentType);
        args.putInt(ARG_INDEX,index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(TYPE_OF_FRAGMENT);
            index = getArguments().getInt(ARG_INDEX);
        }
    }

    /*программное перелистывание ViewPager.setCurrentItem, getCurrentItem - текущая страница*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        int LayoutID;
        int PagerTitleID;
        int ListPagerID;
        switch (index + 1){
            case Settings.ONE_WINDOW:
                LayoutID = R.layout.fragment_swipe_list_1;
                PagerTitleID = R.id.pager_title_strip_1;
                ListPagerID = R.id.list_pager_1;
                break;
            case Settings.TWO_WINDOWS:
                LayoutID = R.layout.fragment_swipe_list_2;
                PagerTitleID = R.id.pager_title_strip_2;
                ListPagerID = R.id.list_pager_2;
                break;
            case Settings.THREE_WINDOWS:
                LayoutID = R.layout.fragment_swipe_list_3;
                PagerTitleID = R.id.pager_title_strip_3;
                ListPagerID = R.id.list_pager_3;
                break;
            case Settings.FOUR_WINDOWS:
                LayoutID = R.layout.fragment_swipe_list_4;
                PagerTitleID = R.id.pager_title_strip_4;
                ListPagerID = R.id.list_pager_4;
                break;
            default:
                LayoutID = R.layout.fragment_swipe_list_1;
                PagerTitleID = R.id.pager_title_strip_1;
                ListPagerID = R.id.list_pager_1;
                break;
        }
        View rootView = inflater.inflate(LayoutID, container, false);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        gContext = getActivity().getIntent().getParcelableExtra(
                MainActivity.class.getPackage() +
                GraphicsContext.class.getSimpleName() +
                String.valueOf(index));
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        Title = (PagerTitleStrip)rootView.findViewById(PagerTitleID);
        mViewPager = (ViewPager) rootView.findViewById(ListPagerID);

        switch (type) {
            case LIST_TYPE:
                Title.setTextColor(TEXT_COLOR);
                Title.setBackgroundColor(LIST_BG_COLOR);
                if(gContext.getListSwipeView().size() != mSectionsPagerAdapter.getCount()){
                    int zeros = mSectionsPagerAdapter.getCount() - gContext.getListSwipeView().size();
                    gContext.getListSwipeView().addZeros(zeros);
                }
                break;
            case GALLERY_TYPE:
                Title.setTextColor(TEXT_COLOR);
                Title.setBackgroundColor(GALLERY_BG_COLOR);
                if(gContext.getGallerySwipeView().size() != mSectionsPagerAdapter.getCount()){
                    int zeros = mSectionsPagerAdapter.getCount() - gContext.getGallerySwipeView().size();
                    gContext.getGallerySwipeView().addZeros(zeros);
                }
                break;
            case EVENT_TYPE:
                Title.setTextColor(TEXT_COLOR);
                Title.setBackgroundColor(EVENT_BG_COLOR);
                if(gContext.getEventSwipeView().size() != mSectionsPagerAdapter.getCount()){
                    int zeros = mSectionsPagerAdapter.getCount() - gContext.getEventSwipeView().size();
                    gContext.getEventSwipeView().addZeros(zeros);
                }
                break;
        }

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                switch (type) {
                    case LIST_TYPE:
                        gContext.getListSwipeView().setPage(mViewPager.getCurrentItem());
                        break;
                    case GALLERY_TYPE:
                        gContext.getGallerySwipeView().setPage(mViewPager.getCurrentItem());
                        break;
                    case EVENT_TYPE:
                        gContext.getEventSwipeView().setPage(mViewPager.getCurrentItem());
                        break;
                }
                getActivity().getIntent().putExtra(
                        MainActivity.class.getPackage() +
                                GraphicsContext.class.getSimpleName() +
                                String.valueOf(index), gContext);
            }

            @Override
            public void onPageScrolled(int position0, float arg1, int position1) {/* не нужен*/}

            @Override
            public void onPageScrollStateChanged(int arg0) {/*не нужен*/}
        });
        switch (type) {
            case LIST_TYPE:
                mViewPager.setCurrentItem(gContext.getListSwipeView().getPage());
                break;
            case GALLERY_TYPE:
                mViewPager.setCurrentItem(gContext.getGallerySwipeView().getPage());
                break;
            case EVENT_TYPE:
                mViewPager.setCurrentItem(gContext.getEventSwipeView().getPage());
                break;
        }
        return rootView;
    }

    @Override
    public void onUpdateData() {
        //порядок очень важен!
        int page = mViewPager.getCurrentItem();
        mSectionsPagerAdapter.UpdateCounter();
        mSectionsPagerAdapter.notifyDataSetChanged();
        int count = mSectionsPagerAdapter.getCount();
        if(page == count){
            page = (count>0)?(count-1):0;
            switch (type) {
                case LIST_TYPE:
                    gContext.getListSwipeView().setPage(page);
                    break;
                case GALLERY_TYPE:
                    gContext.getGallerySwipeView().setPage(page);
                    break;
                case EVENT_TYPE:
                    gContext.getEventSwipeView().setPage(page);
                    break;
            }
        }
        mViewPager.setCurrentItem(page);
    }

    @Override
    public void onLoadingData() {

    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        int count[];

        public SectionsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            count = new int[3];
            TimeLineDatabase db = new TimeLineDatabase(getActivity().getApplicationContext());
            Cursor cursor;

            count[0]=10;

            cursor = db.queryFromTimeLists(new int[]{TimeListItem.VISIBLE, TimeListItem.TEMPORARY});
            count[1] = (cursor != null)? cursor.getCount() : 0;

            Cursor IdCursor = db.queryFromTimeLists(new int[]{TimeListItem.VISIBLE,TimeListItem.TEMPORARY});
            IdCursor.moveToFirst();
            long id[] = new long [IdCursor.getCount()];
            for(int i=0;!IdCursor.isAfterLast();IdCursor.moveToNext(),i++){
                id[i]=IdCursor.getLong(IdCursor.getColumnIndex(Constants.TimeLists.id));
            }
            IdCursor.close();
            cursor = db.queryEventByTimeListsMulti(id, false, false, false, false);
            count[2] = (cursor != null)? cursor.getCount() : 0;

            if(cursor != null){cursor.close();}
            db.close();
        }

        void UpdateCounter(){
            TimeLineDatabase db = new TimeLineDatabase(getActivity().getApplicationContext());
            Cursor cursor = null;
            switch (type) {
                case LIST_TYPE:
                    count[0]=10;
                    break;
                case GALLERY_TYPE:
                    cursor = db.queryFromTimeLists(new int[]{TimeListItem.VISIBLE, TimeListItem.TEMPORARY});
                    count[1] = (cursor != null)? cursor.getCount() : 0;
                    break;
                case EVENT_TYPE:
                    Cursor IdCursor = db.queryFromTimeLists(new int[]{TimeListItem.VISIBLE,TimeListItem.TEMPORARY});
                    IdCursor.moveToFirst();
                    long id[] = new long [IdCursor.getCount()];
                    for(int i=0;!IdCursor.isAfterLast();IdCursor.moveToNext(),i++){
                        id[i]=IdCursor.getLong(IdCursor.getColumnIndex(Constants.TimeLists.id));
                    }
                    IdCursor.close();
                    cursor = db.queryEventByTimeListsMulti(id, false, false, false, false);
                    count[2] = (cursor != null)? cursor.getCount() : 0;
                    break;
            }
            if(cursor != null){cursor.close();}
            db.close();
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a Fragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment = null;
            switch (type) {
                case LIST_TYPE:
                    fragment = SwipeListItemFragment.newInstance(position + 1, index);
                    break;
                case GALLERY_TYPE:
                    fragment = SwipeGalleryItemFragment.newInstance(position + 1, index);
                    break;
                case EVENT_TYPE:
                    fragment = SwipeEventItemFragment.newInstance(position + 1);
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show all pages.
            switch(type){
                case LIST_TYPE:
                    return count[0];
                case GALLERY_TYPE:
                    return count[1];
                case EVENT_TYPE:
                    return count[2];
            }
            return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            String Title = ("Default "+(position+1)).toUpperCase(l);//if default
            switch (type) {
                case LIST_TYPE:
                    Title = ("Title "+(position+1)).toUpperCase(l);
                    break;
                case GALLERY_TYPE:
                    TimeLineDatabase db = new TimeLineDatabase(getActivity().getApplicationContext());
                    Cursor cursor = db.queryFromTimeLists(new int []{TimeListItem.VISIBLE,TimeListItem.TEMPORARY});
                    cursor.moveToPosition(position);
                    Title = cursor.getString(cursor.getColumnIndex(Constants.TimeLists.title)).toUpperCase(l);
                    cursor.close();
                    db.close();
                    break;
                case EVENT_TYPE:
                    Title = (""+(position+1)+"/"+getCount()).toUpperCase(l);
                    break;
            }
            return  Title;
        }
    }
    @Override
    public void onPause() {
        getActivity().getIntent().putExtra(
                MainActivity.class.getPackage() +
                GraphicsContext.class.getSimpleName() +
                String.valueOf(index),gContext);
        super.onPause();
    }
}
