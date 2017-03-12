package com.android.nik.timeline;

import database.TimeLineDatabase;
import database.TimeLineDatabase.Constants;
import model.Settings;
import model.dialog.DialogContext;
import model.graphics.CompositionView;
import model.graphics.DataContainer;
import model.search.TimeListItem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.SimpleCursorAdapter.ViewBinder;

/**
 * Фрагмент бокового меню справа с количеством открытых хронологических рядов.(content' ов)
 */
public class RightMenu extends Fragment {

    public static final String PREFIX = MainActivity.class.getPackage()+RightMenu.class.getSimpleName();
    public static final String STATE_LIST = PREFIX + "_list";

	ListView menu = null;
    Cursor cursor = null;
    SimpleCursorAdapter Adapter = null;
    TimeLineDatabase db;

	public RightMenu() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		View rootView = inflater.inflate(R.layout.right_menu, container, false);
		menu = (ListView)rootView.findViewById(R.id.right_menu_list);
		db = new TimeLineDatabase(getActivity().getApplicationContext());
		cursor = db.queryFromTimeLists(new int []{TimeListItem.VISIBLE,TimeListItem.TEMPORARY});
		Adapter = new SimpleCursorAdapter(
                rootView.getContext(),
    			R.layout.right_menu_item,
    			cursor, 
    			new String[]{
                        Constants.TimeLists.id,
                        Constants.TimeLists.color,
                        Constants.TimeLists.title
                },
    			new int[]{
                        R.id.Close,
                        R.id.TimelineColor,
                        R.id.TimelineTitle
                },
    			SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		Adapter.setViewBinder(new ViewBinder() {

            @Override
            public boolean setViewValue(View view, final Cursor cursor, int columnIndex) {
                boolean out_flag = false;
                switch (view.getId()) {
                    case R.id.TimelineColor:
                        view.setBackgroundColor(cursor.getInt(columnIndex));
                        out_flag = true;
                        break;
                    case R.id.TimelineTitle:
                        TextView Title = ((TextView) view);
                        Title.setText(cursor.getString(columnIndex));
                        final int  position = cursor.getInt(cursor.getColumnIndex(Constants.TimeLists.id));
                        Title.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // действие на редактирование записи
                                DialogContext dialogContext;
                                Intent ActivityIntent = new Intent(getActivity(), DialogActivity.class);
                                Log.i("RightMenu","position(id)="+position);
                                dialogContext = new DialogContext(DialogContext.TIMELINE,position);
                                ActivityIntent.putExtra(
                                        MainActivity.class.getPackage()+DialogContext.class.getSimpleName(),
                                        dialogContext);
                                startActivity(ActivityIntent);
                            }
                        });
                        out_flag =  true;
                        break;
                    case R.id.Close:
                        final int  ClosePosition = cursor.getInt(columnIndex);
                        ImageButton button = (ImageButton)view;
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // действие на закрытие элемента из TimeList.
                                // временный TimeList создаёться как постоянный его можно удалить
                                // через меню удаления.
                                db.beginTransaction();
                                try {
                                    db.updatetoTimeListsVisable(ClosePosition, false);
                                    db.setTransactionSuccessful();
                                }finally {
                                    db.endTransaction();
                                }
                                Adapter.swapCursor(
                                        db.queryFromTimeLists(new int []{TimeListItem.VISIBLE,TimeListItem.TEMPORARY}))
                                        .close();
                                Adapter.notifyDataSetChanged();
                                onDrawerButtonClick();
                            }
                        });
                        out_flag = true;
                        break;
                    case R.layout.right_menu_item:
                        //что бы небыло бага из адаптера. стандартно для всех курсорных адаптеров
                        out_flag = true ;
                        break;
                }
                return out_flag;
            }
        });
		menu.setAdapter(Adapter);

		Parcelable p =  getContainer().getParcelableExtra(STATE_LIST);
        if(p != null){
            menu.onRestoreInstanceState(p);
        }
        Log.i("state", "Rig menu onCreateView ");
        return rootView;
	}

    @Override
    public void onResume() {
        Log.i("state", "Rig menu onResume "+(menu!=null));
        //db = new TimeLineDatabase(getActivity().getApplicationContext());
        cursor = db.queryFromTimeLists(new int []{TimeListItem.VISIBLE,TimeListItem.TEMPORARY});
        Adapter.swapCursor(cursor);
        Adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        getContainer().putExtra(STATE_LIST, menu.onSaveInstanceState());
        Log.i("state", "Rig menu onPause ");
        cursor.close();
        //db.close();
    }

    public void onDrawerButtonClick() {

        String Tags[]={
                LineViewFragment.class.getSimpleName(),
                GridViewFragment.class.getSimpleName(),
                SwipeListFragment.class.getSimpleName() + String.valueOf(SwipeListFragment.GALLERY_TYPE),
                SwipeListFragment.class.getSimpleName() + String.valueOf(SwipeListFragment.LIST_TYPE),
                SwipeListFragment.class.getSimpleName() + String.valueOf(SwipeListFragment.EVENT_TYPE),
                MapFragment.class.getSimpleName()};
        int position;
        Configuration configuration= getResources().getConfiguration();
        switch (configuration.screenLayout&Configuration.SCREENLAYOUT_SIZE_MASK) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                position = ((Spinner)getActivity().findViewById(R.id.ToolbarSpinner))
                        .getSelectedItemPosition();
                    ((DataContainer) getFragmentManager()
                            .findFragmentByTag(Tags[position]))
                            .onUpdateData();
                /*не удобно, оставить как возможность, но мало вероятно использование
                ((ViewSmallScreenActivity)getActivity()).drawerLayout.closeDrawer(Gravity.END);
                */
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                int SpinnerId[]= CompositionView.WindowSpinnerId;/*{
                        R.id.WindowSpinner_1,
                        R.id.WindowSpinner_2,
                        R.id.WindowSpinner_3,
                        R.id.WindowSpinner_4};*/
                SharedPreferences Preferences = PreferenceManager
                    .getDefaultSharedPreferences(getActivity().getBaseContext());
                int window;
                try {
                    window = Integer.valueOf(Preferences.getString(Settings.WINDOWS, Settings.DEFAULT_WINDOWS));
                }catch(NumberFormatException e){window = Settings.TWO_WINDOWS;}
                for(int number = 0; number < window; number++) {
                    position = ((Spinner)getActivity().findViewById(SpinnerId[number]))
                            .getSelectedItemPosition();
                        ((DataContainer) getFragmentManager()
                                .findFragmentByTag(Tags[position] + String.valueOf(number)))
                                .onUpdateData();
                }
                /*не удобно, оставить как возможность, но мало вероятно использование
                ((ViewLargeScreenActivity)getActivity()).drawerLayout.closeDrawer(Gravity.END);
                */
                break;
            default:
                break;
        }
    }

    Intent getContainer(){
        return getActivity().getIntent();
    }
}
