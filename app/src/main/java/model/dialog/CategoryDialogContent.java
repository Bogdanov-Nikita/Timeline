package model.dialog;

import android.content.res.Resources;
import android.database.Cursor;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;

import com.android.nik.timeline.R;

import database.TimeLineDatabase;

/**
 * Created by Nik on 12.03.2015.
 * class for create interface elements Category's dialog.
 */
public class CategoryDialogContent extends DialogContent{

    AutoCompleteTextView category;

    public CategoryDialogContent(final Resources res,TimeLineDatabase db){
        super(res,db);
    }

    @Override
    public void init(View rootView) {
        Cursor cursor = db.query(
                TimeLineDatabase.Constants.Category.TABLE_NAME,
                new String[]{
                        TimeLineDatabase.Constants.Category.id,
                        TimeLineDatabase.Constants.Category.name},
                null, null, null, null, null);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                rootView.getContext(),
                android.R.layout.simple_list_item_1,
                cursor,
                new String[]{TimeLineDatabase.Constants.Category.name},
                new int[]{android.R.id.text1},
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        adapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                return cursor.getString(cursor.getColumnIndex(TimeLineDatabase.Constants.Category.name));
            }
        });

        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                if(constraint!=null){
                    String word = constraint.toString().trim();
                    if(!word.equals("")){
                        return db.query(
                                TimeLineDatabase.Constants.Category.TABLE_NAME,
                                new String[]{
                                        TimeLineDatabase.Constants.Category.id,
                                        TimeLineDatabase.Constants.Category.name},
                                TimeLineDatabase.Constants.Category.name + " LIKE ?",
                                new String[] {word + "%"}, null, null, null);
                    }else{return null;}
                }else{return null;}
            }
        });
        category = (AutoCompleteTextView)rootView.findViewById(R.id.CategoryCompleteText);
        category.setThreshold(1);
        category.setAdapter(adapter);
    }

    @Override
    public void set(long id) {
        Cursor cursor = db.queryCategoryByID(id);
        if(cursor != null ){
            cursor.moveToFirst();
            category.setText(cursor.getString(
                    cursor.getColumnIndex(TimeLineDatabase.Constants.Category.name)));
            cursor.close();
        }
    }

    @Override
    public boolean add() {
        String CategoryStr = category.getText().toString().trim();
        if(!CategoryStr.equals("")) {
            if (!db.queryCategoryByName(CategoryStr).moveToFirst()) {
                db.addtoCategory(CategoryStr);
                return true;
            } else {
                category.setError(res.getString(R.string.error_exists));
                return false;
            }
        } else {
            category.setError(res.getString(R.string.error_empty));
            return false;
        }
    }

    @Override
    public boolean update(long id) {
        String CategoryStr = category.getText().toString().trim();
        if(!CategoryStr.equals("")) {
            if (!db.queryCategoryByName(CategoryStr).moveToFirst()) {
                db.updatetoCategory(id,CategoryStr);
                return true;
            } else {
                category.setError(res.getString(R.string.error_exists));
                return false;
            }
        } else {
            category.setError(res.getString(R.string.error_empty));
            return false;
        }
    }

}
