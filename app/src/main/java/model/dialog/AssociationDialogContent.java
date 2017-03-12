package model.dialog;

import android.content.res.Resources;
import android.database.Cursor;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.android.nik.timeline.R;

import database.TimeLineDatabase;

/**
 * Created by Nik on 19.03.2015.
 * class for create interface elements Association's dialog.
 */
public class AssociationDialogContent extends DialogContent{

    Spinner privacy;
    EditText Connection;
    MultiAutoCompleteTextView Events;
    AutoCompleteTextView Author;

    public AssociationDialogContent(Resources res, TimeLineDatabase db) {
        super(res, db);
    }

    @Override
    public void init(View rootView) {

        privacy = (Spinner)rootView.findViewById(R.id.privacy);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                rootView.getContext(),
                android.R.layout.simple_list_item_1,
                res.getStringArray(R.array.dialog_association_privacy_list));
        privacy.setAdapter(adapter);
        Connection = (EditText)rootView.findViewById(R.id.EditConnection);

        Cursor cursor = db.query(
                TimeLineDatabase.Constants.Event.TABLE_NAME,
                new String[]{
                        TimeLineDatabase.Constants.Event.id,
                        TimeLineDatabase.Constants.Event.title,
                        TimeLineDatabase.Constants.Event.start_date,
                        TimeLineDatabase.Constants.Event.end_date},
                null, null, null, null, null);

