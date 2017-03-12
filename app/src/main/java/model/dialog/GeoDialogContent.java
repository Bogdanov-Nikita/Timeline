package model.dialog;

import android.content.res.Resources;
import android.database.Cursor;
import android.view.View;
import android.widget.EditText;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.android.nik.timeline.R;

import database.TimeLineDatabase;
import database.TimeLineDatabase.Constants;
/**
 * Created by Nik on 17.03.2015.
 * class for create interface elements Geo's dialog.
 */
public class GeoDialogContent extends DialogContent {

    EditText Coordinates;
    AutoCompleteTextView Nationality;
    AutoCompleteTextView Continent;
    AutoCompleteTextView Country;
    AutoCompleteTextView City;
    AutoCompleteTextView Street;
    EditText Home;
    View view;

    public GeoDialogContent(final Resources res,TimeLineDatabase db){
        super(res,db);
    }

    @Override
    public void init(View rootView) {
        view = rootView;
        SimpleCursorAdapter adapter[] = new SimpleCursorAdapter[5];
        final String Table[] = {
                Constants.Nationality.TABLE_NAME,
                Constants.Continent.TABLE_NAME,
                Constants.Country.TABLE_NAME,
                Constants.City.TABLE_NAME,
                Constants.Street.TABLE_NAME};
        final String ID[] = {
                Constants.Nationality.id,
                Constants.Continent.id,
                Constants.Country.id,
                Constants.City.id,
                Constants.Street.id};
        final String name[] = {
                Constants.Nationality.Nationality,
                Constants.Continent.Continent,
                Constants.Country.Country,
                Constants.City.City,
                Constants.Street.Street};
        for(int i = 0; i < adapter.length; i++) {
            final int index = i;
            Cursor cursor = db.query(
                    Table[i],
                    new String[]{
                            ID[index],
                            name[index]},
                    null, null, null, null, null);
            adapter[index] = new SimpleCursorAdapter(
                    rootView.getContext(),
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{name[index]},
                    new int[]{android.R.id.text1},
                    SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

            adapter[index].setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
                @Override
                public CharSequence convertToString(Cursor cursor) {
                    return cursor.getString(cursor.getColumnIndex(name[index]));
                }
            });
            adapter[index].setFilterQueryProvider(new FilterQueryProvider() {
                @Override
                public Cursor runQuery(CharSequence constraint) {
                    if (constraint != null) {
                        String word = constraint.toString().trim();
                        if (!word.equals("")) {
                            return db.query(
                                    Table[index],
                                    new String[]{
                                            ID[index],
                                            name[index]},
                                    name[index] + " LIKE ?",
                                    new String[]{word + "%"}, null, null, null);
                        } else {
                            return null;
                        }
                    } else {
                        return null;
                    }
                }
            });
        }

