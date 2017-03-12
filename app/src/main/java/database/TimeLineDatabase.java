package database;


import java.util.ArrayList;

import database.TimeLineDatabase.Constants.GeoList;
import model.search.SearchContext;
import model.search.TimeListItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;



public class TimeLineDatabase {

    /**---------------------------Database Tables-------------------------------------------*/
    //TODO: оптимизировать некоторые таблицы для полнотекстового поиска задача на будущее
    // _ID из BaseColumss запрещенно линковщикиком по непонятным причинам.
    // пример. CREATE VIRTUAL  CREATE VIRTUAL TABLE "+ TABLE_NAME +" USING fts3 (
    public static final class Constants {

        public static final String DATABASE_NAME = "Timeline.db";
        public static final int DATABASE_VERSION = 2;// 1 version - was in prototype
        public static final String _ID = "_id"; // тоже самое что и BaseColumns._ID = "_id";

        /**-------all table classes use one skeleton-----
         * TABLE_NAME - Table name,
         * Other class objects - Table columns,
         * CREATE - SQLite Create table script.
         * ----*/

        /**------------------------tables with primary keys -------------------------------------*/
		/*Особенность двойной индекс по этому контроль индексация не автоинкрементная а самостоятельная! выделить для этого отдельное поля для счётчика! в таблице*/
        public static final class All_Keywords {
            public static final String TABLE_NAME = "All_Keywords";
            public static final String id = _ID;
            public static final String table_id = "table_id";
            public static final String word =  "word";
            public static final String CREATE =
                    "CREATE TABLE "+TABLE_NAME+"("+
                            id+" INTEGER NOT NULL,"+
                            table_id+" INTEGER NOT NULL,"+
                            word+" TEXT,"+
                            "CONSTRAINT PK_Key_Words PRIMARY KEY ( "+id+" , "+table_id+" ));";
        }

        public static final class Author  {
            public static final String TABLE_NAME =  "Author";
            public static final String id = _ID;
            public static final String author ="author";
            public static final String CREATE =
                    "CREATE TABLE  "+TABLE_NAME+"  ("+
                            id+"  INTEGER NOT NULL,"+
                            author+"  TEXT ,"+
                            "CONSTRAINT  PK_Autor  PRIMARY KEY ( "+id+" ));";
        }

        public static final class Category  {
            public static final String TABLE_NAME =  "Category";
            public static final String id = _ID;
            public static final String name ="name";
            public static final String CREATE =
                    "CREATE TABLE  "+TABLE_NAME+"  ("+
                            id+"  INTEGER NOT NULL,"+
                            name+"  TEXT ,"+
                            "CONSTRAINT  PK_Area_of_Life  PRIMARY KEY ( "+id+" ));";
        }

        public static final class City  {
            public static final String TABLE_NAME =  "City";
            public static final String id = _ID;
            public static final String City ="City";
            public static final String CREATE =
                    "CREATE TABLE  "+TABLE_NAME+"  ("+
                            id+"  INTEGER NOT NULL,"+
                            City+"  TEXT ,"+
                            "CONSTRAINT  PK_Citys  PRIMARY KEY ( "+id+" ));";
        }

        public static final class Continent  {
            public static final String TABLE_NAME =  "Continent";
            public static final String id = _ID;
            public static final String Continent ="Continent";
            public static final String CREATE =
                    "CREATE TABLE  "+TABLE_NAME+"  ("+
                            id+"  INTEGER NOT NULL,"+
                            Continent+"  TEXT ,"+
                            "CONSTRAINT  PK_Continents  PRIMARY KEY ( "+id+" ));";
        }

        public static final class Country  {
            public static final String TABLE_NAME =  "Country";
            public static final String id = _ID;
            public static final String Country ="Country";
            public static final String CREATE =
                    "CREATE TABLE  "+TABLE_NAME+"  ("+
                            id+"  INTEGER NOT NULL,"+
                            Country+"  TEXT ,"+
                            "CONSTRAINT  PK_Countrys    PRIMARY KEY ( "+id+" ));";
        }

        public static final class Nationality  {
            public static final String TABLE_NAME =  "Nationality";
            public static final String id = _ID;
            public static final String Nationality ="Nationality";
            public static final String CREATE =
                    "CREATE TABLE  "+TABLE_NAME+"  ("+
                            id+"  INTEGER NOT NULL,"+
                            Nationality+"  TEXT ,"+
                            "CONSTRAINT  PK_Nationality  PRIMARY KEY ( "+id+" ));";
        }

        public static final class Street  {
            public static final String TABLE_NAME =  "Street";
            public static final String id = _ID;
            public static final String Street ="Street";
            public static final String CREATE =
                    "CREATE TABLE  "+TABLE_NAME+"  ("+
                            id+"  INTEGER NOT NULL,"+
                            Street+"  TEXT ,"+
                            "CONSTRAINT  PK_Streets  PRIMARY KEY ( "+id+" ));";
        }

        public static final class Person  {
            public static final String TABLE_NAME =  "Person";
            public static final String id = _ID;
            public static final String person = "person";
            public static final String start_date ="start_date";
            public static final String end_date ="end_date";
            public static final String CREATE =
                    "CREATE TABLE  "+TABLE_NAME+"  ("+
                            id+"  INTEGER NOT NULL,"+
                            person+"  TEXT ,"+
                            start_date+"  TEXT ,"+
                            end_date+"  TEXT ,"+
                            "CONSTRAINT  PK_Persons  PRIMARY KEY ( "+id+" ));";
        }

        public static final class Author_Keyword  {
            public static final String TABLE_NAME =  "Author_Keyword";
            public static final String Author = "Author";
            public static final String Keyword ="Keyword";
            public static final String Table_N ="Table_N";
            public static final String CREATE =
                    "CREATE TABLE  "+TABLE_NAME+"  ("+
                            Author+"  INTEGER NOT NULL,"+
                            Keyword+"  INTEGER ,"+
                            Table_N+"  INTEGER DEFAULT 1 ,"+
                            "CHECK (Table_N=1),"+
                            "CONSTRAINT  FK_Autor_Autor_Keywords  FOREIGN KEY ( "+Author+" ) REFERENCES  "+Constants.Author.TABLE_NAME+"  ( "+Constants.Author.id+" ),"+
                            "CONSTRAINT  FK_Key_Word_Table_Autor  FOREIGN KEY ( "+Keyword+" ,  "+Table_N+" ) REFERENCES  "+
                            Constants.All_Keywords.TABLE_NAME+"  ( "+Constants.All_Keywords.id+" ,  "+Constants.All_Keywords.table_id+" ));";
        }

        public static final class Category_Keyword  {
            public static final String TABLE_NAME =  "Category_Keyword";
            public static final String Category = "Category";
            public static final String Keyword ="Keyword";
            public static final String Table_N ="Table_N";
            public static final String CREATE =
                    "CREATE TABLE  "+TABLE_NAME+"  ("+
                            Category+"  INTEGER NOT NULL,"+
                            Keyword+"  INTEGER ,"+
                            Table_N+"  INTEGER DEFAULT 2 ,"+
                            "CHECK (Table_N=2),"+
                            "CONSTRAINT  FK_Area_Area_Keywords  FOREIGN KEY ( "+Category+" ) REFERENCES  "+Constants.Category.TABLE_NAME+"  ( "+Constants.Category.id+" ),"+
                            "CONSTRAINT  FK_Key_Word_Table_Area  FOREIGN KEY ( "+Keyword+" ,  "+Table_N+" ) REFERENCES  "+
                            Constants.All_Keywords.TABLE_NAME+"  ( "+Constants.All_Keywords.id+" ,  "+Constants.All_Keywords.table_id+" ));";
        }

        public static final class Geo  {
            public static final String TABLE_NAME =  "Geo";
            public static final String id = _ID;
            public static final String Coordinates = "Coordinates";
            public static final String Nationality_id = "Nationality_id";
            public static final String Continent_id = "Continent_id";
            public static final String Country_id = "Country_id";
            public static final String City_id  = "City_id";
            public static final String Street_id = "Street_id";
            public static final String home = "home";
            public static final String CREATE =
                    "CREATE TABLE  "+TABLE_NAME+"  ("+
                            id+"  INTEGER NOT NULL,"+
                            Coordinates+"  TEXT ,"+
                            Nationality_id+"  INTEGER,"+
                            Continent_id+"  INTEGER,"+
                            Country_id+"  INTEGER,"+
                            City_id+"  INTEGER,"+
                            Street_id+"  INTEGER,"+
                            home+"  TEXT ,"+
                            "CONSTRAINT  PK_id_Geo  PRIMARY KEY ( "+id+" ),"+
                            "CONSTRAINT  FK_Continent_Geo  FOREIGN KEY ( "+Continent_id+" ) REFERENCES  "+Constants.Continent.TABLE_NAME+"  ( "+Constants.Continent.id+" ),"+
                            "CONSTRAINT  FK_Nationality_Geo  FOREIGN KEY ( "+Nationality_id+" ) REFERENCES  "+Constants.Nationality.TABLE_NAME+"  ( "+Constants.Nationality.id+" ),"+
                            "CONSTRAINT  FK_Country_Geo  FOREIGN KEY ( "+Country_id+" ) REFERENCES  Country  ( "+Constants.Country.id+" ),"+
                            "CONSTRAINT  FK_Sity_Geo  FOREIGN KEY ( "+City_id+" ) REFERENCES  City  ( "+Constants.City.id+" ),"+
                            "CONSTRAINT  FK_Street_Geo  FOREIGN KEY ( "+Street_id+" ) REFERENCES  "+Constants.Street.TABLE_NAME+"  ( "+Constants.Street.id+" ));";
        }

        public static final class GeoList  {//TODO поправить значения. null
            public static final String TABLE_NAME =  "GeoList";
            public static final String id = _ID;
            public static final String Nationality_id = "Nationality_id";
            public static final String Continents_id = "Continents_id";
            public static final String Countrys_id = "Countrys_id";
            public static final String Citys_id  = "Citys_id";
            public static final String Streets_id = "Streets_id";
            public static final String home = "home";
            public static final String CREATE =
                    "CREATE TABLE  "+TABLE_NAME+"  ("+
                            id+"  INTEGER NOT NULL,"+
                            Nationality_id+"  INTEGER NOT NULL,"+
                            Continents_id+"  INTEGER NOT NULL,"+
                            Citys_id+"  INTEGER NOT NULL,"+
                            Countrys_id+"  INTEGER NOT NULL,"+
                            Streets_id+"  INTEGER NOT NULL,"+
                            "CONSTRAINT  PK_GeoList  PRIMARY KEY ( "+id+" ),"+
                            "CONSTRAINT  FK_GeoList_Nationality  FOREIGN KEY ( "+Nationality_id+" ) REFERENCES  "+Constants.Nationality.TABLE_NAME+"  ( "+Constants.Nationality.id+" ),"+
                            "CONSTRAINT  FK_GeoList_Continents  FOREIGN KEY ( "+Continents_id+" ) REFERENCES  "+Constants.Continent.TABLE_NAME+"  ( "+Constants.Continent.id+" ),"+
                            "CONSTRAINT  FK_GeoList_Sitys  FOREIGN KEY ( "+Citys_id+" ) REFERENCES  "+Constants.City.TABLE_NAME+"  ( "+Constants.City.id+" ),"+
                            "CONSTRAINT  FK_GeoList_Countrys  FOREIGN KEY ( "+Countrys_id+" ) REFERENCES  "+Constants.Country.TABLE_NAME+"  ( "+Constants.Country.id+" ),"+
                            "CONSTRAINT  FK_GeoList_Steets  FOREIGN KEY ( "+Streets_id+" ) REFERENCES  "+Constants.Street.TABLE_NAME+"  ( "+Constants.Street.id+" ));";
        }

        public static final class Event  {
            public static final String TABLE_NAME =  "Event";
            public static final String id = _ID;
            public static final String geo_id = "geo_id";
            public static final String body = "body";
            public static final String title = "title";
            public static final String start_date = "start_date";
            public static final String end_date = "end_date";
            public static final String author_id = "author_id";
            public static final String category_id = "category_id";
            public static final String privacy = "privacy";
            public static final String CREATE =
                    "CREATE TABLE  "+TABLE_NAME+"  ("+
                            id+"  INTEGER NOT NULL,"+
                            geo_id+"  INTEGER NOT NULL,"+
                            body+"  TEXT  NOT NULL,"+
                            title+"  TEXT  NOT NULL,"+
                            start_date+"  DATE NOT NULL,"+
                            end_date+"  DATE,"+
                            author_id+"  INTEGER,"+
                            category_id+"  INTEGER NOT NULL,"+
                            privacy+"  INTEGER,"+
                            "CONSTRAINT  PK_id_Events  PRIMARY KEY ( "+id+" ),"+
                            "CONSTRAINT  FK_geo_id_Events  FOREIGN KEY ( "+geo_id+" ) REFERENCES  "+Constants.Geo.TABLE_NAME+"  ( "+Constants.Geo.id+" ),"+
                            "CONSTRAINT  FK_Area_of_Life_Events  FOREIGN KEY ( "+category_id+" ) REFERENCES  "+Constants.Category.TABLE_NAME+"  ( "+Constants.Category.id+" ),"+
                            "CONSTRAINT  FK_Autor_id_Events  FOREIGN KEY ( "+author_id+" ) REFERENCES  "+Constants.Author.TABLE_NAME+"  ( "+Constants.Author.id+" ));";
        }

