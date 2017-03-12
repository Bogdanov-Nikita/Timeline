package com.android.nik.timeline;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import database.TimeLineDatabase;
import model.dialog.AssociationDialogContent;
import model.dialog.AuthorDialogContent;
import model.dialog.CategoryDialogContent;
import model.dialog.DialogContent;
import model.dialog.DialogContext;
import model.dialog.EventDialogContent;
import model.dialog.GeoDialogContent;
import model.dialog.LinksDialogContent;
import model.dialog.PersonDialogContent;
import model.dialog.TimelineDialogContent;


public class DialogFragment extends Fragment {

    public static final String PREFIX = MainActivity.class.getPackage()+DialogFragment.class.getSimpleName();
    public static final String ACTION = PREFIX+"_action";
    public static final String TYPE = PREFIX+"_type";
	public static final String ID = PREFIX +"_id";

    TimeLineDatabase db;
    Button OkButton = null;
    Button CanselButton = null;

    //контейнер храняший состояние для каждого диалога .
    DialogContent content;//хранящий сушности Spinner,EditText и прочие для диалогов.

    public static DialogFragment newInstanseState(int action,int type,long id){
        DialogFragment fragment = new DialogFragment();
        Bundle args = new Bundle();
        args.putInt(ACTION,action);
        args.putInt(TYPE,type);
        args.putLong(ID,id);
        fragment.setArguments(args);
        return fragment;
    }

    public DialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int action = getArguments().getInt(ACTION);
        long id = getArguments().getLong(ID);///ошибка приведения типа!?
        // java.lang.ClassCastException: java.lang.Long cannot be cast to java.lang.Integer
        //возможно причина в непровильном обзывании ID по дороге.
        // между DialogFragment и DialogActivity

        db = new TimeLineDatabase(getActivity().getApplicationContext());

        View rootView = super.onCreateView(inflater,container,savedInstanceState);

        switch (getArguments().getInt(TYPE)){
            case DialogContext.EVENT:
                rootView = initDialog(inflater,container,R.layout.fragment_dialog_event,R.id.SaveButton,R.id.CancelButton);
                content = new EventDialogContent(getResources(),db);
                break;
            case DialogContext.EVENT_LINKS:
                rootView = initDialog(inflater,container,R.layout.fragment_dialog_event_links,R.id.SaveButton,R.id.CancelButton);
                content = new LinksDialogContent(getResources(),db);
                break;
            case DialogContext.ASSOCIATION:
                rootView = initDialog(inflater,container,R.layout.fragment_dialog_association,R.id.SaveButton,R.id.CancelButton);
                content = new AssociationDialogContent(getResources(),db);
                break;
            case DialogContext.PERSON:
                rootView = initDialog(inflater,container,R.layout.fragment_dialog_person,R.id.SaveButton,R.id.CancelButton);
                content = new PersonDialogContent(getResources(),db);
                break;
            case DialogContext.AUTHOR:
                rootView = initDialog(inflater,container,R.layout.fragment_dialog_author,R.id.SaveButton,R.id.CancelButton);
                content = new AuthorDialogContent(getResources(),db);
                break;
            case DialogContext.CATEGORY:
                rootView = initDialog(inflater,container,R.layout.fragment_dialog_category,R.id.SaveButton,R.id.CancelButton);
                content = new CategoryDialogContent(getResources(),db);
                break;
            case DialogContext.GEO:
                rootView = initDialog(inflater,container,R.layout.fragment_dialog_geo,R.id.SaveButton,R.id.CancelButton);
                content = new GeoDialogContent(getResources(),db);
                break;
            case DialogContext.TIMELINE:
                rootView = initDialog(inflater,container,R.layout.fragment_dialog_timeline,R.id.SaveButton,R.id.CancelButton);
                content = new TimelineDialogContent(getResources(),db);
                break;
            default:
                content = null;
                break;
        }

        if(content != null) {
            content.init(rootView);
            if (action == DialogContext.UPDATE) {
                content.set(id);
            }
        }

        return rootView;
    }

    View initDialog(LayoutInflater inflater,ViewGroup container,final int layoutId,final int SaveButtonId,final int CanselButtonId){
        View rootView = inflater.inflate(layoutId,container, false);
        OkButton = (Button)rootView.findViewById(SaveButtonId);
        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OKButtonClickListener();
            }
        });
        CanselButton = (Button)rootView.findViewById(CanselButtonId);
        CanselButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelButtonClickListener();
            }
        });
        return rootView;
    }

    boolean AddEvent(){
        return content != null && content.add();
    }

    boolean UpdateEvent(){
        return content != null && content.update(getArguments().getLong(ID));
    }

    void OKButtonClickListener(){
        boolean access = false;
        switch (getArguments().getInt(ACTION)){
            case DialogContext.ADD:
                access = AddEvent();
                break;
            case DialogContext.UPDATE:
                access = UpdateEvent();
                break;
        }
        if(access) {
            db.close();
            this.getActivity().onBackPressed();
        }
    }

    void CancelButtonClickListener(){
        this.getActivity().onBackPressed();
    }

    @Override
    public void onPause() {
        db.close();
        super.onPause();
    }
}
