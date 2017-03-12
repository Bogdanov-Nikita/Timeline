package com.android.nik.timeline;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import model.Settings;

/**
 * Фрагмент "помощи", при первом запуске и по кнопке в меню
 */
public class HelpFragment extends Fragment{
	/**
	 */
	SharedPreferences Preferences;
	
	public HelpFragment() {
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		Preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        View rootView = inflater.inflate(R.layout.fragment_help, container, false);
        Button button = (Button)rootView.findViewById(R.id.StartUseButton);
        button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

                int SearchType;
                try {
                    SearchType = Integer.valueOf(Preferences.getString(Settings.SEARCH_TYPE,Settings.DEFAULT_SEARCH));
                }catch(NumberFormatException e){SearchType = Settings.SIMPLE_SEARCH;}

                if(SearchType == Settings.SIMPLE_SEARCH){
					getFragmentManager().beginTransaction()
					.replace(R.id.container,
							new SimpleSearchFragment(), 
							SimpleSearchFragment.class.getSimpleName())
					.commit();
				}else{
					getFragmentManager().beginTransaction()
					.replace(R.id.container,
							new AdvancedSearchFragment(), 
							AdvancedSearchFragment.class.getSimpleName())
					.commit();
				}
			}
		});        
        return rootView;
    }
}