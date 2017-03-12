package com.android.nik.timeline;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Фрагмент бокового выплывающего слева меню
 */
public class LeftMenu extends Fragment {
	
	public static final int SIMPLE_SEARCH_VIEW = 0;
	public static final int ADVANCED_SEARCH_VIEW = 1;
	public static final int OPEN_LIST_VIEW = 2;
	public static final int DELETE_LIST_VIEW = 3;
	public static final int SETTINGS_VIEW = 4;
	public static final int HELP_VIEW = 5;
	public static final int ABOUT_VIEW = 6;

    public LeftMenu() {
    // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.left_menu, container, false);
        ListView menu = (ListView)rootView.findViewById(R.id.left_menu_list);

        ArrayAdapter<String> MenuListAdapter = new ArrayAdapter<>(
        		rootView.getContext(),
        		android.R.layout.simple_list_item_1,
        		getResources().getStringArray(R.array.left_menu_list));
        menu.setAdapter(MenuListAdapter);
        menu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case SIMPLE_SEARCH_VIEW:
                    setTitle(getResources().getStringArray(R.array.left_menu_list)[position]);
					getFragmentManager().beginTransaction()
					.replace(R.id.container,
							new SimpleSearchFragment(),
							SimpleSearchFragment.class.getSimpleName())
					.commit();
					break;
				case ADVANCED_SEARCH_VIEW:
					setTitle(getResources().getStringArray(R.array.left_menu_list)[position]);
					getFragmentManager().beginTransaction()
					.replace(R.id.container,
							new AdvancedSearchFragment(),
							AdvancedSearchFragment.class.getSimpleName())
					.commit();
					break;
				case OPEN_LIST_VIEW:
					setTitle(getResources().getStringArray(R.array.left_menu_list)[position]);
					getFragmentManager().beginTransaction()
					.replace(R.id.container,
							OpenDeleteFragment.newInstanseState(OpenDeleteFragment.OPEN)).commit();
					break;
				case DELETE_LIST_VIEW:
					setTitle(getResources().getStringArray(R.array.left_menu_list)[position]);
					getFragmentManager().beginTransaction()
					.replace(R.id.container,
							OpenDeleteFragment.newInstanseState(OpenDeleteFragment.DELETE)).commit();
					break;
                case SETTINGS_VIEW:
                    setTitle(getResources().getStringArray(R.array.left_menu_list)[position]);
                    getFragmentManager().beginTransaction()
                        .replace(R.id.container,
                                new SettingsFragment(),
                                SettingsFragment.class.getSimpleName())
                        .commit();
                    break;
				case HELP_VIEW:
					setTitle(getResources().getStringArray(R.array.left_menu_list)[position]);
					getFragmentManager().beginTransaction()
					.replace(R.id.container,
							new HelpFragment(), 
							HelpFragment.class.getSimpleName())
					.commit();
					break;
				case ABOUT_VIEW:
					setTitle(getResources().getStringArray(R.array.left_menu_list)[position]);
					getFragmentManager().beginTransaction()
					.replace(R.id.container,
							new AboutFragment(), 
							AboutFragment.class.getSimpleName())
					.commit();
					break;
				}
                if(position >= 0) {
                    ((MainActivity) getActivity()).drawerLayout.closeDrawer(Gravity.START);
                }
			}
        	
		});
        return rootView;
    }

    void setTitle(String Title){
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(Title);
    }

}

