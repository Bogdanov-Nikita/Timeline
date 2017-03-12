package com.android.nik.timeline;

import model.Settings;
import model.graphics.DataContainer;
import model.graphics.GraphicsContext;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link android.app.Fragment} subclass.
 * 
 */
public class MapFragment extends Fragment implements DataContainer {

	public static  final int DEFAULT_INDEX = Settings.DEFAULT_INDEX;
	private static final String ARG_INDEX = "index";

	int index;
	GraphicsContext gContext;
	
	public MapFragment() {
		// Required empty public constructor
	}

	public static MapFragment newInstance(int index) {
		MapFragment fragment = new MapFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_INDEX, index);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			index = getArguments().getInt(ARG_INDEX);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		gContext = getActivity()
        .getIntent().
        getParcelableExtra(
				MainActivity.class.getPackage() +
				GraphicsContext.class.getSimpleName() +
				String.valueOf(index));
		View rootView = inflater.inflate(R.layout.fragment_map, container, false);
		return rootView;
	}

	@Override
	public void onUpdateData() {
	}

	@Override
	public void onLoadingData() {

	}

	@Override
	public void onPause() {
		getActivity()
        .getIntent()
        .putExtra(
				MainActivity.class.getPackage() +
				GraphicsContext.class.getSimpleName() +
				String.valueOf(index), gContext);
		super.onPause();
	}
}
