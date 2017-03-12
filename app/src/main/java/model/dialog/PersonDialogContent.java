package model.dialog;

import android.content.res.Resources;
import android.database.Cursor;
import android.view.View;
import android.widget.EditText;

import com.android.nik.timeline.R;

import database.TimeLineDatabase;

/**
 * Created by Nik on 20.03.2015.
 * class for create interface elements Person's dialog.
 */
public class PersonDialogContent extends DialogContent{

    EditText Name;
    EditText Birth;
    EditText Death;

    public PersonDialogContent(Resources res, TimeLineDatabase db) {
        super(res, db);
    }

    @Override
    public void init(View rootView) {
        Name = (EditText)rootView.findViewById(R.id.EditName);
        Birth = (EditText)rootView.findViewById(R.id.EditBirth);
        Death = (EditText)rootView.findViewById(R.id.EditDeath);
    }

    @Override
    public void set(long id) {
        Cursor cursor = db.queryPersonByID(id);
        if(cursor != null ){
            cursor.moveToFirst();
            Name.setText(cursor.getString(cursor.getColumnIndex(TimeLineDatabase.Constants.Person.person)));
            Birth.setText(cursor.getString(cursor.getColumnIndex(TimeLineDatabase.Constants.Person.start_date)));
            Death.setText(cursor.getString(cursor.getColumnIndex(TimeLineDatabase.Constants.Person.end_date)));
            cursor.close();
        }
    }

    @Override
    public boolean add() {
        boolean breakFlag = false;
        String NameStr = Name.getText().toString().trim();
        String BirthStr = Birth.getText().toString().trim();
        String DeathStr = Death.getText().toString().trim();

        if(NameStr.equals("")){
            Name.setError(res.getString(R.string.error_empty));
            breakFlag = true;
        }
        if(BirthStr.equals("")){
            Birth.setError(res.getString(R.string.error_empty));
            breakFlag = true;
        }
        /*if(DeathStr.equals("")){//если живы то даты нету продумать вопрос и о тех у ког нет даты рождения точной.
            Death.setError(res.getString(R.string.error_empty));
            breakFlag = true;
        }*/
        if(!breakFlag){
            if(!db.queryPersonByName(NameStr).moveToFirst()){
                db.addtoPersons(NameStr,BirthStr,DeathStr);
            }else{
                Name.setError(res.getString(R.string.error_exists));
                breakFlag = true;
            }
        }
        return !breakFlag;
    }

    @Override
    public boolean update(long id) {
        boolean breakFlag = false;
        String NameStr = Name.getText().toString().trim();
        String BirthStr = Birth.getText().toString().trim();
        String DeathStr = Death.getText().toString().trim();

        if(NameStr.equals("")){
            Name.setError(res.getString(R.string.error_empty));
            breakFlag = true;
        }
        if(BirthStr.equals("")){
            Birth.setError(res.getString(R.string.error_empty));
            breakFlag = true;
        }
        if(DeathStr.equals("")){
            Death.setError(res.getString(R.string.error_empty));
            breakFlag = true;
        }
        if(!breakFlag){
            if(!db.queryPersonByName(NameStr).moveToFirst()){
                db.updatetoPerson(id,NameStr,BirthStr,DeathStr);
            }else{
                Name.setError(res.getString(R.string.error_exists));
                breakFlag = true;
            }
        }
        return !breakFlag;
    }
}
