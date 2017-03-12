package model.dialog;

import android.content.res.Resources;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.nik.timeline.R;
import database.TimeLineDatabase;
import model.graphics.ColorSwitcher;
import model.search.TimeListItem;

/**
 * Created by Nik on 12.03.2015.
 * class for create interface elements Timeline dialog.
 */

public class TimelineDialogContent extends DialogContent {

    public EditText title;
    public EditText about;
    public Spinner color;
    public boolean visible;

    public TimelineDialogContent(final Resources res,TimeLineDatabase db){
        super(res,db);
    }

    @Override
    public void init(View rootView){
        visible = false;
        title = (EditText)rootView.findViewById(R.id.EditTitle);
        about = (EditText)rootView.findViewById(R.id.EditAbout);
        color = (Spinner)rootView.findViewById(R.id.color);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                rootView.getContext(),
                R.layout.color_item,
                R.id.ColorName,
                res.getStringArray(R.array.color_spinner_popup)){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                View img = view.findViewById(R.id.ColorView);
                int Colors[] = res.getIntArray(R.array.primary_colors);
                if(position >= 0 && position < Colors.length){
                    img.setBackgroundColor(Colors[position]);
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                View img = view.findViewById(R.id.ColorView);
                int Colors[] = res.getIntArray(R.array.primary_colors);
                if(position >= 0 && position < Colors.length){
                    img.setBackgroundColor(Colors[position]);
                }
                return view;
            }
        };
        color.setAdapter(adapter);
    }

    @Override
    public void set(long id){
        Cursor cursor = db.queryTimeListsByID(id);
        if(cursor != null){
            cursor.moveToFirst();
            title.setText(cursor.getString(
                    cursor.getColumnIndex(TimeLineDatabase.Constants.TimeLists.title)));
            about.setText(cursor.getString(
                    cursor.getColumnIndex(TimeLineDatabase.Constants.TimeLists.about)));
            color.setSelection(
                    ColorSwitcher.getIndexByPrimaryColor(cursor.getInt(
                                    cursor.getColumnIndex(TimeLineDatabase.Constants.TimeLists.color)),
                            res));/////visible
            visible = (cursor.getInt(cursor.getColumnIndex(
                    TimeLineDatabase.Constants.TimeLists.visible)) == TimeListItem.VISIBLE);
            cursor.close();
        }
    }

    @Override
    public boolean add() {
        db.addtoTimeLists(
                title.getText().toString(),
                about.getText().toString(),
                ColorSwitcher.getPrimaryColorByIndex(color.getSelectedItemPosition(),res),
                true);
        return true;
    }

    @Override
    public boolean update(long id) {
        db.updatetoTimeLists(id,
                title.getText().toString(),
                about.getText().toString(),
                ColorSwitcher.getPrimaryColorByIndex(color.getSelectedItemPosition(),res),
                visible);
        return true;
    }
}