        public static final class Association  {
            public static final String TABLE_NAME =  "Association";
            public static final String id = _ID;
            public static final String event_id_1 = "event_id_1";
            public static final String event_id_2 = "event_id_2";
            public static final String connection = "connection";
            public static final String author_id = "author_id";
            public static final String privacy = "privacy";
            public static final String association_type = "association_type";
            public static final String CREATE =
                    "CREATE TABLE  "+TABLE_NAME+"  ("+
                            id+"  INTEGER NOT NULL,"+
                            event_id_1+"  INTEGER NOT NULL,"+
                            event_id_2+"  INTEGER NOT NULL,"+
                            connection+"  TEXT ,"+
                            author_id+"  INTEGER NOT NULL,"+
                            privacy+"  INTEGER NOT NULL,"+
                            association_type+"  INTEGER,"+
                            "CONSTRAINT  PK_Connection  PRIMARY KEY ( "+id+" ),"+
                            "CONSTRAINT  FK_id_Autor_Connection  FOREIGN KEY ( "+author_id+" ) REFERENCES  "+Constants.Author.TABLE_NAME+"  ( "+Constants.Author.id+" ),"+
                            "CONSTRAINT  FK_ev1_Connection  FOREIGN KEY ( "+event_id_1+" ) REFERENCES  "+Constants.Event.TABLE_NAME+"  ( "+Constants.Event.id+" ),"+
                            "CONSTRAINT  FK_ev2_Connection  FOREIGN KEY ( "+event_id_2+" ) REFERENCES  "+Constants.Event.TABLE_NAME+"  ( "+Constants.Event.id+" ));";
        }

        public static final class Links  {
            public static final String TABLE_NAME =  "Links";
            public static final String id = _ID;
            public static final String link ="link";
            public static final String CREATE =
                    "CREATE TABLE  "+TABLE_NAME+"  ("+
                            id+"  INTEGER NOT NULL,"+
                            link+"  TEXT ,"+
                            "CONSTRAINT  FK_id_Links  FOREIGN KEY ( "+id+" ) REFERENCES  "+Constants.Event.TABLE_NAME+"  ( "+Constants.Event.id+" ));";
        }

        public static final class Event_and_Person  {
            public static final String TABLE_NAME =  "Event_and_Person";
            public static final String Persona = "Persona";
            public static final String Persona_Event ="Persona_Event";
            public static final String CREATE =
                    "CREATE TABLE  "+TABLE_NAME+"  ("+
                            Persona+"  INTEGER NOT NULL,"+
                            Persona_Event+"  INTEGER NOT NULL ,"+
                            "CONSTRAINT  FK_Person_Events_and_Person  FOREIGN KEY ( "+Persona+" ) REFERENCES  "+Constants.Person.TABLE_NAME+"  ( "+Constants.Person.id+" ),"+
                            "CONSTRAINT  FK_Person_Event_Events_and_Pers  FOREIGN KEY ( "+Persona_Event+" ) REFERENCES  "+Constants.Event.TABLE_NAME+"  ( "+Constants.Event.id+" ));";
        }


        public static final class Person_geo  {
            public static final String TABLE_NAME =  "Person_geo";
            public static final String persona_id = "persona_id";
            public static final String start_date ="start_date";
            public static final String end_date ="end_date";
            public static final String geo_id ="geo_id";
            public static final String CREATE =
                    "CREATE TABLE  "+TABLE_NAME+"  ("+
                            persona_id+"  INTEGER NOT NULL,"+
                            start_date+"  DATE,"+
                            end_date+"  DATE,"+
                            geo_id+"  INTEGER NOT NULL,"+
                            "CONSTRAINT  FK_geo_id_Person_geo  FOREIGN KEY ( "+geo_id+" ) REFERENCES  "+Constants.Geo.TABLE_NAME+"  ( "+Constants.Geo.id+" ),"+
                            "CONSTRAINT  FK_person_id_Person_geo  FOREIGN KEY ( "+persona_id+" ) REFERENCES  "+Constants.Person.TABLE_NAME+"  ( "+Constants.Geo.id+" ));";
        }

        public static final class Person_Keyword  {
            public static final String TABLE_NAME =  "Person_Keyword";
            public static final String Persona = "Persona";
            public static final String Keyword ="Keyword";
            public static final String Table_N ="Table_N";
            public static final String CREATE =
                    "CREATE TABLE  "+TABLE_NAME+"  ("+
                            Persona+"  INTEGER ,"+
                            Keyword+"  INTEGER ,"+
                            Table_N+"  INTEGER DEFAULT 4 ,"+
                            "CHECK (Table_N=4),"+
                            "CONSTRAINT  FK_Person_Person_Keywords  FOREIGN KEY ( "+Persona+" ) REFERENCES  "+Constants.Person.TABLE_NAME+"  ( "+Constants.Person.id+" ),"+
                            "CONSTRAINT  FK_Key_Word_Table_Person  FOREIGN KEY ( "+Keyword+" ,  "+Table_N+" ) REFERENCES  "+
                            Constants.All_Keywords.TABLE_NAME+"  ( "+Constants.All_Keywords.id+" ,  "+Constants.All_Keywords.table_id+" ));";
        }

        public static final class Synonym {
            public static final String TABLE_NAME = "Synonym";
            public static final String Keyword_id = "id";
            public static final String Synonym_keyword_id = "Synonym_keyword_id";
            public static final String table_id = "table_id";
            public static final String CREATE =
                    "CREATE TABLE "+TABLE_NAME+"("+
                            Keyword_id+" INTEGER NOT NULL,"+
                            Synonym_keyword_id+" INTEGER NOT NULL,"+
                            table_id+"  INTEGER,"+
                            "CONSTRAINT  FK_Key_word_Synonyms  FOREIGN KEY ( "+Keyword_id+" ,  "+table_id+" ) REFERENCES  "+
                            Constants.All_Keywords.TABLE_NAME+"  ( "+Constants.All_Keywords.id+" ,  table_id ),"+
                            "CONSTRAINT  FK_Synonym_Synonyms  FOREIGN KEY ( "+Synonym_keyword_id+" ,  "+table_id+" ) REFERENCES  "+
                            Constants.All_Keywords.TABLE_NAME+"  ( "+Constants.All_Keywords.id+" ,  "+Constants.All_Keywords.table_id+" ));";
        }

        public static final class TimeLine  {
            public static final String TABLE_NAME =  "TimeLine";
            public static final String id = _ID;
            public static final String description ="description";
            public static final String CREATE =
                    "CREATE TABLE  "+TABLE_NAME+"  ("+
                            id+"  INTEGER NOT NULL,"+
                            description+"  TEXT ,"+
                            "CONSTRAINT  PK_TimeLine  PRIMARY KEY ( "+id+" ));";
        }

        public static final class Mark  {
            public static final String TABLE_NAME =  "Mark";
            public static final String Keyword_id = "Keyword_id";
            public static final String Table_N ="Table_N";
            public static final String event_id ="event_id";
            public static final String TimeLine_id ="TimeLine_id";
            public static final String CREATE =
                    "CREATE TABLE  "+TABLE_NAME+"  ("+
                            Keyword_id+"  INTEGER NOT NULL,"+
                            Table_N+"  INTEGER DEFAULT 3,"+
                            event_id+"  INTEGER NOT NULL,"+
                            TimeLine_id+"  INTEGER,"+
                            "CHECK ("+Table_N+"=3),"+
                            "CONSTRAINT  FK_event_id_Tegs  FOREIGN KEY ( "+event_id+" ) REFERENCES  "+Constants.Event.TABLE_NAME+"  ( "+Constants.Event.id+" ),"+
                            "CONSTRAINT  FK_Key_Word_Table_Tegs  FOREIGN KEY ( "+Keyword_id+" ,  "+Table_N+" ) REFERENCES  "+Constants.All_Keywords.TABLE_NAME+"  ( "+Constants.All_Keywords.id+" ,  "+Constants.All_Keywords.table_id+" ),"+
                            "CONSTRAINT  FK_Timeline_id  FOREIGN KEY ( "+TimeLine_id+" ) REFERENCES  "+Constants.TimeLine.TABLE_NAME+"  ( "+Constants.All_Keywords.id+" ));";
        }

        public static final class TimeLists  {
            public static final String TABLE_NAME =  "TimeLists";
            public static final String id = _ID;
            public static final String title ="title";
            public static final String about ="about";
            public static final String color ="color";
            public static final String visible ="visible";
            public static final String CREATE =
                    "CREATE TABLE  "+TABLE_NAME+"  ("+
                            id+"  INTEGER NOT NULL,"+
                            title+"  TEXT ,"+
                            about+"  TEXT ,"+
                            color+"  INTEGER,"+
                            visible +"  INTEGER,"+
                            "CHECK ("+ visible +"=0 OR "+ visible +"=1 OR "+ visible +"=2 OR "+ visible +"=3 ),"+
                            "CONSTRAINT  PK_TimeLists  PRIMARY KEY ( "+id+" ));";
        }

        public static final class Context  {
            public static final String TABLE_NAME =  "Context";
            public static final String id = "id";
            public static final String geo ="geo";
            public static final String author ="author";
            public static final String persona ="persona";
            public static final String category ="category";
            public static final String timeline ="timeline";
            public static final String CREATE =
                    "CREATE TABLE  "+TABLE_NAME+"  ("+

                            id+"  INTEGER NOT NULL,"+
                            geo+"  INTEGER,"+
                            author+"  INTEGER,"+
                            persona+"  INTEGER,"+
                            category+"  INTEGER,"+
                            timeline+"  INTEGER,"+

                            //id+"  INTEGER NOT NULL,"+
                            //geo+"  INTEGER NOT NULL,"+
                            //author+"  INTEGER NOT NULL,"+
                            //persona+"  INTEGER NOT NULL,"+
                            //category+"  INTEGER NOT NULL,"+
                            //timeline+"  INTEGER,"+

                            "CONSTRAINT  FK_TimeLists_geo  FOREIGN KEY ( "+geo+" ) REFERENCES  "+Constants.GeoList.TABLE_NAME+"  ( "+Constants.GeoList.id+" ),"+
                            "CONSTRAINT  FK_TimeLists_Person  FOREIGN KEY ( "+persona+" ) REFERENCES  "+Constants.Person.TABLE_NAME+"  ( "+Constants.Person.id+" ),"+
                            "CONSTRAINT  FK_TimeLists_Area  FOREIGN KEY ( "+category+" ) REFERENCES  "+Constants.Category.TABLE_NAME+"  ( "+Constants.Category.id+" ),"+
                            "CONSTRAINT  FK_ConnectTimeLists  FOREIGN KEY ( "+id+" ) REFERENCES  "+Constants.TimeLists.TABLE_NAME+"  ( "+Constants.TimeLists.id+" ),"+
                            "CONSTRAINT  FK_TimeLists_Autors  FOREIGN KEY ( "+author+" ) REFERENCES  "+Constants.Author.TABLE_NAME+"  ( "+Constants.Author.id+" ),"+
                            "CONSTRAINT  FK_Timeline_id2  FOREIGN KEY ( "+timeline+" ) REFERENCES  "+Constants.TimeLine.TABLE_NAME+"  ( "+Constants.TimeLine.id+" ));";
        }

        public static final class EventList  {
            public static final String TABLE_NAME =  "EventList";
            public static final String TimeList_id = "TimeList_id";
            public static final String Event_id ="Event_id";
            public static final String x = "x";
            public static final String y ="y";
            public static final String CREATE =
                    "CREATE TABLE  "+TABLE_NAME+"  ("+
                            TimeList_id+"  INTEGER NOT NULL,"+
                            Event_id+"  INTEGER NOT NULL,"+
                            x+"  INTEGER,"+
                            y+"  INTEGER,"+
                            "CONSTRAINT  FK_EventList_list  FOREIGN KEY ( "+TimeList_id+" ) REFERENCES  "+Constants.TimeLists.TABLE_NAME+"  ( "+Constants.TimeLists.id+" ),"+
                            "CONSTRAINT  FK_EventList_event  FOREIGN KEY ( "+Event_id+" ) REFERENCES  "+Constants.Event.TABLE_NAME+"  ( "+Constants.Event.id+" ));";
        }

    }

    /**
     * @author   Nik
     */
    private static class TimeLineOpenHelper extends SQLiteOpenHelper implements  BaseColumns{
        /**
         */
        private LoadDataTask task;
        private SQLiteDatabase mDatabase; //

