package model.dialog;

import android.content.res.Resources;
import android.database.Cursor;
import android.view.View;
import android.widget.EditText;

import com.android.nik.timeline.R;

import database.TimeLineDatabase;

/**
 * Created by Nik on 03.04.2015.
 * class for create interface elements Link's dialog.
 */
public class LinksDialogContent extends DialogContent{
    //мало вероятно что придётся добавить позиции и элементы так как этот диалог редко вызываем
    //только при необходимости.
    EditText Links;

    public LinksDialogContent(Resources res, TimeLineDatabase db) {
        super(res, db);
    }

    @Override
    public void init(View rootView) {
        Links = (EditText)rootView.findViewById(R.id.EditLinks);
    }

    @Override
    public void set(long id) {
        Cursor LinksCursor = db.queryLinksByID(id);
        if(LinksCursor != null){
            if(LinksCursor.moveToFirst()) {
                String links = "";
                for (; !LinksCursor.isAfterLast(); LinksCursor.moveToNext()) {
                    links = links + LinksCursor.getString(LinksCursor.getColumnIndex(TimeLineDatabase.Constants.Links.link)) + ", ";
                }
                Links.setText(links);
            }
        }
    }

    @Override
    public boolean add() {
        return false;
    }

    @Override
    public boolean update(long id) {
        //Update links
        String LinksStr = Links.getText().toString().trim();
        if(!LinksStr.equals("")){
            boolean EmptyFlag = true;
            String LinkArray[] = LinksStr.split(",");
            for (String link : LinkArray) {
                if (!link.trim().equals("")) {
                    EmptyFlag = false;
                }
            }
            if(EmptyFlag){
                Links.setError(res.getString(R.string.error_empty));
                return false;
            }else{
                //удаляем старое
                db.deleteLinksByID(id);
                //добавляем новое по элементу.
                for (String link : LinkArray) {
                    String TempStr = link.trim();
                    if (!TempStr.equals("")) {
                        db.addtoLinks(id, TempStr);
                    }
                }
                return true;
            }
        }else{
            Links.setError(res.getString(R.string.error_empty));
            return false;
        }
    }
}
