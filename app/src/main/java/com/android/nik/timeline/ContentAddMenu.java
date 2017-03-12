package com.android.nik.timeline;

import model.dialog.DialogContext;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;




/**
 * A simple {@link Fragment} subclass.
 */
public class ContentAddMenu extends Fragment {


    public static final int EVENT_DIALOG = 0;
    public static final int ASSOCIATION_DIALOG = 1;
    public static final int PERSON_DIALOG = 2;
    public static final int AUTHOR_DIALOG = 3;
    public static final int CATEGORY_DIALOG = 4;
    public static final int GEO_DIALOG = 5;
    public static final int TIMELINE_DIALOG = 6;

    public ContentAddMenu() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_content_add_menu, container, false);

        ListView List = (ListView) rootView.findViewById(R.id.AddContentMenu);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        rootView.getContext(),
                        android.R.layout.simple_list_item_1,
                        getResources().getStringArray(R.array.add_content));
        List.setAdapter(adapter);
        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogContext dialogContext;
                Intent ActivityIntent = new Intent(getActivity(), DialogActivity.class);

                switch (position){
                    case EVENT_DIALOG:
                        dialogContext = new DialogContext(DialogContext.EVENT);
                        ActivityIntent.putExtra(
                                MainActivity.class.getPackage()+DialogContext.class.getSimpleName(),
                                dialogContext);
                        startActivity(ActivityIntent);
                        break;
                    case ASSOCIATION_DIALOG:
                        dialogContext = new DialogContext(DialogContext.ASSOCIATION);
                        ActivityIntent.putExtra(
                                MainActivity.class.getPackage()+DialogContext.class.getSimpleName(),
                                dialogContext);
                        startActivity(ActivityIntent);
                        break;
                    case PERSON_DIALOG:
                        dialogContext = new DialogContext(DialogContext.PERSON);
                        ActivityIntent.putExtra(
                                MainActivity.class.getPackage()+DialogContext.class.getSimpleName(),
                                dialogContext);
                        startActivity(ActivityIntent);
                        break;
                    case AUTHOR_DIALOG:
                        dialogContext = new DialogContext(DialogContext.AUTHOR);
                        ActivityIntent.putExtra(
                                MainActivity.class.getPackage()+DialogContext.class.getSimpleName(),
                                dialogContext);
                        startActivity(ActivityIntent);
                        break;
                    case CATEGORY_DIALOG:
                        dialogContext = new DialogContext(DialogContext.CATEGORY);
                        ActivityIntent.putExtra(
                                MainActivity.class.getPackage()+DialogContext.class.getSimpleName(),
                                dialogContext);
                        startActivity(ActivityIntent);
                        break;
                    case GEO_DIALOG:
                        dialogContext = new DialogContext(DialogContext.GEO);
                        ActivityIntent.putExtra(
                                MainActivity.class.getPackage()+DialogContext.class.getSimpleName(),
                                dialogContext);
                        startActivity(ActivityIntent);
                        break;
                    case TIMELINE_DIALOG:
                        dialogContext = new DialogContext(DialogContext.TIMELINE);
                        ActivityIntent.putExtra(
                                MainActivity.class.getPackage()+DialogContext.class.getSimpleName(),
                                dialogContext);
                        startActivity(ActivityIntent);
                        break;
                }
                onDrawerButtonClick();
            }
        });
        return rootView;
    }

    public void onDrawerButtonClick() {
        Configuration configuration= getResources().getConfiguration();
        switch (configuration.screenLayout&Configuration.SCREENLAYOUT_SIZE_MASK) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                ((ViewSmallScreenActivity)getActivity()).drawerLayout.closeDrawer(Gravity.START);
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                ((ViewLargeScreenActivity)getActivity()).drawerLayout.closeDrawer(Gravity.START);
                break;
            default:
                break;
        }
    }
}