        public TimeLineOpenHelper(Context context) {
            super(context,Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            mDatabase = db;

            /**
             * Порядок создания таблиц менять запрещено!
             * в противном случае при создании таблиц выйдет ошибка из-за внешних ключей.
             **/
            mDatabase.beginTransaction();
            mDatabase.execSQL(Constants.All_Keywords.CREATE);
            mDatabase.execSQL(Constants.Author.CREATE);
            mDatabase.execSQL(Constants.Category.CREATE);
            mDatabase.execSQL(Constants.City.CREATE);
            mDatabase.execSQL(Constants.Continent.CREATE);
            mDatabase.execSQL(Constants.Country.CREATE);
            mDatabase.execSQL(Constants.Nationality.CREATE);
            mDatabase.execSQL(Constants.Street.CREATE);
            mDatabase.execSQL(Constants.Person.CREATE);
            mDatabase.execSQL(Constants.Author_Keyword.CREATE);
            mDatabase.execSQL(Constants.Category_Keyword.CREATE);
            mDatabase.execSQL(Constants.Geo.CREATE);
            mDatabase.execSQL(Constants.GeoList.CREATE);
            mDatabase.execSQL(Constants.Event.CREATE);
            mDatabase.execSQL(Constants.Association.CREATE);
            mDatabase.execSQL(Constants.Links.CREATE);
            mDatabase.execSQL(Constants.Event_and_Person.CREATE);
            mDatabase.execSQL(Constants.Person_geo.CREATE);
            mDatabase.execSQL(Constants.Person_Keyword.CREATE);
            mDatabase.execSQL(Constants.Synonym.CREATE);
            mDatabase.execSQL(Constants.TimeLine.CREATE);
            mDatabase.execSQL(Constants.Mark.CREATE);
            mDatabase.execSQL(Constants.TimeLists.CREATE);
            mDatabase.execSQL(Constants.Context.CREATE);
            mDatabase.execSQL(Constants.EventList.CREATE);
            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();
            LoadTestDataBase();
        }
        void LoadTestDataBase(){
            task = new LoadDataTask();
            task.execute();
        }
        class LoadDataTask extends AsyncTask<Void, Void, Void>{

            @Override
            protected void onPreExecute() {
                Log.i(TimeLineDatabase.class.getSimpleName(), "start load test data");
                super.onPreExecute();
            }
            @Override
            protected Void doInBackground(Void... params) {
                _LoadTestDataBase();
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
                Log.i(TimeLineDatabase.class.getSimpleName(), "end load test data");
                super.onPostExecute(result);
            }
        }

        void _LoadTestDataBase(){
            //заполнение тестовыми данными остальное через интерфейс во время работы телефона.
            DataLoader.loadNationality(mDatabase);
            DataLoader.loadContinent(mDatabase);
            DataLoader.loadCountry(mDatabase);
            DataLoader.loadCity(mDatabase);
            DataLoader.loadStreets(mDatabase);
            DataLoader.loadTestGeo(mDatabase,10);
            DataLoader.loadAuthors(mDatabase);
            DataLoader.loadCategory(mDatabase);
            DataLoader.loadPersons(mDatabase);
            DataLoader.loadEvents(mDatabase);
            DataLoader.loadTimeline(mDatabase);
            DataLoader.loadMark(mDatabase);
        }
        void LoadTestData(int i){

            ContentValues values = new ContentValues();

            values.put(Constants.TimeLists.title,"title_"+i);
            values.put(Constants.TimeLists.about,"some text about_"+i);
            values.put(Constants.TimeLists.color, Color.WHITE);
            values.put(Constants.TimeLists.visible, 1);
            mDatabase.insert(Constants.TimeLists.TABLE_NAME,null,values);
            values.clear();
/*
            values.put(Constants.Street.Street,"Strieet_"+i);
            mDatabase.insert(Constants.Street.TABLE_NAME,null,values);
            values.clear();

            values.put(Constants.City.City,"City_"+i);
            mDatabase.insert(Constants.City.TABLE_NAME,null,values);
            values.clear();

            values.put(Constants.Country.Country,"Country_"+i);
            mDatabase.insert(Constants.Country.TABLE_NAME,null,values);
            values.clear();

            values.put(Constants.Continent.Continent,"Continent_"+i);
            mDatabase.insert(Constants.Continent.TABLE_NAME,null,values);
            values.clear();

            values.put(Constants.Nationality.Nationality,"Nationality_"+i);
            mDatabase.insert(Constants.Nationality.TABLE_NAME,null,values);
            values.clear();


            values.put(Constants.Geo.Coordinates,"GEO Coordinates_"+i);
            values.put(Constants.Geo.Nationality_id,i);
            values.put(Constants.Geo.Continent_id,i);
            values.put(Constants.Geo.Country_id,i);
            values.put(Constants.Geo.City_id,i);
            values.put(Constants.Geo.Street_id,i);
            values.put(Constants.Geo.home,"home_"+i);
            mDatabase.insert(Constants.Geo.TABLE_NAME,null,values);
            values.clear();


            values.put(Constants.Author.author,"Author_"+i);
            mDatabase.insert(Constants.Author.TABLE_NAME,null,values);
            values.clear();

            values.put(Constants.Category.name,"Category_"+i);
            mDatabase.insert(Constants.Category.TABLE_NAME,null,values);
            values.clear();
*/

            values.put(Constants.Event.title,"Event Title_"+i);
            values.put(Constants.Event.body,"Some text about event_"+i);
            values.put(Constants.Event.category_id,1);
            values.put(Constants.Event.geo_id,1);
            values.put(Constants.Event.author_id,1);
            values.put(Constants.Event.start_date,1);
            values.put(Constants.Event.end_date,1);
            values.put(Constants.Event.privacy,0xF);
            mDatabase.insert(Constants.Event.TABLE_NAME,null,values);
            values.clear();

            values.put(Constants.Links.id,i);
            values.put(Constants.Links.link,"link_"+i);
            mDatabase.insert(Constants.Links.TABLE_NAME,null,values);
            values.clear();

            /*values.put(Constants.Person.start_date,i);
            values.put(Constants.Person.end_date,i);
            mDatabase.insert(Constants.Person.TABLE_NAME,null,values);
            values.clear();

            values.put(Constants.Person_geo.persona_id,i);
            values.put(Constants.Person_geo.geo_id,i);
            values.put(Constants.Person_geo.start_date,i);
            values.put(Constants.Person_geo.end_date,i);
            mDatabase.insert(Constants.Person_geo.TABLE_NAME,null,values);
            values.clear();

            values.put(Constants.Event_and_Person.Persona,i);
            values.put(Constants.Event_and_Person.Persona_Event,i);
            mDatabase.insert(Constants.Event_and_Person.TABLE_NAME,null,values);
            values.clear();

            values.put(Constants.Event_and_Person.Persona,i);
            values.put(Constants.Event_and_Person.Persona_Event,i);
            mDatabase.insert(Constants.Event_and_Person.TABLE_NAME,null,values);
            values.clear();*/

            /*values.put(Constants.TimeLine.description,"Time line description_"+i);
            mDatabase.insert(Constants.TimeLine.TABLE_NAME,null,values);
            values.clear();

            values.put(Constants.GeoList.Nationality_id,i);
            values.put(Constants.GeoList.Continents_id,i);
            values.put(Constants.GeoList.Countrys_id,i);
            values.put(Constants.GeoList.Citys_id,i);
            values.put(Constants.GeoList.Streets_id,i);
            mDatabase.insert(Constants.GeoList.TABLE_NAME,null,values);
            values.clear();
            */
            values.put(Constants.All_Keywords.id, i);
            values.put(Constants.All_Keywords.table_id, i);
            values.put(Constants.All_Keywords.word, "key word_" + i);
            mDatabase.insert(Constants.All_Keywords.TABLE_NAME, null, values);
            values.clear();

            values.put(Constants.Mark.event_id,i);
            values.put(Constants.Mark.Keyword_id,i);
            values.put(Constants.Mark.TimeLine_id,i);
            mDatabase.insert(Constants.Mark.TABLE_NAME,null,values);
            values.clear();

            values.put(Constants.Category_Keyword.Category,i);
            values.put(Constants.Category_Keyword.Keyword,i);
            mDatabase.insert(Constants.Category_Keyword.TABLE_NAME,null,values);
            values.clear();

            values.put(Constants.Author_Keyword.Author,i);
            values.put(Constants.Author_Keyword.Keyword,i);
            mDatabase.insert(Constants.Author_Keyword.TABLE_NAME,null,values);
            values.clear();

            values.put(Constants.Person_Keyword.Persona,i);
            values.put(Constants.Person_Keyword.Keyword,i);
            mDatabase.insert(Constants.Person_Keyword.TABLE_NAME,null,values);
            values.clear();

        }
        void addTestAssociation(int i,int j){
            ContentValues values = new ContentValues();
            values.put(Constants.Association.event_id_1,i);
            values.put(Constants.Association.event_id_2,j);
            values.put(Constants.Association.privacy,0xFF);
            values.put(Constants.Association.connection,"cconnection_"+i);
            values.put(Constants.Association.author_id,i);
            mDatabase.insert(Constants.Association.TABLE_NAME,null,values);
            values.clear();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if(newVersion>oldVersion){
                //Когда появиться сервер с базой тогда добавим здесь код для работы обновления базы.
            }
        }

    }

    /**
     */
    private final TimeLineOpenHelper mTimeLineOpenHelper;

    public TimeLineDatabase(Context context) {
        mTimeLineOpenHelper = new TimeLineOpenHelper(context);
        mTimeLineOpenHelper.mDatabase = mTimeLineOpenHelper.getWritableDatabase();
    }

    public void beginTransaction(){
        mTimeLineOpenHelper.mDatabase.beginTransaction();}
    public void endTransaction(){
        mTimeLineOpenHelper.mDatabase.endTransaction();}
    public void setTransactionSuccessful(){
        mTimeLineOpenHelper.mDatabase.setTransactionSuccessful();}
    public /*synchronized*/ void close(){
        mTimeLineOpenHelper.mDatabase.close();}
    /**
     * @param table - название таблиы к которой обращаемся
     * @param columns - столбцы для возврата
     * @param selection - НАЗВАНИЕ столбца
     * @param selectionArgs - АРГУМЕНТ столбца
     * @param groupBy - выражение после оператора GROUP BY
     * @param having - выражение  после оператора HAVING
     * @param orderBy - ORDER BY сортировка
     * <h2>
     * Восстановленный sql запрос для наглядности.
     * </h2>
     * <blockquote>
     * <code>
     * SELECT <b>columns</b> FROM <b>table</b> WHERE <b>selection</b> = <b>selectionArgs</b> GROUP BY <b>groupBy</b> HAVING <b>having</b> ORDER <b>orderBy</b>
     * </code>
     * </blockquote>
     * */
    public Cursor query(String table,String[] columns,String selection,String[] selectionArgs,String groupBy,String having,String orderBy){
        return mTimeLineOpenHelper.mDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }
    public Cursor query(String table,String[] columns,String selection,String[] selectionArgs,String groupBy,String having,String orderBy,String limit){
        return mTimeLineOpenHelper.mDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }
    public Cursor query(boolean distinct,String table,String[] columns,String selection,String[] selectionArgs,String groupBy,String having,String orderBy,String limit){
        return mTimeLineOpenHelper.mDatabase.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }
    public long update(String table,ContentValues values,String whereClause,String[] whereArgs){
        return mTimeLineOpenHelper.mDatabase.update(table, values, whereClause, whereArgs);
    }
    public long insert(String table,String nullColumnHack,ContentValues values){
        return mTimeLineOpenHelper.mDatabase.insert(table, nullColumnHack, values);
    }
    public int delete(String table,String whereClause,String[] whereArgs){
        return mTimeLineOpenHelper.mDatabase.delete(table, whereClause, whereArgs);
    }
    public Cursor rawQuery(String sql,String[] selectionArgs){
        return mTimeLineOpenHelper.mDatabase.rawQuery(sql, selectionArgs);
    }
    public void execSQL(String sql){
        mTimeLineOpenHelper.mDatabase.execSQL(sql);
    }
    public void execSQL(String sql,Object []bindArgs){
        mTimeLineOpenHelper.mDatabase.execSQL(sql, bindArgs);
    }

    //Методы добавления специально для каждой из таблиц. INSERT
    //не забыть какой тип для баз данных испльзуеться
    // т.к. по переменным не всега понятно пример таблица All_Key_Words (исправленно)

    /**------------------------tables with primary keys -------------------------------------*/

