package model.search;

public class GeoAreas {
    /**
     * aggregation: true = "AND" false = "OR"
     */
    boolean aggregation[];
    GeoLocation geoLocation[];
    /**
     *If Default Constructor aggregation = false, and in geoLocation null parameters;
     * */
    public GeoAreas() {
        aggregation = new boolean[5];
        geoLocation = new GeoLocation[]{new GeoLocation()};
    }
    public GeoAreas(GeoLocation geoLocation[],boolean aggregation[]) {
        this.aggregation=aggregation;
        this.geoLocation=geoLocation;
    }
    public GeoAreas(GeoLocation geoLocation[],boolean Nationality,
                    boolean Continent,boolean Country,boolean City,boolean Street){
        this.geoLocation=geoLocation;
        aggregation = new boolean[5];
        this.aggregation[0] = Nationality;
        this.aggregation[1] = Continent;
        this.aggregation[2] = Country;
        this.aggregation[3] = City;
        this.aggregation[4] = Street;
    }
    public void setAggregation(boolean aggregation[]) {
        this.aggregation = aggregation;
    }
    public void setAggregation(boolean Nationality,
                               boolean Continent,boolean Country,boolean City,boolean Street) {
        this.aggregation[0] = Nationality;
        this.aggregation[1] = Continent;
        this.aggregation[2] = Country;
        this.aggregation[3] = City;
        this.aggregation[4] = Street;
    }
    public GeoLocation[] getGeoLocation() {
        return geoLocation;
    }
    public boolean isEmpty(){
        if(geoLocation!=null){
            for(int i=0;i<geoLocation.length;i++){
                if(!geoLocation[i].isEmpty()){return false;}
            }
            return true;
        }else{return true;}
    }
    public boolean[] isAggregation() {
        return aggregation;
    }
}

