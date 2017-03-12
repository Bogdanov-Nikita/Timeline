package model.search;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchContext implements Parcelable {

    public final static int SIMPLE_SEARCH = 0;
    public final static int EXPANDED_SEARCH = 1;

    /**
     *  добавим в следующей версии
     */
    int TypeOfSearch;

    SimpleSearch simpleSearch;

    /**
     * Expanded Search, основные поля для заполнения по расширенному поиску
     */
    GeoAreas geoAreas;
    SearchItemContainer authors;
    SearchItemContainer timeLine;
    SearchItemContainer categorys;
    SearchItemContainer persons;
    Date timeDate;

    public SearchContext(SimpleSearch simpleSearch) {
        this.simpleSearch = simpleSearch;
        TypeOfSearch = SIMPLE_SEARCH;
    }
    /**
     * use default constructor field if don't use.
     */
    public SearchContext(
            GeoAreas geoAreas,SearchItemContainer authors,SearchItemContainer timeLine,
            SearchItemContainer categorys,SearchItemContainer persons,Date timeDate) {
        this.geoAreas = geoAreas;
        this.authors = authors;
        this.timeLine = timeLine;
        this.categorys = categorys;
        this.persons = persons;
        this.timeDate = timeDate;
        TypeOfSearch = EXPANDED_SEARCH;
    }
    public SearchContext(Parcel in) {
        //Type of Search
        TypeOfSearch = in.readInt();
        if(TypeOfSearch==SIMPLE_SEARCH){
            simpleSearch =new SimpleSearch(in.readString(),in.readInt(),in.readInt()) ;
        }
        if(TypeOfSearch==EXPANDED_SEARCH){
            //flags
            boolean flags[] = new boolean[4];
            in.readBooleanArray(flags);
            //GeoAreas length
            int GeoLocationLength = in.readInt();
            GeoLocation geoLocation[] = new GeoLocation[GeoLocationLength];
            for(int i=0;i<GeoLocationLength;i++){
                String location[] = new String [6];
                in.readStringArray(location);
                geoLocation[i] = new GeoLocation(
                        location[0],
                        location[1],
                        location[2],
                        location[3],
                        location[4],
                        location[5]);
            }
            boolean GeoAggregation[] = new boolean [5];
            in.readBooleanArray(GeoAggregation);
            //Author
            int AuthorLength = in.readInt();
            String Authors[] = new String[AuthorLength];
            in.readStringArray(Authors);
            //Category
            int CategoryLength = in.readInt();
            String Categorys[] = new String[CategoryLength];
            in.readStringArray(Categorys);
            //Person
            int PersonLength = in.readInt();
            String Persons[] = new String[PersonLength];
            in.readStringArray(Persons);
            //TimeLine
            int TimeLineLength = in.readInt();
            String TimeLine[] = new String[TimeLineLength];
            in.readStringArray(TimeLine);

            //Date
            timeDate = new Date(in.readString(), in.readString());

            geoAreas = new GeoAreas(geoLocation, GeoAggregation);
            authors = new SearchItemContainer(Authors, flags[0]);
            categorys = new SearchItemContainer(Categorys, flags[1]);
            persons = new SearchItemContainer(Persons, flags[2]);
            timeLine = new SearchItemContainer(TimeLine,flags[3]);
        }
    }

    public SimpleSearch getSimpleSearch() {
        return simpleSearch;
    }
    public GeoAreas getGeoAreas() {
        return geoAreas;
    }
    public SearchItemContainer getAuthors() {
        return authors;
    }
    public SearchItemContainer getTimeLine() {
        return timeLine;
    }
    public SearchItemContainer getCategorys() {
        return categorys;
    }
    public SearchItemContainer getPersons() {
        return persons;
    }
    public Date getTimeDate() {
        return timeDate;
    }
    public int getTypeOfSearch() {
        return TypeOfSearch;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //Type of Search
        dest.writeInt(TypeOfSearch);
        if(TypeOfSearch==SIMPLE_SEARCH){
            dest.writeString(simpleSearch.getWord());
            dest.writeInt(simpleSearch.getTypeOfContext());
            dest.writeInt(simpleSearch.getId());
        }
        if(TypeOfSearch==EXPANDED_SEARCH){
            //write aggregation flags
            dest.writeBooleanArray(new boolean []{authors.isAggregation(),categorys.isAggregation(),persons.isAggregation(),timeLine.isAggregation()});
            //geoAreas.length
            dest.writeInt(geoAreas.getGeoLocation().length);
            //geoAreas
            for(int i=0;i<geoAreas.getGeoLocation().length;i++){
                dest.writeStringArray(new String[]{
                        geoAreas.getGeoLocation()[i].Nationality,
                        geoAreas.getGeoLocation()[i].Continent,
                        geoAreas.getGeoLocation()[i].Country,
                        geoAreas.getGeoLocation()[i].City,
                        geoAreas.getGeoLocation()[i].Street,
                        geoAreas.getGeoLocation()[i].Home});
            }
            dest.writeBooleanArray(geoAreas.isAggregation());
            //Author
            dest.writeInt(authors.getData().length);
            dest.writeStringArray(authors.getData());
            //Category
            dest.writeInt(categorys.getData().length);
            dest.writeStringArray(categorys.getData());
            //Person
            dest.writeInt(persons.getData().length);
            dest.writeStringArray(persons.getData());
            //TimeLine
            dest.writeInt(timeLine.getData().length);
            dest.writeStringArray(timeLine.getData());
            //Date
            dest.writeString(timeDate.getStart());
            dest.writeString(timeDate.getEnd());
        }
    }
    public static final Parcelable.Creator<SearchContext> CREATOR = new Parcelable.Creator<SearchContext>() {

        @Override
        public SearchContext createFromParcel(Parcel source) {
            return new SearchContext(source);
        }

        @Override
        public SearchContext[] newArray(int size) {
            return new SearchContext[size];
        }
    };

}

