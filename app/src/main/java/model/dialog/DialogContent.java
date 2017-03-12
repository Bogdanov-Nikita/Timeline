package model.dialog;

import android.content.res.Resources;
import android.database.Cursor;
import android.view.View;

import database.TimeLineDatabase;

/**
 * Created by Nik on 12.03.2015.
 * class for create interface elements dialog.
 */
public abstract class DialogContent {

    Resources res;
    TimeLineDatabase db;

    public DialogContent(final Resources res,TimeLineDatabase db){
        this.res = res;
        this.db = db;
    }

    abstract public void init(View rootView);
    abstract public void set(long id);
    abstract public boolean add();
    abstract public boolean update(long id);

}
