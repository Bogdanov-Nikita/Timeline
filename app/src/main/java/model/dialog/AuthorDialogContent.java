package model.dialog;

import android.content.res.Resources;
import android.database.Cursor;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;

import com.android.nik.timeline.R;

import database.TimeLineDatabase;
import database.TimeLineDatabase.Constants;
/**
 * Created by Nik on 12.03.2015.
 * class for create interface elements Author's dialog.
 */
public class AuthorDialogContent extends DialogContent {

    AutoCompleteTextView author;

    public AuthorDialogContent(final Resources res,TimeLineDatabase db){
        super(res,db);
    }

    @Override
    public void init(View rootView) {

        Cursor cursor = db.query(
                Constants.Author.TABLE_NAME,
                new String[]{
                        Constants.Author.id,
                        Constants.Author.author},
                null, null, null, null, null);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                rootView.getContext(),
                android.R.layout.simple_list_item_1,
                cursor,
                new String[]{Constants.Author.author},
                new int[]{android.R.id.text1},
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        adapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                return cursor.getString(cursor.getColumnIndex(Constants.Author.author));
            }
        });

        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                if(constraint!=null){
                    String word = constraint.toString().trim();
                    if(!word.equals("")){
                        return db.query(
                                TimeLineDatabase.Constants.Author.TABLE_NAME,
                                new String[]{
                                        Constants.Author.id,
                                        Constants.Author.author},
                                Constants.Author.author + " LIKE ?",
                                new String[] {"%" + word + "%"}, null, null, null);
                    }else{return null;}
                }else{return null;}
            }
        });

        author = (AutoCompleteTextView)rootView.findViewById(R.id.AuthorCompleteText);
        author.setAdapter(adapter);
    }

    @Override
    public void set(long id) {
        Cursor cursor = db.queryAuthorByID(id);
        if(cursor != null){
            cursor.moveToFirst();
            author.setText(cursor.getString(
                    cursor.getColumnIndex(TimeLineDatabase.Constants.Author.author)));
            cursor.close();
        }
    }

    @Override
    public boolean add() {
        String AuthorStr = author.getText().toString().trim();
        if(!AuthorStr.equals("")){
            if(!db.queryAuthorByName(AuthorStr).moveToFirst()){//проверка на дублирующую запись
                db.addtoAutors(AuthorStr);
                return true;
            } else {
                author.setError(res.getString(R.string.error_exists));
                return false;
            }
        } else {
            author.setError(res.getString(R.string.error_empty));
            return false;
        }
    }

    @Override
    public boolean update(long id) {
        String AuthorStr = author.getText().toString().trim();
        if(!AuthorStr.equals("")) {
            if (!db.queryAuthorByName(AuthorStr).moveToFirst()) {//проверка на дублирующую запись
                db.updatetoAutor(id, author.getText().toString());
                return true;
            } else {
                author.setError(res.getString(R.string.error_exists));
                return false;
            }
        } else {
            author.setError(res.getString(R.string.error_empty));
            return false;
        }
    }
}