        Coordinates = (EditText)rootView.findViewById(R.id.EditCoordinates);
        Nationality = (AutoCompleteTextView)rootView.findViewById(R.id.NationalityCompleteText);
        Nationality.setThreshold(1);
        Nationality.setAdapter(adapter[0]);
        Continent = (AutoCompleteTextView)rootView.findViewById(R.id.ContinentCompleteText);
        Continent.setThreshold(1);
        Continent.setAdapter(adapter[1]);
        Country = (AutoCompleteTextView)rootView.findViewById(R.id.CountryCompleteText);
        Country.setThreshold(1);
        Country.setAdapter(adapter[2]);
        City = (AutoCompleteTextView)rootView.findViewById(R.id.CityCompleteText);
        City.setThreshold(1);
        City.setAdapter(adapter[3]);
        Street = (AutoCompleteTextView)rootView.findViewById(R.id.StreetCompleteText);
        Street.setThreshold(1);
        Street.setAdapter(adapter[4]);
        Home = (EditText)rootView.findViewById(R.id.EditHome);
    }

    @Override
    public void set(long id){
        Cursor cursor = db.queryGeoByID(id);
        if(cursor != null){
            cursor.moveToFirst();
            Coordinates.setText(cursor.getString(cursor.getColumnIndex(Constants.Geo.Coordinates)));
            Home.setText(cursor.getString(cursor.getColumnIndex(Constants.Geo.home)));
            Cursor NationalityCursor = db.queryNationalityByID(cursor.getLong(cursor.getColumnIndex(Constants.Geo.Nationality_id)));
            NationalityCursor.moveToFirst();
            Nationality.setText(NationalityCursor.getString(NationalityCursor.getColumnIndex(Constants.Nationality.Nationality)));
            NationalityCursor.close();
            Cursor ContinentCursor = db.queryContinentByID(cursor.getLong(cursor.getColumnIndex(Constants.Geo.Continent_id)));
            ContinentCursor.moveToFirst();
            Continent.setText(ContinentCursor.getString(ContinentCursor.getColumnIndex(Constants.Continent.Continent)));
            ContinentCursor.close();
            Cursor CountryCursor = db.queryCountryByID(cursor.getLong(cursor.getColumnIndex(Constants.Geo.Country_id)));
            CountryCursor.moveToFirst();
            Country.setText(CountryCursor.getString(CountryCursor.getColumnIndex(Constants.Country.Country)));
            CountryCursor.close();
            Cursor CityCursor = db.queryCityByID(cursor.getLong(cursor.getColumnIndex(Constants.Geo.City_id)));
            CityCursor.moveToFirst();
            City.setText(CityCursor.getString(CityCursor.getColumnIndex(Constants.City.City)));
            CityCursor.close();
            Cursor StreetCursor = db.queryStreetByID(cursor.getLong(cursor.getColumnIndex(Constants.Geo.Street_id)));
            StreetCursor.moveToFirst();
            Street.setText(StreetCursor.getString(StreetCursor.getColumnIndex(Constants.Street.Street)));
            StreetCursor.close();
            cursor.close();
        }
    }

    @Override
    public boolean add() {
        String CoordinatesStr = Coordinates.toString().trim();
        String NationalityStr = Nationality.toString().trim();
        String ContinentStr = Continent.toString().trim();
        String CountryStr = Country.toString().trim();
        String CityStr = City.getText().toString().trim();
        String StreetStr = Street.toString().trim();
        String HomeStr = Home.toString().trim();
        if(CoordinatesStr.equals("") &&
                NationalityStr.equals("") &&
                ContinentStr.equals("") &&
                CountryStr.equals("") &&
                CityStr.equals("") &&
                StreetStr.equals("") &&
                HomeStr.equals("")){
            Toast.makeText(view.getContext(),res.getString(R.string.error_empty_optional),Toast.LENGTH_LONG).show();
            return false;
        }else {
            db.addtoGeo(
                    Coordinates.toString(),
                    Nationality.toString(),
                    Continent.toString(),
                    Country.toString(),
                    City.getText().toString(),
                    Street.toString(),
                    Home.toString());
            return true;
        }
    }

    @Override
    public boolean update(long id) {
        String CoordinatesStr = Coordinates.toString().trim();
        String NationalityStr = Nationality.toString().trim();
        String ContinentStr = Continent.toString().trim();
        String CountryStr = Country.toString().trim();
        String CityStr = City.getText().toString().trim();
        String StreetStr = Street.toString().trim();
        String HomeStr = Home.toString().trim();
        if(CoordinatesStr.equals("") &&
                NationalityStr.equals("") &&
                ContinentStr.equals("") &&
                CountryStr.equals("") &&
                CityStr.equals("") &&
                StreetStr.equals("") &&
                HomeStr.equals("")){
            Toast.makeText(view.getContext(),res.getString(R.string.error_empty_optional),Toast.LENGTH_LONG).show();
            return false;
        }else {
            db.updatetoGeo(id,
                    Coordinates.toString(),
                    Nationality.toString(),
                    Continent.toString(),
                    Country.toString(),
                    City.getText().toString(),
                    Street.toString(),
                    Home.toString());
            return true;
        }
    }
}
