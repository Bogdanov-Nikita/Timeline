package model.dialog;

import android.content.res.Resources;
import android.database.Cursor;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.nik.timeline.R;

//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;

import database.TimeLineDatabase;

/**
 * Created by Nik on 01.04.2015.
 * class for create interface elements Event's dialog.
 */
public class EventDialogContent extends DialogContent {

    Spinner privacy;
    //title
    EditText Title;
    //date
    EditText StartDate;
    EditText EndDate;
    //event description
    EditText Body;
    //category
    AutoCompleteTextView Category;
    //author
    AutoCompleteTextView Author;
    //geo
    EditText Coordinates;
    AutoCompleteTextView Nationality;
    AutoCompleteTextView Continent;
    AutoCompleteTextView Country;
    AutoCompleteTextView City;
    AutoCompleteTextView Street;
    EditText Home;
    //links
    EditText Links;

    View view;

    public EventDialogContent(Resources res, TimeLineDatabase db) {
        super(res, db);
    }

    @Override
    public void init(View rootView) {
        view = rootView;
        //privacy
        privacy = (Spinner)rootView.findViewById(R.id.privacy);
        ArrayAdapter<String> PrivacyAdapter = new ArrayAdapter<>(
                rootView.getContext(),
                android.R.layout.simple_list_item_1,
                res.getStringArray(R.array.dialog_association_privacy_list));
        privacy.setAdapter(PrivacyAdapter);
        //Title
        Title = (EditText)rootView.findViewById(R.id.EditTitle);

        //Date
        StartDate = (EditText)rootView.findViewById(R.id.EditStartDate);
        EndDate = (EditText)rootView.findViewById(R.id.EditEndDate);

        //Event description
        Body = (EditText)rootView.findViewById(R.id.EditBody);

        //Category
        Cursor CategoryCursor = db.query(
                TimeLineDatabase.Constants.Category.TABLE_NAME,
                new String[]{
                        TimeLineDatabase.Constants.Category.id,
                        TimeLineDatabase.Constants.Category.name},
                null, null, null, null, null);
        SimpleCursorAdapter CategoryAdapter = new SimpleCursorAdapter(
                rootView.getContext(),
                android.R.layout.simple_list_item_1,
                CategoryCursor,
                new String[]{TimeLineDatabase.Constants.Category.name},
                new int[]{android.R.id.text1},
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        CategoryAdapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                return cursor.getString(cursor.getColumnIndex(TimeLineDatabase.Constants.Category.name));
            }
        });

        CategoryAdapter.setFilterQueryProvider(new FilterQueryProvider() {
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

        Category = (AutoCompleteTextView)rootView.findViewById(R.id.CategoryCompleteText);
        Category.setThreshold(1);
        Category.setAdapter(CategoryAdapter);

        //Author
        Cursor AuthorCursor = db.query(
                TimeLineDatabase.Constants.Author.TABLE_NAME,
                new String[]{
                        TimeLineDatabase.Constants.Author.id,
                        TimeLineDatabase.Constants.Author.author},
                null, null, null, null, null);

        SimpleCursorAdapter AuthorAdapter = new SimpleCursorAdapter(
                rootView.getContext(),
                android.R.layout.simple_list_item_1,
                AuthorCursor,
                new String[]{TimeLineDatabase.Constants.Author.author},
                new int[]{android.R.id.text1},
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        AuthorAdapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                return cursor.getString(cursor.getColumnIndex(TimeLineDatabase.Constants.Author.author));
            }
        });

        AuthorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
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
        Author.setAdapter(AuthorAdapter);

        //Geo
        SimpleCursorAdapter adapter[] = new SimpleCursorAdapter[5];
        final String Table[] = {
                TimeLineDatabase.Constants.Nationality.TABLE_NAME,
                TimeLineDatabase.Constants.Continent.TABLE_NAME,
                TimeLineDatabase.Constants.Country.TABLE_NAME,
                TimeLineDatabase.Constants.City.TABLE_NAME,
                TimeLineDatabase.Constants.Street.TABLE_NAME};
        final String ID[] = {
                TimeLineDatabase.Constants.Nationality.id,
                TimeLineDatabase.Constants.Continent.id,
                TimeLineDatabase.Constants.Country.id,
                TimeLineDatabase.Constants.City.id,
                TimeLineDatabase.Constants.Street.id};
        final String name[] = {
                TimeLineDatabase.Constants.Nationality.Nationality,
                TimeLineDatabase.Constants.Continent.Continent,
                TimeLineDatabase.Constants.Country.Country,
                TimeLineDatabase.Constants.City.City,
                TimeLineDatabase.Constants.Street.Street};
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

        //Links
        Links = (EditText)rootView.findViewById(R.id.EditLinks);

    }

    @Override
    public void set(long id) {
        Cursor EventCursor = db.queryEventByID(new long[]{id});
        EventCursor.moveToFirst();

        //Title
        Title.setText(EventCursor.getString(EventCursor.getColumnIndex(TimeLineDatabase.Constants.Event.title)));

        //Date
        StartDate.setText(EventCursor.getString(EventCursor.getColumnIndex(TimeLineDatabase.Constants.Event.start_date)));
        EndDate.setText(EventCursor.getString(EventCursor.getColumnIndex(TimeLineDatabase.Constants.Event.end_date)));

        //Event description
        Body.setText(EventCursor.getString(EventCursor.getColumnIndex(TimeLineDatabase.Constants.Event.body)));

        //Category
        Cursor CategoryCursor = db.queryCategoryByID(
                EventCursor.getLong(EventCursor.getColumnIndex(TimeLineDatabase.Constants.Event.category_id)));
        CategoryCursor.moveToFirst();
        Category.setText(CategoryCursor.getString(
                CategoryCursor.getColumnIndex(TimeLineDatabase.Constants.Category.name)));
        CategoryCursor.close();

        //Author
        Cursor AuthorCursor = db.queryAuthorByID(
                EventCursor.getLong(EventCursor.getColumnIndex(TimeLineDatabase.Constants.Event.author_id)));
        AuthorCursor.moveToFirst();
        Author.setText(AuthorCursor.getString(
                AuthorCursor.getColumnIndex(TimeLineDatabase.Constants.Author.author)));
        AuthorCursor.close();

        //Geo
        Cursor GeoCursor = db.queryGeoByID(
                EventCursor.getLong(EventCursor.getColumnIndex(TimeLineDatabase.Constants.Event.geo_id)));
        GeoCursor.moveToFirst();
        Coordinates.setText(GeoCursor.getString(GeoCursor.getColumnIndex(TimeLineDatabase.Constants.Geo.Coordinates)));
        Home.setText(GeoCursor.getString(GeoCursor.getColumnIndex(TimeLineDatabase.Constants.Geo.home)));
        Cursor NationalityCursor = db.queryNationalityByID(GeoCursor.getLong(GeoCursor.getColumnIndex(TimeLineDatabase.Constants.Geo.Nationality_id)));
        NationalityCursor.moveToFirst();
        Nationality.setText(NationalityCursor.getString(NationalityCursor.getColumnIndex(TimeLineDatabase.Constants.Nationality.Nationality)));
        NationalityCursor.close();
        Cursor ContinentCursor = db.queryContinentByID(GeoCursor.getLong(GeoCursor.getColumnIndex(TimeLineDatabase.Constants.Geo.Continent_id)));
        ContinentCursor.moveToFirst();
        Continent.setText(ContinentCursor.getString(ContinentCursor.getColumnIndex(TimeLineDatabase.Constants.Continent.Continent)));
        ContinentCursor.close();
        Cursor CountryCursor = db.queryCountryByID(GeoCursor.getLong(GeoCursor.getColumnIndex(TimeLineDatabase.Constants.Geo.Country_id)));
        CountryCursor.moveToFirst();
        Country.setText(CountryCursor.getString(CountryCursor.getColumnIndex(TimeLineDatabase.Constants.Country.Country)));
        CountryCursor.close();
        Cursor CityCursor = db.queryCityByID(GeoCursor.getLong(GeoCursor.getColumnIndex(TimeLineDatabase.Constants.Geo.City_id)));
        CityCursor.moveToFirst();
        City.setText(CityCursor.getString(CityCursor.getColumnIndex(TimeLineDatabase.Constants.City.City)));
        CityCursor.close();
        Cursor StreetCursor = db.queryStreetByID(GeoCursor.getLong(GeoCursor.getColumnIndex(TimeLineDatabase.Constants.Geo.Street_id)));
        StreetCursor.moveToFirst();
        Street.setText(StreetCursor.getString(StreetCursor.getColumnIndex(TimeLineDatabase.Constants.Street.Street)));
        StreetCursor.close();
        GeoCursor.close();

        //Links
        Cursor LinksCursor = db.queryLinksByID(id);
        if(LinksCursor.moveToFirst()) {
            String links = "";
            for (;!LinksCursor.isAfterLast(); LinksCursor.moveToNext()) {
                links = links + LinksCursor.getString(LinksCursor.getColumnIndex(TimeLineDatabase.Constants.Links.link)) + ", ";
            }
            Links.setText(links);
        }
        EventCursor.close();
    }

    @Override
    public boolean add() {
        boolean GeoFlag;
        boolean EventFlag;
        long GeoID = -1;
        long EventID = -1;
        //add geo
        String CoordinatesStr = Coordinates.getText().toString().trim();
        String NationalityStr = Nationality.getText().toString().trim();
        String ContinentStr = Continent.getText().toString().trim();
        String CountryStr = Country.getText().toString().trim();
        String CityStr = City.getText().toString().trim();
        String StreetStr = Street.getText().toString().trim();
        String HomeStr = Home.getText().toString().trim();
        if(CoordinatesStr.equals("") &&
                NationalityStr.equals("") &&
                ContinentStr.equals("") &&
                CountryStr.equals("") &&
                CityStr.equals("") &&
                StreetStr.equals("") &&
                HomeStr.equals("")){
            Toast.makeText(view.getContext(), res.getString(R.string.error_empty_optional), Toast.LENGTH_LONG).show();
            GeoFlag = false;
        }else {
            GeoID = db.addtoGeo(
                    Coordinates.toString(),
                    Nationality.toString(),
                    Continent.toString(),
                    Country.toString(),
                    City.getText().toString(),
                    Street.toString(),
                    Home.toString());
            GeoFlag = true;
        }
        //add event
        if(GeoFlag) {
            String TitleStr = Title.getText().toString().trim();
            String StartDateStr = StartDate.getText().toString().trim();
            String EndDateStr = EndDate.getText().toString().trim();
            String BodyStr = Body.getText().toString().trim();
            String CategoryStr = Category.getText().toString().trim();
            String AuthorStr = Author.getText().toString().trim();

            if(TitleStr.equals("") &&
                    StartDateStr.equals("") &&
                    EndDateStr.equals("") &&
                    BodyStr.equals("") &&
                    CategoryStr.equals("") &&
                    AuthorStr.equals("")){
                Toast.makeText(view.getContext(), res.getString(R.string.error_empty_optional_event), Toast.LENGTH_LONG).show();
                EventFlag = false;
            }else {
                long CategoryID = -1,AuthorID = -1;
                //Category
                if(!CategoryStr.equals("")) {
                    Cursor CategoryCursor = db.queryCategoryByName(CategoryStr);
                    if(CategoryCursor != null) {
                        CategoryID = CategoryCursor.moveToFirst() ?
                                CategoryCursor.getLong(CategoryCursor.getColumnIndex(TimeLineDatabase.Constants.Category.id)) :
                                db.addtoCategory(CategoryStr);
                    }else{
                        //предупредить что нужно создать элемент сначала, а затем уже указывать его в событии.
                        //в дальнейшем добавить диалог с вопросом о создании нового элемента.
                        Category.setError(res.getString(R.string.error_not_category));
                    }
                }

                //Author
                if(!AuthorStr.equals("")) {
                    Cursor AuthorCursor = db.queryAuthorByName(AuthorStr);
                    if(AuthorCursor != null) {
                        AuthorID = AuthorCursor.moveToFirst() ?
                                AuthorCursor.getLong(AuthorCursor.getColumnIndex(TimeLineDatabase.Constants.Author.id)) :
                                db.addtoAutors(AuthorStr);
                    }else{
                        //предупредить что нужно создать элемент сначала, а затем уже указывать его в событии.
                        //в дальнейшем добавить диалог с вопросом о создании нового элемента.
                        Author.setError(res.getString(R.string.error_not_author));
                    }
                }
                //Event
                //возможно в дальнейшем условие проверки необходимости ID шников возможно будет пересмотренно.
                if(GeoID!=-1 && CategoryID!=-1 && AuthorID!=-1) {
                    EventID = db.addtoEvents(GeoID, BodyStr, TitleStr, StartDateStr, EndDateStr, AuthorID,
                            privacy.getSelectedItemPosition(), CategoryID);
                    EventFlag = true;
                }else{
                    EventFlag = false;
                }
            }
        }else{
            EventFlag = false;
        }
        //add links
        if(EventFlag){
            boolean EmptyFlag = true;
            String LinksStr = Links.getText().toString().trim();
            if(!LinksStr.equals("")){
                String LinkArray[] = LinksStr.split(",");
                for (String link : LinkArray) {
                    String TempStr = link.trim();
                    if (!TempStr.equals("")) {
                        EmptyFlag = false;
                        db.addtoLinks(EventID, TempStr);
                    }
                }//условие мусора вместо данных тогда ошибка
                if(EmptyFlag){
                    Links.setError(res.getString(R.string.error_empty));
                    return false;
                }
            }
        }
        return EventFlag;
    }

    @Override
    public boolean update(long id) {
        Cursor EventCursor = db.queryEventByID(new long[]{id});
        EventCursor.moveToFirst();
        //Update Event
        String TitleStr = EventCursor.getString(EventCursor.getColumnIndex(TimeLineDatabase.Constants.Event.title));
        String newTitle = Title.getText().toString().trim();
        if(!TitleStr.equals(newTitle)){
            db.updatetoEventTitle(id,newTitle);
        }
        String BodyStr = EventCursor.getString(EventCursor.getColumnIndex(TimeLineDatabase.Constants.Event.body));
        String newBody = Body.getText().toString().trim();
        if(!BodyStr.equals(newBody)){
            db.updatetoEventBody(id, newBody);
        }

        //Date TODO раскоментить и переделать тогда когда точно определим какой формат для дат использовать.
        /*String StartDateStr = EventCursor.getString(EventCursor.getColumnIndex(TimeLineDatabase.Constants.Event.start_date));
        String EndDateStr = EventCursor.getString(EventCursor.getColumnIndex(TimeLineDatabase.Constants.Event.end_date));
        SimpleDateFormat DataFormat = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS.SSS", Locale.getDefault());
        try {
            Date Start = DataFormat.parse(StartDateStr);
            Date End =  DataFormat.parse(EndDateStr);
            int i = Start.compareTo(End);
            if(i != 1){
                String newStartDate = StartDate.getText().toString().trim();
                String newEndDate = EndDate.getText().toString().trim();

                if(!StartDateStr.equals(newStartDate)){
                    db.updatetoEventStartDate(id, newStartDate);
                }

                if(!EndDateStr.equals(newEndDate)){
                    db.updatetoEventEndDate(id, newEndDate);
                }
            }else{
                StartDate.setError(res.getString(R.string.error_date_position));
                EndDate.setError(res.getString(R.string.error_date_position));
                return false;
            }
        } catch (ParseException e) {
            StartDate.setError(res.getString(R.string.error_date_format));
            EndDate.setError(res.getString(R.string.error_date_format));
            e.printStackTrace();
            return false;
        }*/

        //Category
        String CategoryStr = Category.getText().toString().trim();
        if(!CategoryStr.equals("")) {
            Cursor CategoryCursor = db.queryCategoryByName(CategoryStr);
            if( CategoryCursor != null ) {
                if (CategoryCursor.moveToFirst()) {
                    long CategoryId = CategoryCursor.getLong(CategoryCursor.getColumnIndex(TimeLineDatabase.Constants.Category.id));
                    db.updatetoEventCategoryId(id, CategoryId);
                }
            }else{
                //предупредить что нужно создать элемент сначала, а затем уже указывать его в событии.
                //в дальнейшем добавить диалог с вопросом о создании нового элемента.
                Category.setError(res.getString(R.string.error_not_category));
                return false;
            }
        }
        //Author
        String AuthorStr = Author.getText().toString().trim();
        if(!AuthorStr.equals("")) {
            Cursor AuthorCursor = db.queryAuthorByName(AuthorStr);
            if(AuthorCursor != null){
                if(AuthorCursor.moveToFirst()){
                    long AuthorId = AuthorCursor.getLong(AuthorCursor.getColumnIndex(TimeLineDatabase.Constants.Author.id));
                    db.updatetoEventAuthorId(id,AuthorId);
                }
            }else{
                //предупредить что нужно создать элемент сначала, а затем уже указывать его в событии.
                //в дальнейшем добавить диалог с вопросом о создании нового элемента.
                Author.setError(res.getString(R.string.error_not_author));
                return false;
            }
        }
        int PrivacyType = EventCursor.getInt(EventCursor.getColumnIndex(TimeLineDatabase.Constants.Event.privacy));
        if(privacy.getSelectedItemPosition() != PrivacyType){
            db.updatetoEventPrivacy(id,PrivacyType);
        }

        //Geo
        //TODO задать реакцию на изменения Geo если к геопозиции привязанно только одно событие то изменяем Geo
        //если несколько то создаём новое Geo c изменёнными параметрами, проверяя на существование уже такого же Geo
        //задействуем все таблицы связанные с Geo.
        //сейчас простая без проверки на привязку к нескольким событиям.

        String CoordinatesStr = Coordinates.getText().toString().trim();
        String NationalityStr = Nationality.getText().toString().trim();
        String ContinentStr = Continent.getText().toString().trim();
        String CountryStr = Country.getText().toString().trim();
        String CityStr = City.getText().toString().trim();
        String StreetStr = Street.getText().toString().trim();
        String HomeStr = Home.getText().toString().trim();
        if(CoordinatesStr.equals("") &&
                NationalityStr.equals("") &&
                ContinentStr.equals("") &&
                CountryStr.equals("") &&
                CityStr.equals("") &&
                StreetStr.equals("") &&
                HomeStr.equals("")){
            Toast.makeText(view.getContext(), res.getString(R.string.error_empty_optional), Toast.LENGTH_LONG).show();
        }else {
            long GeoID = EventCursor.getLong(EventCursor.getColumnIndex(TimeLineDatabase.Constants.Event.geo_id));
            db.updatetoGeo(GeoID,CoordinatesStr,NationalityStr,ContinentStr,CountryStr,CityStr,StreetStr,HomeStr);
        }


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