        SimpleCursorAdapter adapter2 = new SimpleCursorAdapter(
                rootView.getContext(),
                android.R.layout.simple_list_item_1,
                cursor,
                new String[]{TimeLineDatabase.Constants.Event.title},
                new int[]{android.R.id.text1},
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        adapter2.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                return cursor.getString(cursor.getColumnIndex(TimeLineDatabase.Constants.Event.title));
            }
        });

        adapter2.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                if(constraint!=null){
                    String word = constraint.toString().trim();
                    if(!word.equals("")){
                        return db.query(
                                TimeLineDatabase.Constants.Event.TABLE_NAME,
                                new String[]{
                                        TimeLineDatabase.Constants.Event.id,
                                        TimeLineDatabase.Constants.Event.title,
                                        TimeLineDatabase.Constants.Event.start_date,
                                        TimeLineDatabase.Constants.Event.end_date},
                                TimeLineDatabase.Constants.Event.title + " LIKE ?",
                                new String[] {word + "%"}, null, null, null);
                    }else{return null;}
                }else{return null;}
            }
        });

        Events = (MultiAutoCompleteTextView)rootView.findViewById(R.id.EventsAssociationCompleteText);
        Events.setThreshold(1);
        Events.setAdapter(adapter2);

        Cursor cursor2 = db.query(
                TimeLineDatabase.Constants.Author.TABLE_NAME,
                new String[]{
                        TimeLineDatabase.Constants.Author.id,
                        TimeLineDatabase.Constants.Author.author},
                null, null, null, null, null);

        SimpleCursorAdapter adapter3 = new SimpleCursorAdapter(
                rootView.getContext(),
                android.R.layout.simple_list_item_1,
                cursor2,
                new String[]{TimeLineDatabase.Constants.Author.author},
                new int[]{android.R.id.text1},
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        adapter3.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                return cursor.getString(cursor.getColumnIndex(TimeLineDatabase.Constants.Author.author));
            }
        });

        adapter3.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                if(constraint!=null){
                    String word = constraint.toString().trim();
                    if(!word.equals("")){
                        return db.query(
                                TimeLineDatabase.Constants.Author.TABLE_NAME,
                                new String[]{
                                        TimeLineDatabase.Constants.Author.id,
                                        TimeLineDatabase.Constants.Author.author},
                                TimeLineDatabase.Constants.Author.author + " LIKE ?",
                                new String[] {"%" + word + "%"}, null, null, null);
                    }else{return null;}
                }else{return null;}
            }
        });

        Author = (AutoCompleteTextView)rootView.findViewById(R.id.AuthorCompleteText);
        Author.setAdapter(adapter3);
    }

    @Override
    public void set(long id) {
        Cursor cursor = db.queryAssociationByID(id);
        cursor.moveToFirst();
        long event_id_1 = cursor.getInt(cursor.getColumnIndex(TimeLineDatabase.Constants.Association.event_id_1));
        long event_id_2 = cursor.getInt(cursor.getColumnIndex(TimeLineDatabase.Constants.Association.event_id_2));
        Connection.setText(cursor.getString(cursor.getColumnIndex(TimeLineDatabase.Constants.Association.connection)));
        long author_id = cursor.getInt(cursor.getColumnIndex(TimeLineDatabase.Constants.Association.author_id));
        privacy.setSelection(cursor.getInt(cursor.getColumnIndex(TimeLineDatabase.Constants.Association.privacy)));
        cursor.close();
        Cursor cursorAuthor = db.queryAuthorByID(author_id);
        cursorAuthor.moveToFirst();
        Author.setText(cursorAuthor.getString(cursorAuthor.getColumnIndex(TimeLineDatabase.Constants.Author.author)));
        cursorAuthor.close();
        Cursor event_id_cursor = db.queryEventByID(new long[]{event_id_1,event_id_2});
        event_id_cursor.moveToFirst();
        String Event1 = event_id_cursor.getString(event_id_cursor.getColumnIndex(TimeLineDatabase.Constants.Event.title));
        event_id_cursor.moveToNext();
        String Event2 = event_id_cursor.getString(event_id_cursor.getColumnIndex(TimeLineDatabase.Constants.Event.title));
        Events.setText(Event1+", "+Event2);
        event_id_cursor.close();
    }

    @Override
    public boolean add() {
        long event_id[] = new long[2];
        Cursor AuthorCursor,EventsCursor;
        boolean blockFlag = false;

        String ConnectionStr = Connection.getText().toString().trim();
        String AuthorStr = Author.getText().toString().trim();
        String EventStr = Events.getText().toString().trim();
        int author_id = 0;

        if(AuthorStr.equals("")){
           Author.setError(res.getString(R.string.error_empty));
           blockFlag = true;
        }
        if(!blockFlag){
            AuthorCursor = db.queryAuthorByName(AuthorStr);
            if (AuthorCursor.moveToNext()){
                author_id = AuthorCursor.getInt(AuthorCursor.getColumnIndex(TimeLineDatabase.Constants.Author.id));
            }else{
                Author.setError(res.getString(R.string.error_record));
                blockFlag = true;
            }
            AuthorCursor.close();
        }
        if(ConnectionStr.equals("")){
            Connection.setError(res.getString(R.string.error_empty));
            blockFlag = true;
        }
        if(!EventStr.equals("")){
            if(EventStr.indexOf(",") != 2){
                //оставленно с закладкой на возможное расширение и добавление нескольких событий, а не только 2х
                String EventArray[] = EventStr.split(",");
                for(String elem:EventArray){if(elem.equals("")){blockFlag = true; break;}}
                if(!blockFlag){
                    EventsCursor = db.queryEventByTitles(EventArray);
                    if(EventsCursor.moveToFirst()){
                        int i=0;
                        for (EventsCursor.moveToFirst(); !EventsCursor.isAfterLast(); EventsCursor.moveToNext(), i++) {
                            event_id[i] = EventsCursor.getLong(EventsCursor.getColumnIndex(TimeLineDatabase.Constants.Event.id));
                        }
                        if(event_id[0]==event_id[1]){
                            Events.setError(res.getString(R.string.error_duplicate));
                            blockFlag = true;
                        }
                    }
                    EventsCursor.close();
                }
            }else{
                Events.setError(res.getString(R.string.error_complete));
                blockFlag = true;
            }
        }else{
            Events.setError(res.getString(R.string.error_empty));
            blockFlag = true;
        }

        if(!blockFlag){//параметр 0 - будет использован в преспективе.
            db.addtoAssociation(event_id[0], event_id[1], ConnectionStr, 0, author_id, privacy.getSelectedItemPosition());
        }
        return !blockFlag;
    }

    @Override
    public boolean update(long id) {
        long event_id[] = new long[2];
        Cursor AuthorCursor,EventsCursor;
        boolean blockFlag = false;

        String ConnectionStr = Connection.getText().toString().trim();
        String AuthorStr = Author.getText().toString().trim();
        String EventStr = Events.getText().toString().trim();
        int author_id = 0;

        if(AuthorStr.equals("")){
            Author.setError(res.getString(R.string.error_empty));
            blockFlag = true;
        }
        if(!blockFlag){
            AuthorCursor = db.queryAuthorByName(AuthorStr);
            if (AuthorCursor.moveToNext()){
                author_id = AuthorCursor.getInt(AuthorCursor.getColumnIndex(TimeLineDatabase.Constants.Author.id));
            }else{
                Author.setError(res.getString(R.string.error_record));
                blockFlag = true;
            }
            AuthorCursor.close();
        }
        if(ConnectionStr.equals("")){
            Connection.setError(res.getString(R.string.error_empty));
            blockFlag = true;
        }
        if(!EventStr.equals("")){
            if(correctStr(EventStr)){
                //оставленно с закладкой на возможное расширение и добавление нескольких событий, а не только 2х
                String EventArray[] = EventStr.split(",");
                for(String elem:EventArray){if(elem.equals("")){blockFlag = true; break;}}
                if(!blockFlag){
                    EventsCursor = db.queryEventByTitles(EventArray);
                    if(EventsCursor.moveToFirst()){
                        int i=0;
                        for (EventsCursor.moveToFirst(); !EventsCursor.isAfterLast(); EventsCursor.moveToNext(), i++) {
                            event_id[i] = EventsCursor.getLong(EventsCursor.getColumnIndex(TimeLineDatabase.Constants.Event.id));
                        }
                        if(event_id[0]==event_id[1]){
                            Events.setError(res.getString(R.string.error_duplicate));
                            blockFlag = true;
                        }
                    }
                    EventsCursor.close();
                }
            }else{
                Events.setError(res.getString(R.string.error_complete));
                blockFlag = true;
            }
        }else{
            Events.setError(res.getString(R.string.error_empty));
            blockFlag = true;
        }

        if(!blockFlag){
            db.updatetoAssociation(id,event_id[0], event_id[1], ConnectionStr, author_id, privacy.getSelectedItemPosition());
        }
        return !blockFlag;
    }

    //false - empty all
    //false - full first, empty second
    //false - empty first, full second
    //true - full all
    boolean correctStr(String str) {
        int commaIndex;
        return (commaIndex = str.indexOf(",")) > 0
                && str.substring(0, commaIndex - 1).trim().equals("")
                | str.substring(commaIndex).trim().equals("");
    }
}