    /** Заполнение  таблицы All_Key_Words
     * @see
     * SQLiteDatabase
     * @param id - тип INTEGER
     * @param table_id - тип INTEGER
     * @param word - тип TEXT
     *  @return the row ID of the newly inserted row, or -1 if an error occurred
     *  */
    public long addtoAll_Key_Words(int id,int table_id,String word){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.All_Keywords.id,id);
        initialValues.put(Constants.All_Keywords.table_id,table_id);
        initialValues.put(Constants.All_Keywords.word,word);
        return insert(Constants.All_Keywords.TABLE_NAME,null,initialValues);
    }
    public long addtoCategory(String name) {
        ContentValues initialValues = new ContentValues();
        initialValues.put( Constants.Category.name,name);
        return insert(Constants.Category.TABLE_NAME,null,initialValues);
    }
    public long addtoAutors(String autor){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.Author.author,autor);
        return insert(Constants.Author.TABLE_NAME,null,initialValues);
    }
    public long addtoContinents(String continent){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.Continent.Continent,continent);
        return insert(Constants.Continent.TABLE_NAME,null,initialValues);
    }
    public long addtoCountrys(String country){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.Country.Country,country);
        return insert(Constants.Country.TABLE_NAME,null,initialValues);
    }
    public long addtoSitys(String city){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.City.City,city);
        return insert(Constants.City.TABLE_NAME,null,initialValues);
    }
    public long addtoStreets(String street){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.Street.Street,street);
        return insert(Constants.Street.TABLE_NAME,null,initialValues);
    }
    public long addtoNationality(String nationality){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.Nationality.Nationality,nationality);
        return insert(Constants.Nationality.TABLE_NAME,null,initialValues);
    }
    /**
     * @param start_date - дата рождения.
     * @param end_date - дата смерти.
     * в базе это TEXT , но для логики программы это DATE дата. */
    public long addtoPersons(String person,String start_date,String end_date){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.Person.person,person);
        initialValues.put(Constants.Person.start_date,start_date);
        initialValues.put(Constants.Person.end_date,end_date);
        return insert(Constants.Person.TABLE_NAME,null,initialValues);
    }
    /**----------------------all logic tables with foreign keys-----------------------------*/
    public long addtoSynonyms(int Keyword_id,int Synonym_keyword_id,int table_id){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.Synonym.Keyword_id,Keyword_id);
        initialValues.put(Constants.Synonym.Synonym_keyword_id,Synonym_keyword_id);
        initialValues.put(Constants.Synonym.table_id,table_id);
        return insert(Constants.Synonym.TABLE_NAME,null,initialValues);
    }
    public long addtoGeo(String Coordinates,long Nationality_id,long Continent_id,long Country_id,long City_id,long Street_id,String home){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.Geo.Coordinates,Coordinates);
        initialValues.put(Constants.Geo.Nationality_id,Nationality_id);
        initialValues.put(Constants.Geo.Continent_id, Continent_id);
        initialValues.put(Constants.Geo.Country_id, Country_id);
        initialValues.put(Constants.Geo.City_id, City_id);
        initialValues.put(Constants.Geo.Street_id, Street_id);
        initialValues.put(Constants.Geo.home, home);
        return insert(Constants.Geo.TABLE_NAME, null, initialValues);
    }
    public long addtoGeo(String Coordinates,String Nationality,String Continent,String Country,String City,String Street,String home){
        Cursor NationalityCursor = query(Constants.Nationality.TABLE_NAME,
                new String[]{
                        Constants.Nationality.id,
                        Constants.Nationality.Nationality},
                Constants.Nationality.Nationality + " LIKE ? ",
                new String[] { Nationality },
                null, null, null);
        Cursor ContinentCursor = query(Constants.Continent.TABLE_NAME,
                new String[]{
                        Constants.Continent.id,
                        Constants.Continent.Continent},
                Constants.Continent.Continent + " LIKE ? ",
                new String[] { Continent },
                null, null, null);
        Cursor CountryCursor = query(Constants.Country.TABLE_NAME,
                new String[]{
                        Constants.Country.id,
                        Constants.Country.Country},
                Constants.Country.Country + " LIKE ? ",
                new String[] { Country },
                null, null, null);
        Cursor CityCursor = query(Constants.City.TABLE_NAME,
                new String[]{
                        Constants.City.id,
                        Constants.City.City},
                Constants.City.City + " LIKE ? ",
                new String[] { City },
                null, null, null);
        Cursor StreetCursor = query(Constants.Street.TABLE_NAME,
                new String[]{
                        Constants.Street.id,
                        Constants.Street.Street},
                Constants.Street.Street + " LIKE ? ",
                new String[] { Street },
                null, null, null);
        long Nationality_id;
        if(NationalityCursor != null) {
            Nationality_id = (NationalityCursor.moveToFirst()) ?
                    NationalityCursor.getInt(NationalityCursor.getColumnIndex(Constants.Nationality.id)) ://if - > найти запись в таблице
                    this.addtoNationality(Nationality);//else -> нету такой записи. добавить в таблицу и получить id
            NationalityCursor.close();
        }else{
            Nationality_id = this.addtoNationality(Nationality);
        }
        long Continent_id;
        if(ContinentCursor != null) {
            Continent_id = (ContinentCursor.moveToFirst()) ?
                    ContinentCursor.getInt(ContinentCursor.getColumnIndex(Constants.Continent.id)) :
                    this.addtoContinents(Continent);
            ContinentCursor.close();
        }else{
            Continent_id = this.addtoContinents(Continent);
        }
        long Country_id;
        if(CountryCursor != null) {
            Country_id = (CountryCursor.moveToFirst()) ?
                    CountryCursor.getInt(CountryCursor.getColumnIndex(Constants.Country.id)) :
                    this.addtoCountrys(Country);
            CountryCursor.close();
        }else{
            Country_id = this.addtoCountrys(Country);
        }
        long City_id;
        if(CityCursor != null) {
            City_id = (CityCursor.moveToFirst()) ?
                    CityCursor.getInt(CityCursor.getColumnIndex(Constants.City.id)) :
                    this.addtoSitys(City);
            CityCursor.close();
        }else{
            City_id = this.addtoSitys(City);
        }
        long Street_id;
        if(StreetCursor != null) {
            Street_id = (StreetCursor.moveToFirst()) ?
                    StreetCursor.getInt(StreetCursor.getColumnIndex(Constants.Street.id)) :
                    this.addtoStreets(Street);
            StreetCursor.close();
        }else{
            Street_id = this.addtoStreets(Street);
        }

        return addtoGeo(Coordinates,Nationality_id,Continent_id,Country_id,City_id,Street_id,home);
    }
    /**
     * @param start_date - дата начала события.
     * @param end_date - дата конца события если есть.
     * в базе это TEXT , но для логики программы это DATE дата. */
    public long addtoEvents(long geo_id,String body,String title,String start_date,String end_date,long author_id,int privacy,long category_id){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.Event.geo_id,geo_id);
        initialValues.put(Constants.Event.body,body);
        initialValues.put(Constants.Event.title,title);
        initialValues.put(Constants.Event.start_date,start_date);
        initialValues.put(Constants.Event.end_date,end_date);
        initialValues.put(Constants.Event.author_id,author_id);
        initialValues.put(Constants.Event.category_id,category_id);
        initialValues.put(Constants.Event.privacy,privacy);
        return insert(Constants.Event.TABLE_NAME,null,initialValues);
    }
    /**
     * @param start_date - дата начала пребывания персоны в этом месте.
     * @param end_date - дата конца пребывания.
     * в базе это TEXT , но для логики программы это DATE дата. */
    public long addtoPersons_geo(int persona_id,String start_date,String end_date,int geo_id){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.Person_geo.persona_id,persona_id);
        initialValues.put(Constants.Person_geo.start_date,start_date);
        initialValues.put(Constants.Person_geo.end_date,end_date);
        initialValues.put(Constants.Person_geo.geo_id,geo_id);
        return insert(Constants.Person_geo.TABLE_NAME,null,initialValues);
    }
    public long addtoEvents_and_Person(int persona,int persona_Event){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.Event_and_Person.Persona,persona);
        initialValues.put(Constants.Event_and_Person.Persona_Event,persona_Event);
        return insert(Constants.Event_and_Person.TABLE_NAME,null,initialValues);
    }
    /** поле Table_N всегда только 4. */
    public long addtoPerson_Keywords(int Persona,int Keyword){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.Person_Keyword.Persona,Persona);
        initialValues.put(Constants.Person_Keyword.Keyword,Keyword);
        return insert(Constants.Person_Keyword.TABLE_NAME,null,initialValues);
    }
    public long addtoAssociation(long event_id_1,long event_id_2,String connection,int association_type,int author_id,int privacy){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.Association.event_id_1,event_id_1);
        initialValues.put(Constants.Association.event_id_2,event_id_2);
        initialValues.put(Constants.Association.connection,connection);
        initialValues.put(Constants.Association.association_type,association_type);
        initialValues.put(Constants.Association.author_id,author_id);
        initialValues.put(Constants.Association.privacy,privacy);
        return insert(Constants.Association.TABLE_NAME,null,initialValues);
    }

    /** поле Table_N всегда только 3. */
    public long addtoMark(int Keyword_id,int event_id){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.Mark.Keyword_id,Keyword_id);
        initialValues.put(Constants.Mark.event_id,event_id);
        return insert(Constants.Mark.TABLE_NAME,null,initialValues);
    }

    /** поле Table_N всегда только 2.*/
    public long addtoCategory_Keyword(int  Category,int  Keyword){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.Category_Keyword.Category,Category);
        initialValues.put(Constants.Category_Keyword.Keyword,Keyword);
        return insert(Constants.Category_Keyword.TABLE_NAME,null,initialValues);
    }
    /** поле Table_N всегда только 1. */
    public long addtoAuthor_Keyword(int Author,int Keyword){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.Author_Keyword.Author,Author);
        initialValues.put(Constants.Author_Keyword.Keyword,Keyword);
        return insert(Constants.Author_Keyword.TABLE_NAME,null,initialValues);
    }
    /**
     *@param id ссылка на id таблицы Events
     *@param link - url страница в интернете .*/
    public long addtoLinks (long id,String link){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.Links.id,id);
        initialValues.put(Constants.Links.link,link);
        return insert(Constants.Links.TABLE_NAME,null,initialValues);
    }

    /**--------------------------Logic for time list---------------------------- */

    public long addtoTimeLists (String title,String about,int color,boolean visable){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.TimeLists.title,title);
        initialValues.put(Constants.TimeLists.about,about);
        initialValues.put(Constants.TimeLists.color,color);
        int int_visable = 0;
        if(visable){int_visable = 1;}
        initialValues.put(Constants.TimeLists.visible,int_visable );
        return insert(Constants.TimeLists.TABLE_NAME,null,initialValues);
    }
    public long addtoEventList(int TimeLists_id ,int Events_id,int x,int y){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.EventList.TimeList_id,TimeLists_id);
        initialValues.put(Constants.EventList.Event_id,Events_id);
        initialValues.put(Constants.EventList.x,x);
        initialValues.put(Constants.EventList.y,y);
        return insert(Constants.EventList.TABLE_NAME,null,initialValues);
    }
    public long addtoGeoList(int Nationality_id,int Continents_id,int Citys_id,int Countrys_id,int Streets_id){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.GeoList.Nationality_id,Nationality_id);
        initialValues.put(Constants.GeoList.Continents_id,Continents_id);
        initialValues.put(Constants.GeoList.Citys_id,Citys_id);
        initialValues.put(Constants.GeoList.Countrys_id,Countrys_id);
        initialValues.put(Constants.GeoList.Streets_id,Streets_id);
        return insert(Constants.GeoList.TABLE_NAME,null,initialValues);
    }
    public long addtoContext(int geo,int author,int persona,int category,int timeline){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.Context.geo, geo);
        initialValues.put(Constants.Context.author,author);
        initialValues.put(Constants.Context.persona, persona);
        initialValues.put(Constants.Context.category, category);
        initialValues.put(Constants.Context.timeline, timeline);
        return insert(Constants.Context.TABLE_NAME,null,initialValues);
    }
    public long insertTimeLists(String title,String about,int color,int visable){
        ContentValues values = new ContentValues();
        values.put(Constants.TimeLists.title, title);
        values.put(Constants.TimeLists.about, about);
        values.put(Constants.TimeLists.color,color);
        values.put(Constants.TimeLists.visible,visable);
        return insert(Constants.TimeLists.TABLE_NAME, null, values);
    }
    public long insertContext(long TimeLists_Id,long geo,int author,int persona,int category,int timeline){
        ContentValues values = new ContentValues();
        values.put(Constants.Context.id, TimeLists_Id);
        if(geo!=-1){values.put(Constants.Context.geo, geo);}
        if(author!=-1){values.put(Constants.Context.author,author);}
        if(persona!=-1){values.put(Constants.Context.persona,persona);}
        if(category!=-1){values.put(Constants.Context.category,category);}
        if(timeline!=-1){values.put(Constants.Context.timeline,timeline);}
        return insert(Constants.Context.TABLE_NAME, null, values);
    }
    public long insertGeoList(int Nationality_id,int Continents_id,int Countrys_id,int Citys_id,int Streets_id){
        ContentValues values = new ContentValues();
        if(Nationality_id!=-1){values.put(GeoList.Nationality_id,Nationality_id);}
        if(Continents_id!=-1){values.put(GeoList.Continents_id,Continents_id);}
        if(Countrys_id!=-1){values.put(GeoList.Countrys_id,Countrys_id);}
        if(Citys_id!=-1){values.put(GeoList.Citys_id,Citys_id);}
        if(Streets_id!=-1){values.put(GeoList.Streets_id,Streets_id);}
        return insert(Constants.GeoList.TABLE_NAME, null, values);
    }

    /**query(
     * table,   - название таблиы к которой обращаемся
     * columns, WHERE
     * selection, НАЗВАНИЕ
     * selectionArgs, АРГУМЕНТ
     * groupBy, - GROUP BY
     * having, - выражение  после оператора HAVING
     * orderBy - ORDER BY сортировка);
     *
     * восстановленный sql запрос для наглядности.
     *
     * SELECT columns FROM table WHERE selection = selectionArgs GROUP BY groupBy HAVING having ORDER orderBy
     *
     * */

    //TODO: Прописать методы обновления данных UPDATE

    /**------------------------tables with primary keys -------------------------------------*/

    /**Аккуратно прописать первая таблица исключение в ней двойной ключ.*/
    public long updatetoAll_Key_Words(int id,int table_id,String word){
        ContentValues values = new ContentValues();
        values.put(Constants.All_Keywords.word,word);
        return update(Constants.All_Keywords.TABLE_NAME,values,Constants.All_Keywords.id+" = "+id+" AND "+Constants.All_Keywords.table_id+" ="+table_id,null);
    }
    public long updatetoCategory(long id,String name) {
        ContentValues values = new ContentValues();
        values.put( Constants.Category.name,name);
        return update(Constants.Category.TABLE_NAME,values, Constants.Category.id+" = "+String.valueOf(id), null);
    }
    public long updatetoAutor(long id,String author){
        ContentValues values = new ContentValues();
        values.put(Constants.Author.author,author);
        return  update(Constants.Author.TABLE_NAME,values,Constants.Author.id+" = "+String.valueOf(id),null);
    }

    public long updateContinent(int id,String continent){
        ContentValues values = new ContentValues();
        values.put(Constants.Continent.Continent,continent);
        return update(Constants.Continent.TABLE_NAME,values,Constants.Continent.id+" = "+String.valueOf(id),null);
    }

    public long updatetoCountry(int id,String country){
        ContentValues values = new ContentValues();
        values.put(Constants.Country.Country,country);
        return update(Constants.Country.TABLE_NAME,values,Constants.Country.id+" = "+String.valueOf(id),null);
    }
    public long updatetoCity(int id,String city){
        ContentValues values = new ContentValues();
        values.put(Constants.City.City,city);
        return update(Constants.City.TABLE_NAME,values,Constants.City.id+" = "+String.valueOf(id),null);
    }
    public long updatetoStreet(int id,String street){
        ContentValues values = new ContentValues();
        values.put(Constants.Street.Street,street);
        return update(Constants.Street.TABLE_NAME,values,Constants.Street.id+" = "+String.valueOf(id),null);
    }
    public long updatetoNationality(int id,String nationality){
        ContentValues values = new ContentValues();
        values.put(Constants.Nationality.Nationality,nationality);
        return update(Constants.Nationality.TABLE_NAME,values,Constants.Nationality.id+" = "+String.valueOf(id),null);
    }

    /**
     * @param start_date - дата рождения.
     * @param end_date - дата смерти.
     * в базе это TEXT , но для логики программы это DATE дата. */
    public long updatetoPerson(long id,String person,String start_date,String end_date){
        ContentValues values = new ContentValues();
        values.put(Constants.Person.person,person);
        values.put(Constants.Person.start_date,start_date);
        values.put(Constants.Person.end_date,end_date);
        return update(Constants.Person.TABLE_NAME,values,Constants.Person.id+" = "+String.valueOf(id),null);
    }
    public long updatetoPersonStartDate(int id,String start_date){
        ContentValues values = new ContentValues();
        values.put(Constants.Person.start_date,start_date);
        return update(Constants.Person.TABLE_NAME,values,Constants.Person.id+" = "+String.valueOf(id),null);
    }
    public long updatetoPersonEndDate(int id,String end_date){
        ContentValues values = new ContentValues();
        values.put(Constants.Person.end_date,end_date);
        return update(Constants.Person.TABLE_NAME,values,Constants.Person.id+" = "+String.valueOf(id),null);
    }

    /**----------------------all logic tables with foreign keys-----------------------------*/

    public long updatetoSynonym(int Keyword_id,int table_id,int Synonym_key_word_id){
        ContentValues values = new ContentValues();
        values.put(Constants.Synonym.Synonym_keyword_id,Synonym_key_word_id);
        return update(Constants.Synonym.TABLE_NAME,values,Constants.Synonym.Keyword_id+" = "+String.valueOf(Keyword_id)+
                " AND "+Constants.Synonym.table_id+" = "+String.valueOf(table_id),null);
    }

    public long updatetoGeo(long id,String Coordinates,long Nationality_id,long Continent_id,long Country_id,long City_id,long Street_id,String home){
        ContentValues values = new ContentValues();
        values.put(Constants.Geo.Coordinates,Coordinates);
        values.put(Constants.Geo.Nationality_id,Nationality_id);
        values.put(Constants.Geo.Continent_id,Continent_id);
        values.put(Constants.Geo.Country_id,Country_id);
        values.put(Constants.Geo.City_id,City_id);
        values.put(Constants.Geo.Street_id,Street_id);
        values.put(Constants.Geo.home,home);
        return update(Constants.Geo.TABLE_NAME,values,Constants.Geo.id+" = "+String.valueOf(id),null);
    }

    public long updatetoGeo(long id,String Coordinates,String Nationality,String Continent,String Country,String City,String Street,String home){
        Cursor NationalityCursor = query(Constants.Nationality.TABLE_NAME,
                new String[]{
                        Constants.Nationality.id,
                        Constants.Nationality.Nationality},
                Constants.Nationality.Nationality + " LIKE ? ",
                new String[] { Nationality },
                null, null, null);
        Cursor ContinentCursor = query(Constants.Continent.TABLE_NAME,
                new String[]{
                        Constants.Continent.id,
                        Constants.Continent.Continent},
                Constants.Continent.Continent + " LIKE ? ",
                new String[] { Continent },
                null, null, null);
        Cursor CountryCursor = query(Constants.Country.TABLE_NAME,
                new String[]{
                        Constants.Country.id,
                        Constants.Country.Country},
                Constants.Country.Country + " LIKE ? ",
                new String[] { Country },
                null, null, null);
        Cursor CityCursor = query(Constants.City.TABLE_NAME,
                new String[]{
                        Constants.City.id,
                        Constants.City.City},
                Constants.City.City + " LIKE ? ",
                new String[] { City },
                null, null, null);
        Cursor StreetCursor = query(Constants.Street.TABLE_NAME,
                new String[]{
                        Constants.Street.id,
                        Constants.Street.Street},
                Constants.Street.Street + " LIKE ? ",
                new String[] { Street },
                null, null, null);
        long Nationality_id;
        if(NationalityCursor != null) {
            Nationality_id = (NationalityCursor.moveToFirst()) ?
                    NationalityCursor.getInt(NationalityCursor.getColumnIndex(Constants.Nationality.id)) ://if - > найти запись в таблице
                    this.addtoNationality(Nationality);//else -> нету такой записи. добавить в таблицу и получить id
            NationalityCursor.close();
        }else{
            Nationality_id = this.addtoNationality(Nationality);
        }
        long Continent_id;
        if(ContinentCursor != null) {
            Continent_id = (ContinentCursor.moveToFirst()) ?
                    ContinentCursor.getInt(ContinentCursor.getColumnIndex(Constants.Continent.id)) :
                    this.addtoContinents(Continent);
            ContinentCursor.close();
        }else{
            Continent_id = this.addtoContinents(Continent);
        }
        long Country_id;
        if(CountryCursor != null) {
            Country_id = (CountryCursor.moveToFirst()) ?
                    CountryCursor.getInt(CountryCursor.getColumnIndex(Constants.Country.id)) :
                    this.addtoCountrys(Country);
            CountryCursor.close();
        }else{
            Country_id = this.addtoCountrys(Country);
        }
        long City_id;
        if(CityCursor != null) {
            City_id = (CityCursor.moveToFirst()) ?
                    CityCursor.getInt(CityCursor.getColumnIndex(Constants.City.id)) :
                    this.addtoSitys(City);
            CityCursor.close();
        }else{
            City_id = this.addtoSitys(City);
        }
        long Street_id;
        if(StreetCursor != null) {
            Street_id = (StreetCursor.moveToFirst()) ?
                    StreetCursor.getInt(StreetCursor.getColumnIndex(Constants.Street.id)) :
                    this.addtoStreets(Street);
            StreetCursor.close();
        }else{
            Street_id = this.addtoStreets(Street);
        }
        return updatetoGeo(id, Coordinates, Nationality_id, Continent_id, Country_id, City_id, Street_id,home);
    }

    public long updatetoGeoCoordinates(int id,String Coordinates){
        ContentValues values = new ContentValues();
        values.put(Constants.Geo.Coordinates,Coordinates);
        return update(Constants.Geo.TABLE_NAME,values,Constants.Geo.id+" = "+String.valueOf(id),null);
    }
    public long updatetoGeoNationalityId(int id,int Nationality_id){
        ContentValues values = new ContentValues();
        values.put(Constants.Geo.Nationality_id,Nationality_id);
        return update(Constants.Geo.TABLE_NAME,values,Constants.Geo.id+" = "+String.valueOf(id),null);
    }
    public long updatetoGeoContinentId(int id,int Continent_id){
        ContentValues values = new ContentValues();
        values.put(Constants.Geo.Continent_id,Continent_id);
        return update(Constants.Geo.TABLE_NAME,values,Constants.Geo.id+" = "+String.valueOf(id),null);
    }
    public long updatetoGeoCountryId(int id,int Country_id){
        ContentValues values = new ContentValues();
        values.put(Constants.Geo.Country_id,Country_id);
        return update(Constants.Geo.TABLE_NAME,values,Constants.Geo.id+" = "+String.valueOf(id),null);
    }
    public long updatetoGeoSityId(int id,int Sity_id){
        ContentValues values = new ContentValues();
        values.put(Constants.Geo.City_id,Sity_id);
        return update(Constants.Geo.TABLE_NAME,values,Constants.Geo.id+" = "+String.valueOf(id),null);
    }
    public long updatetoGeoStreet_id(int id,int Street_id){
        ContentValues values = new ContentValues();
        values.put(Constants.Geo.Street_id,Street_id);
        return update(Constants.Geo.TABLE_NAME,values,Constants.Geo.id+" = "+String.valueOf(id),null);
    }
    public long updatetoGeoHome(int id,String home){
        ContentValues values = new ContentValues();
        values.put(Constants.Geo.home,home);
        return update(Constants.Geo.TABLE_NAME,values,Constants.Geo.id+" = "+String.valueOf(id),null);
    }

    /**
     * @param start_date - дата начала события.
     * @param end_date - дата конца события если есть.
     * в базе это TEXT , но для логики программы это DATE дата. */
    public long updatetoEvent(int id,int geo_id,String body,String title,String start_date,String end_date,int author_id,int privacy,int category_id){
        ContentValues values = new ContentValues();
        values.put(Constants.Event.geo_id,geo_id);
        values.put(Constants.Event.body,body);
        values.put(Constants.Event.title,title);
        values.put(Constants.Event.start_date,start_date);
        values.put(Constants.Event.end_date,end_date);
        values.put(Constants.Event.author_id,author_id);
        values.put(Constants.Event.privacy,privacy);
        values.put(Constants.Event.category_id,category_id);
        return update(Constants.Event.TABLE_NAME,values,Constants.Event.id+" = "+String.valueOf(id),null);
    }
    public long updatetoEventGeoId(int id,int geo_id){
        ContentValues values = new ContentValues();
        values.put(Constants.Event.geo_id,geo_id);
        return update(Constants.Event.TABLE_NAME,values,Constants.Event.id+" = "+String.valueOf(id),null);
    }
    public long updatetoEventBody(long id,String body){
        ContentValues values = new ContentValues();
        values.put(Constants.Event.body,body);
        return update(Constants.Event.TABLE_NAME,values,Constants.Event.id+" = "+String.valueOf(id),null);
    }
    public long updatetoEventTitle(long id,String title){
        ContentValues values = new ContentValues();
        values.put(Constants.Event.title,title);
        return update(Constants.Event.TABLE_NAME,values,Constants.Event.id+" = "+String.valueOf(id),null);
    }
    public long updatetoEventStartDate(long id,String start_date){
        ContentValues values = new ContentValues();
        values.put(Constants.Event.start_date,start_date);
        return update(Constants.Event.TABLE_NAME,values,Constants.Event.id+" = "+String.valueOf(id),null);
    }
    public long updatetoEventEndDate(long id,String end_date){
        ContentValues values = new ContentValues();
        values.put(Constants.Event.end_date,end_date);
        return update(Constants.Event.TABLE_NAME,values,Constants.Event.id+" = "+String.valueOf(id),null);
    }
    public long updatetoEventAuthorId(long id,long author_id){
        ContentValues values = new ContentValues();
        values.put(Constants.Event.author_id,author_id);
        return update(Constants.Event.TABLE_NAME,values,Constants.Event.id+" = "+String.valueOf(id),null);
    }
    public long updatetoEventPrivacy(long id,int privacy){
        ContentValues values = new ContentValues();
        values.put(Constants.Event.privacy,privacy);
        return update(Constants.Event.TABLE_NAME,values,Constants.Event.id+" = "+String.valueOf(id),null);
    }
    public long updatetoEventCategoryId(long id,long category_id){
        ContentValues values = new ContentValues();
        values.put(Constants.Event.category_id,category_id);
        return update(Constants.Event.TABLE_NAME,values,Constants.Event.id+" = "+String.valueOf(id),null);
    }

    /**
     * @param start_date - дата начала пребывания персоны в этом месте.
     * @param end_date - дата конца пребывания.
     * в базе это TEXT , но для логики программы это DATE дата. */
    public long updatetoPerson_geo(int id,String start_date,String end_date,int geo_id){
        ContentValues values = new ContentValues();
        values.put(Constants.Person_geo.start_date,start_date);
        values.put(Constants.Person_geo.end_date,end_date);
        values.put(Constants.Person_geo.geo_id,geo_id);
        return update(Constants.Person_geo.TABLE_NAME,values,Constants.Person_geo.persona_id+" = "+String.valueOf(id),null);
    }
    public long updatetoPerson_geoStartDate(int id,String start_date){
        ContentValues values = new ContentValues();
        values.put(Constants.Person_geo.start_date,start_date);
        return update(Constants.Person_geo.TABLE_NAME,values,Constants.Person_geo.persona_id+" = "+String.valueOf(id),null);
    }
    public long updatetoPerson_geoEndDate(int id,String end_date){
        ContentValues values = new ContentValues();
        values.put(Constants.Person_geo.end_date,end_date);
        return update(Constants.Person_geo.TABLE_NAME,values,Constants.Person_geo.persona_id+" = "+String.valueOf(id),null);
    }
    public long updatetoPerson_geoGeoId(int id,int geo_id){
        ContentValues values = new ContentValues();
        values.put(Constants.Person_geo.geo_id,geo_id);
        return update(Constants.Person_geo.TABLE_NAME,values,Constants.Person_geo.persona_id+" = "+String.valueOf(id),null);
    }
    public long updatetoEvent_and_PersonbyPerson (int persona,int persona_Event){
        ContentValues values = new ContentValues();
        values.put(Constants.Event_and_Person.Persona_Event,persona_Event);
        return update(Constants.Event_and_Person.TABLE_NAME,values,Constants.Event_and_Person.Persona+" = "+String.valueOf(persona),null);
    }
    public long updatetoEvent_and_PersonbyPersonEvent (int persona_Event, int persona ){
        ContentValues values = new ContentValues();
        values.put(Constants.Event_and_Person.Persona,persona);
        return update(Constants.Event_and_Person.TABLE_NAME,values,Constants.Event_and_Person.Persona_Event+" = "+String.valueOf(persona_Event),null);
    }
    /** поле Table_N всегда только 4. */
    public long updatetoPerson_Keyword(int persona,int Keyword){
        ContentValues values = new ContentValues();
        values.put(Constants.Person_Keyword.Keyword,Keyword);
        return update(Constants.Person_Keyword.TABLE_NAME,values,Constants.Person_Keyword.Persona+" = "+String.valueOf(persona),null);
    }
    public long updatetoAssociation(long id,long event_id_1,long event_id_2,String connection,int author_id,int privacy){
        ContentValues values = new ContentValues();
        values.put(Constants.Association.event_id_1,event_id_1);
        values.put(Constants.Association.event_id_2,event_id_2);
        values.put(Constants.Association.connection,connection);
        values.put(Constants.Association.author_id,author_id);
        values.put(Constants.Association.privacy,privacy);
        return update(Constants.Association.TABLE_NAME,values,Constants.Association.id+" = "+String.valueOf(id),null);
    }
    public long updatetoAssociationbyEventId1(int event_id_1,int event_id_2,String connection,int author_id,int privacy){
        ContentValues values = new ContentValues();
        values.put(Constants.Association.event_id_2,event_id_2);
        values.put(Constants.Association.connection,connection);
        values.put(Constants.Association.author_id,author_id);
        values.put(Constants.Association.privacy,privacy);
        return update(Constants.Association.TABLE_NAME,values,Constants.Association.event_id_1+" = "+String.valueOf(event_id_1),null);
    }
    public long updatetoAssociationbyEventId2(int event_id_2,int event_id_1,String connection,int author_id,int privacy){
        ContentValues values = new ContentValues();
        values.put(Constants.Association.event_id_1,event_id_1);
        values.put(Constants.Association.connection,connection);
        values.put(Constants.Association.author_id,author_id);
        values.put(Constants.Association.privacy,privacy);
        return update(Constants.Association.TABLE_NAME,values,Constants.Association.event_id_2+" = "+String.valueOf(event_id_2),null);
    }
    /** поле Table_N всегда только 3. */
    public long updatetoMarkbyKeyword_id(int Keyword_id,int event_id){
        ContentValues values = new ContentValues();
        values.put(Constants.Mark.event_id,event_id);
        return update(Constants.Mark.TABLE_NAME,values,Constants.Mark.Keyword_id+" = "+String.valueOf(Keyword_id),null);
    }
    /** поле Table_N всегда только 3. */
    public long updatetoMarkbyEvent(int event_id,int Keyword_id){
        ContentValues values = new ContentValues();
        values.put(Constants.Mark.Keyword_id,Keyword_id);
        return update(Constants.Mark.TABLE_NAME,values,Constants.Mark.event_id+" = "+String.valueOf(event_id),null);
    }
    /** поле Table_N всегда только 2.*/
    public long updatetoCategory_Keyword(int Category,int  Keyword){
        ContentValues values = new ContentValues();
        values.put(Constants.Category_Keyword.Keyword,Keyword);
        return update(Constants.Category_Keyword.TABLE_NAME,values,Constants.Category_Keyword.Category +" = "+String.valueOf(Category),null);
    }
    /** поле Table_N всегда только 1. */
    public long updatetoAuthor_Keyword(int Author,int Keyword){
        ContentValues values = new ContentValues();
        values.put(Constants.Author_Keyword.Keyword,Keyword);
        return update(Constants.Author_Keyword.TABLE_NAME,values,Constants.Author_Keyword.Author+" = "+String.valueOf(Author),null);
    }
    /**
     *@param id ссылка на id таблицы Events
     *@param link - url страница в интернете .*/
    public long updatetoLinks (int id,String link){
        ContentValues values = new ContentValues();
        values.put(Constants.Links.link,link);
        return update(Constants.Links.TABLE_NAME,values,Constants.Links.id+" = "+String.valueOf(id),null);
    }

    /**--------------------------Logic for time list---------------------------- */

    public long updatetoTimeLists (long id,String title,String about,int color,boolean visable){
        ContentValues values = new ContentValues();
        values.put(Constants.TimeLists.title,title);
        values.put(Constants.TimeLists.about,about);
        values.put(Constants.TimeLists.color,color);
        int int_visible = TimeListItem.NOT_VISIBLE;
        if(visable){int_visible = TimeListItem.VISIBLE;}
        values.put(Constants.TimeLists.visible,int_visible );
        return update(Constants.TimeLists.TABLE_NAME,values,Constants.TimeLists.id+" = "+String.valueOf(id),null);
    }
    public long updatetoTimeListsTitle (int id,String title){
        ContentValues values = new ContentValues();
        values.put(Constants.TimeLists.title,title);
        return update(Constants.TimeLists.TABLE_NAME,values,Constants.TimeLists.id+" = "+String.valueOf(id),null);
    }
    public long updatetoTimeListsAbout (int id,String about){
        ContentValues values = new ContentValues();
        values.put(Constants.TimeLists.about,about);
        return update(Constants.TimeLists.TABLE_NAME,values,Constants.TimeLists.id+" = "+String.valueOf(id),null);
    }
    public long updatetoTimeListsColor (int id,int color){
        ContentValues values = new ContentValues();
        values.put(Constants.TimeLists.color,color);
        return update(Constants.TimeLists.TABLE_NAME,values,Constants.TimeLists.id+" = "+String.valueOf(id),null);
    }
    public long updatetoTimeListsVisable (int id,boolean visable){
        ContentValues values = new ContentValues();
        int int_visable = 0;
        if(visable){int_visable = 1;}
        values.put(Constants.TimeLists.visible,int_visable );
        return update(Constants.TimeLists.TABLE_NAME,values,Constants.TimeLists.id+" = "+String.valueOf(id),null);
    }
    public long updatetoEventList(int TimeList_id,int Event_id,int x,int y){
        ContentValues values = new ContentValues();
        values.put(Constants.EventList.x,x);
        values.put(Constants.EventList.y,y);
        return update(Constants.EventList.TABLE_NAME,values,Constants.EventList.TimeList_id+" = "+String.valueOf(TimeList_id)+" AND "+
                Constants.EventList.Event_id+" = "+String.valueOf(Event_id),null);
    }
    public long updatetoGeoList(int id,int Nationality_id,int Continents_id,int Citys_id,int Countrys_id,int Streets_id){
        ContentValues values = new ContentValues();
        values.put(Constants.GeoList.Nationality_id,Nationality_id);
        values.put(Constants.GeoList.Continents_id,Continents_id);
        values.put(Constants.GeoList.Citys_id,Citys_id);
        values.put(Constants.GeoList.Countrys_id,Countrys_id);
        values.put(Constants.GeoList.Streets_id,Streets_id);
        return update(Constants.GeoList.TABLE_NAME,values,Constants.GeoList.id+" = "+String.valueOf(id),null);
    }
    /**Спорный вопрос о необходимости дополнительных функций для этой таблицы в случае надобности добавить*/
    public long updatetoContext(int from_id,int geo,int author,int persona,int category,int timeline){
        ContentValues values = new ContentValues();
        values.put(Constants.Context.geo, geo);
        values.put(Constants.Context.author,author);
        values.put(Constants.Context.persona, persona);
        values.put(Constants.Context.category, category);
        values.put(Constants.Context.timeline, timeline);
        return update(Constants.Context.TABLE_NAME, values, Constants.Context.id+" = ?" ,new String[] {String.valueOf(from_id)});
    }
    public long updatetoContext(int[] from_id,int geo,int author,int persona,int category,int timeline){
        ContentValues values = new ContentValues();
        values.put(Constants.Context.geo, geo);
        values.put(Constants.Context.author,author);
        values.put(Constants.Context.persona, persona);
        values.put(Constants.Context.category, category);
        values.put(Constants.Context.timeline, timeline);
        String whereArgs[] = new String[from_id.length];
        for(int i=0;i<from_id.length;i++){
            whereArgs[i] = String.valueOf(from_id[i]);
        }
        return update(Constants.Context.TABLE_NAME, values, Constants.Context.id , whereArgs);
    }
    //Прописать поисковые запросы SELECT будут расписанны по мере необходимости
    // т.к. исходно не определить какие могут понадобиться (исправленно - прописанны почти все что требуются)
    /**
     * @param id - номер списка сохранённого для выборки по нему
     * @param Flag1 - Nationality (true - 'and', false - 'or') для остальных так же
     * @param Flag2 - Continents
     * @param Flag3 - Countrys
     * @param Flag4 - Citys,
     *Это  Самый важный запрос из базы  единичный**/
    public Cursor queryEventByTimeLists(long id,boolean Flag1,boolean Flag2,boolean Flag3,boolean Flag4){
        String SELECT =" SELECT "+
                //Event
                Constants.Event.TABLE_NAME+"."+Constants.Event.id+" as "+Constants._ID+","+
                Constants.Event.TABLE_NAME+"."+Constants.Event.geo_id+" as "+Constants.Event.geo_id+","+
                Constants.Event.TABLE_NAME+"."+Constants.Event.title+" as "+Constants.Event.title+","+
                Constants.Event.TABLE_NAME+"."+Constants.Event.body+" as "+Constants.Event.body+","+
                Constants.Event.TABLE_NAME+"."+Constants.Event.start_date+" as "+Constants.Event.start_date+","+
                Constants.Event.TABLE_NAME+"."+Constants.Event.end_date+" as "+Constants.Event.end_date+","+
                Constants.Event.TABLE_NAME+"."+Constants.Event.category_id+" as "+Constants.Event.category_id+","+
                Constants.Event.TABLE_NAME+"."+Constants.Event.author_id+" as "+Constants.Event.author_id+","+
                Constants.Event.TABLE_NAME+"."+Constants.Event.privacy+" as "+Constants.Event.privacy+","+
                //TimeLists
                Constants.TimeLists.TABLE_NAME+"."+Constants.TimeLists.id+" as TimeLists_id"+","+
                Constants.TimeLists.TABLE_NAME+"."+Constants.TimeLists.color+" as "+Constants.TimeLists.color+","+
                Constants.TimeLists.TABLE_NAME+"."+Constants.TimeLists.visible +" as "+Constants.TimeLists.visible;
        //поиск по Category
        String FROM1 = " FROM "+
                Constants.TimeLists.TABLE_NAME+","+Constants.Context.TABLE_NAME+","+Constants.Event.TABLE_NAME;
        String WHERE1 = " WHERE "+
                Constants.TimeLists.TABLE_NAME+"."+Constants.TimeLists.id+" = "+id+" and "+
                Constants.Context.TABLE_NAME+"."+Constants.Context.id+" = "+Constants.TimeLists.TABLE_NAME+"."+Constants.TimeLists.id+" and "+
                Constants.Context.TABLE_NAME+"."+Constants.Context.category+" = "+Constants.Event.TABLE_NAME+"."+Constants.Event.category_id;
        //поиск по Author
        String WHERE2 = " WHERE "+
                Constants.TimeLists.TABLE_NAME+"."+Constants.TimeLists.id+" = "+id+" and "+
                Constants.Context.TABLE_NAME+"."+Constants.Context.id+" = "+Constants.TimeLists.TABLE_NAME+"."+Constants.TimeLists.id+" and "+
                Constants.Context.TABLE_NAME+"."+Constants.Context.author+" = "+Constants.Event.TABLE_NAME+"."+Constants.Event.author_id;
        //поиск по Person
        String FROM3 = " FROM "+
                Constants.TimeLists.TABLE_NAME+","+Constants.Context.TABLE_NAME+","+Constants.Event.TABLE_NAME+","+Constants.Event_and_Person.TABLE_NAME;
        String WHERE3 = " WHERE "+
                Constants.TimeLists.TABLE_NAME+"."+Constants.TimeLists.id+" = "+id+" and "+
                Constants.Context.TABLE_NAME+"."+Constants.Context.id+"="+Constants.TimeLists.TABLE_NAME+"."+Constants.TimeLists.id+" and "+
                Constants.Context.TABLE_NAME+"."+Constants.Context.persona+" = "+Constants.Event_and_Person.TABLE_NAME+"."+Constants.Event_and_Person.Persona +" and "+
                Constants.Event_and_Person.TABLE_NAME+"."+Constants.Event_and_Person.Persona_Event+" = "+Constants.Event.TABLE_NAME+"."+Constants.Event.id;
        //поиск по Timeline(Mark)
        String FROM4 = " FROM "+
                Constants.TimeLists.TABLE_NAME+","+Constants.Context.TABLE_NAME+","+Constants.Event.TABLE_NAME+","+Constants.Mark.TABLE_NAME;
        String WHERE4 = " WHERE "+
                Constants.TimeLists.TABLE_NAME+"."+Constants.TimeLists.id+" = "+id+" and "+
                Constants.Context.TABLE_NAME+"."+Constants.Context.id+" = "+Constants.TimeLists.TABLE_NAME+"."+Constants.TimeLists.id+ " and "+
                Constants.Context.TABLE_NAME+"."+Constants.Context.timeline+" = "+Constants.Mark.TABLE_NAME+"."+Constants.Mark.TimeLine_id+ " and "+
                Constants.Mark.TABLE_NAME+"."+Constants.Mark.event_id+" = "+Constants.Event.TABLE_NAME+"."+Constants.Event.id;
        //поиск по Geo
        String FROM5 = " FROM "+
                Constants.TimeLists.TABLE_NAME+","+Constants.Context.TABLE_NAME+","+Constants.GeoList.TABLE_NAME+","+Constants.Geo.TABLE_NAME+","+Constants.Event.TABLE_NAME;
        String WHERE5part = " WHERE "+
                Constants.TimeLists.TABLE_NAME+"."+Constants.TimeLists.id+" = "+id+" and "+
                Constants.Context.TABLE_NAME+"."+Constants.Context.id+" = "+Constants.TimeLists.TABLE_NAME+"."+Constants.TimeLists.id+" and "+
                Constants.Context.TABLE_NAME+"."+Constants.Context.geo+" = "+Constants.GeoList.TABLE_NAME+"."+Constants.GeoList.id+" and ";

        //параметризируемые условия or или and для geo соответствия
        String Naoperator = Flag1?" and ":" or ";
        String ConOperator = Flag2?" and ":" or ";
        String CouOperator = Flag3?" and ":" or ";
        String CiOperator = Flag4?" and ":" or ";

        String GEOPart = "("+
                Constants.GeoList.TABLE_NAME+"."+Constants.GeoList.Nationality_id+" = "+
                Constants.Geo.TABLE_NAME+"."+Constants.Geo.Nationality_id+
                Naoperator+
                Constants.GeoList.TABLE_NAME+"."+Constants.GeoList.Continents_id+" = "+
                Constants.Geo.TABLE_NAME+"."+Constants.Geo.Continent_id+
                ConOperator+
                Constants.GeoList.TABLE_NAME+"."+Constants.GeoList.Countrys_id+" = "+
                Constants.Geo.TABLE_NAME+"."+Constants.Geo.Country_id+
                CouOperator+
                Constants.GeoList.TABLE_NAME+"."+Constants.GeoList.Citys_id+" = "+
                Constants.Geo.TABLE_NAME+"."+Constants.Geo.City_id+
                CiOperator+
                Constants.GeoList.TABLE_NAME+"."+Constants.GeoList.Streets_id+" = "+
                Constants.Geo.TABLE_NAME+"."+Constants.Geo.Street_id+")"+" and "+
                Constants.Geo.TABLE_NAME+"."+Constants.Geo.id+" = "+Constants.Event.TABLE_NAME+"."+Constants.Event.geo_id;
        String WHERE5 = WHERE5part+GEOPart;
        //объединяем все запросы
        String UNION =
                SELECT+FROM1+WHERE1+" UNION "+
                        SELECT+FROM1+WHERE2+" UNION "+
                        SELECT+FROM3+WHERE3+" UNION "+
                        SELECT+FROM4+WHERE4+" UNION "+
                        SELECT+FROM5+WHERE5+";";

        return rawQuery(UNION, null);
    }

    public Cursor queryEventByTimeListsMulti(long id[],boolean Flag1,boolean Flag2,boolean Flag3,boolean Flag4){
        if(id!=null){
            if(id.length > 1){
                String TimelineIdArray=" ( ";
                for(int i=0;i<id.length;i++){
                    if(i<id.length-1){
                        TimelineIdArray = TimelineIdArray + Constants.TimeLists.TABLE_NAME+"."+Constants.TimeLists.id+" = "+String.valueOf(id[i])+" or ";
                    }else{
                        TimelineIdArray = TimelineIdArray + Constants.TimeLists.TABLE_NAME+"."+Constants.TimeLists.id+" = "+String.valueOf(id[i])+" ) ";
                    }
                }

                String SELECT =" SELECT "+
                        //Event
                        Constants.Event.TABLE_NAME+"."+Constants.Event.id+" as "+Constants._ID+","+
                        Constants.Event.TABLE_NAME+"."+Constants.Event.geo_id+" as "+Constants.Event.geo_id+","+
                        Constants.Event.TABLE_NAME+"."+Constants.Event.title+" as "+Constants.Event.title+","+
                        Constants.Event.TABLE_NAME+"."+Constants.Event.body+" as "+Constants.Event.body+","+
                        Constants.Event.TABLE_NAME+"."+Constants.Event.start_date+" as "+Constants.Event.start_date+","+
                        Constants.Event.TABLE_NAME+"."+Constants.Event.end_date+" as "+Constants.Event.end_date+","+
                        Constants.Event.TABLE_NAME+"."+Constants.Event.category_id+" as "+Constants.Event.category_id+","+
                        Constants.Event.TABLE_NAME+"."+Constants.Event.author_id+" as "+Constants.Event.author_id+","+
                        Constants.Event.TABLE_NAME+"."+Constants.Event.privacy+" as "+Constants.Event.privacy+","+
                        //TimeLists
                        Constants.TimeLists.TABLE_NAME+"."+Constants.TimeLists.id+" as TimeLists_id"+","+
                        Constants.TimeLists.TABLE_NAME+"."+Constants.TimeLists.color+" as "+Constants.TimeLists.color+","+
                        Constants.TimeLists.TABLE_NAME+"."+Constants.TimeLists.visible +" as "+Constants.TimeLists.visible;
                //поиск по Category
                String FROM1 = " FROM "+
                        Constants.TimeLists.TABLE_NAME+","+Constants.Context.TABLE_NAME+","+Constants.Event.TABLE_NAME;
                String WHERE1 = " WHERE "+
                        TimelineIdArray+" and "+
                        Constants.Context.TABLE_NAME+"."+Constants.Context.id+" = "+Constants.TimeLists.TABLE_NAME+"."+Constants.TimeLists.id+" and "+
                        Constants.Context.TABLE_NAME+"."+Constants.Context.category+" = "+Constants.Event.TABLE_NAME+"."+Constants.Event.category_id;
                //поиск по Author
                String WHERE2 = " WHERE "+
                        TimelineIdArray+" and "+
                        Constants.Context.TABLE_NAME+"."+Constants.Context.id+" = "+Constants.TimeLists.TABLE_NAME+"."+Constants.TimeLists.id+" and "+
                        Constants.Context.TABLE_NAME+"."+Constants.Context.author+" = "+Constants.Event.TABLE_NAME+"."+Constants.Event.author_id;
                //поиск по Person
                String FROM3 = " FROM "+
                        Constants.TimeLists.TABLE_NAME+","+Constants.Context.TABLE_NAME+","+Constants.Event.TABLE_NAME+","+Constants.Event_and_Person.TABLE_NAME;
                String WHERE3 = " WHERE "+
                        TimelineIdArray+" and "+
                        Constants.Context.TABLE_NAME+"."+Constants.Context.id+"="+Constants.TimeLists.TABLE_NAME+"."+Constants.TimeLists.id+" and "+
                        Constants.Context.TABLE_NAME+"."+Constants.Context.persona+" = "+Constants.Event_and_Person.TABLE_NAME+"."+Constants.Event_and_Person.Persona +" and "+
                        Constants.Event_and_Person.TABLE_NAME+"."+Constants.Event_and_Person.Persona_Event+" = "+Constants.Event.TABLE_NAME+"."+Constants.Event.id;
                //поиск по Timeline(Mark)
                String FROM4 = " FROM "+
                        Constants.TimeLists.TABLE_NAME+","+Constants.Context.TABLE_NAME+","+Constants.Event.TABLE_NAME+","+Constants.Mark.TABLE_NAME;
                String WHERE4 = " WHERE "+
                        TimelineIdArray+" and "+
                        Constants.Context.TABLE_NAME+"."+Constants.Context.id+" = "+Constants.TimeLists.TABLE_NAME+"."+Constants.TimeLists.id+ " and "+
                        Constants.Context.TABLE_NAME+"."+Constants.Context.timeline+" = "+Constants.Mark.TABLE_NAME+"."+Constants.Mark.TimeLine_id+ " and "+
                        Constants.Mark.TABLE_NAME+"."+Constants.Mark.event_id+" = "+Constants.Event.TABLE_NAME+"."+Constants.Event.id;
                //поиск по Geo
                String FROM5 = " FROM "+
                        Constants.TimeLists.TABLE_NAME+","+Constants.Context.TABLE_NAME+","+Constants.GeoList.TABLE_NAME+","+Constants.Geo.TABLE_NAME+","+Constants.Event.TABLE_NAME;
                String WHERE5part = " WHERE "+
                        TimelineIdArray+" and "+
                        Constants.Context.TABLE_NAME+"."+Constants.Context.id+" = "+Constants.TimeLists.TABLE_NAME+"."+Constants.TimeLists.id+" and "+
                        Constants.Context.TABLE_NAME+"."+Constants.Context.geo+" = "+Constants.GeoList.TABLE_NAME+"."+Constants.GeoList.id+" and ";

                //параметризируемые условия or или and для geo соответствия
                String Naoperator = Flag1?" and ":" or ";
                String ConOperator = Flag2?" and ":" or ";
                String CouOperator = Flag3?" and ":" or ";
                String CiOperator = Flag4?" and ":" or ";

                String GEOPart = "("+
                        Constants.GeoList.TABLE_NAME+"."+Constants.GeoList.Nationality_id+" = "+
                        Constants.Geo.TABLE_NAME+"."+Constants.Geo.Nationality_id+
                        Naoperator+
                        Constants.GeoList.TABLE_NAME+"."+Constants.GeoList.Continents_id+" = "+
                        Constants.Geo.TABLE_NAME+"."+Constants.Geo.Continent_id+
                        ConOperator+
                        Constants.GeoList.TABLE_NAME+"."+Constants.GeoList.Countrys_id+" = "+
                        Constants.Geo.TABLE_NAME+"."+Constants.Geo.Country_id+
                        CouOperator+
                        Constants.GeoList.TABLE_NAME+"."+Constants.GeoList.Citys_id+" = "+
                        Constants.Geo.TABLE_NAME+"."+Constants.Geo.City_id+
                        CiOperator+
                        Constants.GeoList.TABLE_NAME+"."+Constants.GeoList.Streets_id+" = "+
                        Constants.Geo.TABLE_NAME+"."+Constants.Geo.Street_id+")"+" and "+
                        Constants.Geo.TABLE_NAME+"."+Constants.Geo.id+" = "+Constants.Event.TABLE_NAME+"."+Constants.Event.geo_id;
                String WHERE5 = WHERE5part+GEOPart;
                //объединяем все запросы
                String UNION =
                        SELECT+FROM1+WHERE1+" UNION "+
                                SELECT+FROM1+WHERE2+" UNION "+
                                SELECT+FROM3+WHERE3+" UNION "+
                                SELECT+FROM4+WHERE4+" UNION "+
                                SELECT+FROM5+WHERE5+";";

                return rawQuery(UNION, null);
            }else{
                if(id.length==1){
                    return queryEventByTimeLists(id[0], Flag1, Flag2, Flag3, Flag4);
                }
                else{return null;}
            }
        }else{return null;}
    }

    public Cursor queryEventByID(long id[]){
        String where = "";
        String args[] = new String[id.length];
        for(int i = 0; i < id.length; i++){
            if(i < (id.length-1)) {
                where = where + Constants.Event.id + "=? OR ";
            }else{
                where = where + Constants.Event.id + "=?";
            }
            args[i] = String.valueOf(id[i]);
        }
        return	mTimeLineOpenHelper.mDatabase.query(Constants.Event.TABLE_NAME,
                new String[]{
                        Constants.Event.id,
                        Constants.Event.geo_id,
                        Constants.Event.body,
                        Constants.Event.title,
                        Constants.Event.start_date,
                        Constants.Event.end_date,
                        Constants.Event.author_id,
                        Constants.Event.category_id,
                        Constants.Event.privacy},
                where,
                args,
                null, null, null);
    }
    public Cursor queryEventByTitles(String Titles[]){
        String where = "";
        String args[] = new String[Titles.length];
        for(int i = 0; i < Titles.length; i++){
            if(i < (Titles.length-1)) {
                where = where + Constants.Event.title + "=? OR ";
            }else{
                where = where + Constants.Event.title + "=?";
            }
            args[i] = Titles[i];
        }
        return	mTimeLineOpenHelper.mDatabase.query(Constants.Event.TABLE_NAME,
                new String[]{
                        Constants.Event.id,
                        Constants.Event.geo_id,
                        Constants.Event.body,
                        Constants.Event.title,
                        Constants.Event.start_date,
                        Constants.Event.end_date,
                        Constants.Event.author_id,
                        Constants.Event.category_id,
                        Constants.Event.privacy},
                where,
                args,
                null, null, null);
    }
    public Cursor queryGeoByID(long id){
        return	mTimeLineOpenHelper.mDatabase.query(Constants.Geo.TABLE_NAME,
                new String[]{
                        Constants.Geo.id,
                        Constants.Geo.Coordinates,
                        Constants.Geo.Nationality_id,
                        Constants.Geo.Continent_id,
                        Constants.Geo.Country_id,
                        Constants.Geo.City_id,
                        Constants.Geo.Street_id,
                        Constants.Geo.home},
                Constants.Geo.id + "=?" ,
                new String[] { String.valueOf(id)},
                null, null, null);
    }
    public Cursor queryNationalityByID(long id){
        return	mTimeLineOpenHelper.mDatabase.query(Constants.Nationality.TABLE_NAME,
                new String[]{
                        Constants.Nationality.id,
                        Constants.Nationality.Nationality},
                Constants.Nationality.id + "=?" ,
                new String[] { String.valueOf(id)},
                null, null, null);
    }
    public Cursor queryContinentByID(long id){
        return	mTimeLineOpenHelper.mDatabase.query(Constants.Continent.TABLE_NAME,
                new String[]{
                        Constants.Continent.id,
                        Constants.Continent.Continent},
                Constants.Continent.id + "=?" ,
                new String[] { String.valueOf(id)},
                null, null, null);
    }
    public Cursor queryCountryByID(long id){
        return	mTimeLineOpenHelper.mDatabase.query(Constants.Country.TABLE_NAME,
                new String[]{
                        Constants.Country.id,
                        Constants.Country.Country},
                Constants.Continent.id + "=?" ,
                new String[] { String.valueOf(id)},
                null, null, null);
    }
    public Cursor queryCityByID(long id){
        return	mTimeLineOpenHelper.mDatabase.query(Constants.City.TABLE_NAME,
                new String[]{
                        Constants.City.id,
                        Constants.City.City},
                Constants.City.id + "=?" ,
                new String[] { String.valueOf(id)},
                null, null, null);
    }
    public Cursor queryStreetByID(long id){
        return	mTimeLineOpenHelper.mDatabase.query(Constants.Street.TABLE_NAME,
                new String[]{
                        Constants.Street.id,
                        Constants.Street.Street},
                Constants.Street.id + "=?" ,
                new String[] { String.valueOf(id)},
                null, null, null);
    }
    public Cursor queryLinksByID(long id){
        return	mTimeLineOpenHelper.mDatabase.query(
                Constants.Links.TABLE_NAME,
                new String[]{
                        Constants.Links.id,
                        Constants.Links.link},
                Constants.Links.id + "=?" ,
                new String[]{String.valueOf(id)},
                null, null, null);
    }


    public Cursor queryTimeListsByID(long id){
        return mTimeLineOpenHelper.mDatabase.query(Constants.TimeLists.TABLE_NAME,
                new String[]{
                        Constants.TimeLists.id,
                        Constants.TimeLists.title,
                        Constants.TimeLists.about,
                        Constants.TimeLists.color,
                        Constants.TimeLists.visible
                },
                Constants.TimeLists.id + "=?" ,
                new String[]{String.valueOf(id)},
                null, null, null);
    }

    public Cursor queryAuthorByID(long id){
        return mTimeLineOpenHelper.mDatabase.query(Constants.Author.TABLE_NAME,
                new String[]{
                        Constants.Author.id,
                        Constants.Author.author
                },
                Constants.Author.id + "=?" ,
                new String[]{String.valueOf(id)},
                null, null, null);
    }

    public Cursor queryAuthorByName(String name){
        return mTimeLineOpenHelper.mDatabase.query(Constants.Author.TABLE_NAME,
                new String[]{
                        Constants.Author.id,
                        Constants.Author.author
                },
                Constants.Author.author + "=?" ,
                new String[]{name},
                null, null, null);
    }

    public Cursor queryAssociationByID(long id){
        return mTimeLineOpenHelper.mDatabase.query(Constants.Association.TABLE_NAME,
                new String[]{
                        Constants.Association.id,
                        Constants.Association.event_id_1,
                        Constants.Association.event_id_2,
                        Constants.Association.connection,
                        Constants.Association.author_id,
                        Constants.Association.privacy,
                        Constants.Association.association_type
                },
                Constants.Association.id + "=?" ,
                new String[]{String.valueOf(id)},
                null, null, null);
    }

    public Cursor queryCategoryByID(long id){
        return mTimeLineOpenHelper.mDatabase.query(Constants.Category.TABLE_NAME,
                new String[]{
                        Constants.Category.id,
                        Constants.Category.name
                },
                Constants.Category.id + "=?" ,
                new String[]{String.valueOf(id)},
                null, null, null);
    }

    public Cursor queryCategoryByName(String category){
        return mTimeLineOpenHelper.mDatabase.query(
            Constants.Category.TABLE_NAME,
            new String[]{
                    Constants.Category.id,
                    Constants.Category.name},
            Constants.Category.name + "=?",
            new String[] {category}, null, null, null);
    }

    public Cursor queryPersonByID(long id){
        return mTimeLineOpenHelper.mDatabase.query(Constants.Person.TABLE_NAME,
                new String[]{
                        Constants.Person.id,
                        Constants.Person.person
                },
                Constants.Person.id + "=?" ,
                new String[]{String.valueOf(id)},
                null, null, null);
    }

    public Cursor queryPersonByName(String person){
        return mTimeLineOpenHelper.mDatabase.query(Constants.Person.TABLE_NAME,
                new String[]{
                        Constants.Person.id,
                        Constants.Person.person
                },
                Constants.Person.person + "=?" ,
                new String[]{person},
                null, null, null);
    }
    /*Table - Constants.Nationality.TABLE_NAME*/
	/*IdCollum - Constants.Nationality.id*/
	/*NameCollum -  Constants.Nationality.Nationality*/
    public Cursor queryByGeolocationTable(String WhereArg[],String Table,String IdCollum,String NameCollum){
        String Selection="";
        for(int i=0;i<WhereArg.length;i++){
            if(WhereArg[i]!=null){
                if(i<WhereArg.length-1){
                    Selection = Selection+Table+"."+NameCollum +" LIKE ?  or ";
                }else{
                    Selection = Selection+Table+"."+NameCollum +" LIKE ? ";
                }
            }
        }
        return query(Table,new String[]{IdCollum,NameCollum},
                Selection,WhereArg,null, null,Table+"."+IdCollum);
    }
    /**
     * Функция для записи запроса внутрь базы.
     * **/
    public long SaveSearchQuery(SearchContext searchContext,TimeListItem ListItem){

        ArrayList<Integer> CategoryId = new ArrayList<>();
        ArrayList<Integer> AuthorsId = new ArrayList<>();
        ArrayList<Integer> PersonsId = new ArrayList<>();
        ArrayList<Integer> TimeLineId = new ArrayList<>();
        long GeoId[] = null;

        //Category
        if(!searchContext.getCategorys().isEmpty()){
            Cursor categoryCursor = queryFromAllKeywords(
                searchContext.getCategorys().getData(),
                Constants.Category_Keyword.TABLE_NAME,
                Constants.Category_Keyword.Category,
                Constants.Category_Keyword.Keyword,
                Constants.Category_Keyword.Table_N);

            for(categoryCursor.moveToFirst();!categoryCursor.isAfterLast();categoryCursor.moveToNext()){
                CategoryId.add(categoryCursor.getInt(categoryCursor.getColumnIndex(Constants.Category_Keyword.Category)));
            }
        }

        //Author
        if(!searchContext.getAuthors().isEmpty()){
            Cursor authorCursor = queryFromAllKeywords(
                    searchContext.getAuthors().getData(),
                    Constants.Author_Keyword.TABLE_NAME,
                    Constants.Author_Keyword.Author,
                    Constants.Author_Keyword.Keyword,
                    Constants.Author_Keyword.Table_N);

            for(authorCursor.moveToFirst();!authorCursor.isAfterLast();authorCursor.moveToNext()){
                AuthorsId.add(authorCursor.getInt(authorCursor.getColumnIndex(Constants.Author_Keyword.Author)));
            }
        }

        //Person
        if(!searchContext.getPersons().isEmpty()){
            Cursor PersonCursor = queryFromAllKeywords(
                    searchContext.getPersons().getData(),
                    Constants.Person_Keyword.TABLE_NAME,
                    Constants.Person_Keyword.Persona,
                    Constants.Person_Keyword.Keyword,
                    Constants.Person_Keyword.Table_N);

            for(PersonCursor.moveToFirst();!PersonCursor.isAfterLast();PersonCursor.moveToNext()){
                PersonsId.add(PersonCursor.getInt(PersonCursor.getColumnIndex(Constants.Person_Keyword.Persona)));
            }
        }

        //TimeLine
        if(!searchContext.getTimeLine().isEmpty()){
            Cursor TimeLineCursor = queryFromAllKeywords(
                    searchContext.getTimeLine().getData(),
                    Constants.Mark.TABLE_NAME,
                    Constants.Mark.TimeLine_id,
                    Constants.Mark.Keyword_id,
                    Constants.Mark.Table_N);

            for(TimeLineCursor.moveToFirst();!TimeLineCursor.isAfterLast();TimeLineCursor.moveToNext()){
                TimeLineId.add(TimeLineCursor.getInt(TimeLineCursor.getColumnIndex(Constants.Mark.TimeLine_id)));
            }
        }

        //Geo
        if(!searchContext.getGeoAreas().isEmpty()){

            int Length = searchContext.getGeoAreas().getGeoLocation().length;
            long Geo_Id[] = new long[Length];
            String Nationalty[] = new String[Length];
            String Continent[] = new String[Length];
            String Country[] = new String[Length];
            String City[] = new String[Length];
            String Street[] = new String[Length];

            for(int i=0;i<Length;i++){
                Nationalty[i] = searchContext.getGeoAreas().getGeoLocation()[i].getNationalty();
                Continent[i] = searchContext.getGeoAreas().getGeoLocation()[i].getContinent();
                Country[i] = searchContext.getGeoAreas().getGeoLocation()[i].getCountry();
                City[i] = searchContext.getGeoAreas().getGeoLocation()[i].getCity();
                Street[i] = searchContext.getGeoAreas().getGeoLocation()[i].getStreet();
            }

            Cursor nationaltyCursor = queryByGeolocationTable(Nationalty, Constants.Nationality.TABLE_NAME, Constants.Nationality.id, Constants.Nationality.Nationality);
            Cursor continentCursor = queryByGeolocationTable(Continent, Constants.Continent.TABLE_NAME, Constants.Continent.id, Constants.Continent.Continent);
            Cursor countryCursor = queryByGeolocationTable(Country, Constants.Country.TABLE_NAME, Constants.Country.id, Constants.Country.Country);
            Cursor cityCursor = queryByGeolocationTable(City, Constants.City.TABLE_NAME, Constants.City.id, Constants.City.City);
            Cursor streetCursor = queryByGeolocationTable(Street, Constants.Street.TABLE_NAME, Constants.Street.id, Constants.Street.Street);

            int i=0;
            for(	nationaltyCursor.moveToFirst(),continentCursor.moveToFirst(),countryCursor.moveToFirst(),
                            cityCursor.moveToFirst(),streetCursor.moveToFirst();

                    (!nationaltyCursor.isAfterLast())&&(!continentCursor.isAfterLast())&&(!countryCursor.isAfterLast())&&
                            (!cityCursor.isAfterLast())&&(!streetCursor.isAfterLast()); i++){

                int Nationalty_id,Continent_id,Country_id,City_id,Street_id;

                if(!nationaltyCursor.isAfterLast()){Nationalty_id = nationaltyCursor.getInt(nationaltyCursor.getColumnIndex(Constants.Nationality.id));}
                else{Nationalty_id = -1;}
                if(!continentCursor.isAfterLast()){Continent_id = continentCursor.getInt(continentCursor.getColumnIndex(Constants.Continent.id));}
                else{Continent_id = -1;}
                if(!countryCursor.isAfterLast()){Country_id = countryCursor.getInt(countryCursor.getColumnIndex(Constants.Country.id));}
                else{Country_id = -1;}
                if(!cityCursor.isAfterLast()){City_id = cityCursor.getInt(cityCursor.getColumnIndex(Constants.City.id));}
                else{City_id = -1;}
                if(!streetCursor.isAfterLast()){Street_id = streetCursor.getInt(cityCursor.getColumnIndex(Constants.Street.id));}
                else{Street_id = -1;}

                Geo_Id[i] = insertGeoList(Nationalty_id, Continent_id, Country_id, City_id, Street_id);

                if(!nationaltyCursor.isAfterLast()){nationaltyCursor.moveToNext();}
                if(!continentCursor.isAfterLast()){continentCursor.moveToNext();}
                if(!countryCursor.isAfterLast()){countryCursor.moveToNext();}
                if(!cityCursor.isAfterLast()){cityCursor.moveToNext();}
                if(!streetCursor.isAfterLast()){streetCursor.moveToNext();}
            }
            GeoId = Geo_Id;
        }

        long TimeLists_Id =  insertTimeLists(ListItem.getTitle(), ListItem.getAbout(), ListItem.getColor(), ListItem.getVisible());

        int length;
        if(GeoId!=null){
            length = Math.max(
                Math.max(Math.max(CategoryId.size(),TimeLineId.size()),GeoId.length),
                Math.max(AuthorsId.size(),PersonsId.size()));
        }else{
            length = Math.max(
                Math.max(CategoryId.size(), TimeLineId.size()),
                Math.max(AuthorsId.size(),PersonsId.size()));
        }

        for(int inx=0;inx<length;inx++){
            insertContext(
                    TimeLists_Id,
                    (GeoId!=null)?((inx<GeoId.length)?GeoId[inx]:-1):-1,
                    (inx<AuthorsId.size())?AuthorsId.get(inx):-1,
                    (inx<PersonsId.size())?PersonsId.get(inx):-1,
                    (inx<CategoryId.size())?CategoryId.get(inx):-1,
                    (inx<TimeLineId.size())?TimeLineId.get(inx):-1);
        }
        return TimeLists_Id;
    }
    //пример
	/*String Table - Constants.Category_Keyword.TABLE_NAME
	String ColumnName - Constants.Category_Keyword.Category
	String KeywordColumn - Constants.Category_Keyword.Keyword
	String ColumnTable_N - Constants.Category_Keyword.Table_N*/
    public Cursor queryFromAllKeywords(String selectionArgs[],String Table,String ColumnName,String KeywordColumn,String ColumnTable_N){

        String Selection = "(";
        for(int i=0;i<selectionArgs.length;i++){
            if(selectionArgs[i]!=null){
                if(i<selectionArgs.length-1){
                    Selection = Selection+Constants.All_Keywords.TABLE_NAME+"."+Constants.All_Keywords.word +" LIKE \""+selectionArgs[i]+"\" OR ";
                }else{
                    Selection = Selection+Constants.All_Keywords.TABLE_NAME+"."+Constants.All_Keywords.word +" LIKE \""+selectionArgs[i]+"\" ";
                }
            }
        }
        Selection = Selection + ")" +
                " and "+Constants.All_Keywords.TABLE_NAME+"."+Constants.All_Keywords.table_id+" = "+Table+"."+ColumnTable_N+
                " and "+Constants.All_Keywords.TABLE_NAME+"."+Constants.All_Keywords.id+" = "+Table+"."+KeywordColumn;

        return query(Constants.All_Keywords.TABLE_NAME+","+Table,
                new String[]{
                        Constants.All_Keywords.TABLE_NAME+"."+Constants.All_Keywords.word,
                        Table+"."+ColumnName},
                Selection,
                null,null,null,
                Table+"."+ColumnName);
    }

    public Cursor queryFromTimeLists(int visible[]){
        String selection ="";
        String selectionArgs[] = new String[visible.length];

        for(int i=0;i<visible.length;i++){
            if(i<visible.length-1){
                selection = selection + Constants.TimeLists.visible +"=? OR ";
            }else{
                selection = selection + Constants.TimeLists.visible +"=? ";
            }
            selectionArgs[i] = String.valueOf(visible[i]);
        }
        return query(Constants.TimeLists.TABLE_NAME,new String[]{
                        Constants.TimeLists.id,
                        Constants.TimeLists.title,
                        Constants.TimeLists.about,
                        Constants.TimeLists.color,
                        Constants.TimeLists.visible},
                selection,
                selectionArgs,
                null, null, null);
    }
    public void deleteLinksByID(long id){
        delete(Constants.Links.TABLE_NAME,Constants.Links.id+"=?",new String[]{String.valueOf(id)});
    }
    public void deleteFromTimeLists(int id){
        //delete(Constants.TimeLists.TABLE_NAME, Constants.TimeLists.id+" =?",new String[]{String.valueOf(id)});
        // прописать действия удаление элемента из TimeList.
        //TODO : добавить методы для удаления данных из связанных с записью таблиц
    }
}

