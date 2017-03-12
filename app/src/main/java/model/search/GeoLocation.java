package model.search;

public class GeoLocation {

    String Nationality;
    String Continent;
    String Country;
    String City;
    String Street;
    String Home;
    /**
     *Default Constructor is null parameter;
     * */
    public GeoLocation() {
        Nationality = null;
        Continent = null;
        Country = null;
        City = null;
        Street = null;
        Home = null;
    }
    public GeoLocation(String Nationalty,String Continent,String Country,String City,String Street,String Home) {
        this.Nationality = Nationalty;
        this.Continent = Continent;
        this.Country = Country;
        this.City = City;
        this.Street = Street;
        this.Home = Home;
    }
    public String getNationalty() {
        return Nationality;
    }
    public String getContinent() {
        return Continent;
    }
    public String getCountry() {
        return Country;
    }
    public String getCity() {
        return City;
    }
    public String getStreet() {
        return Street;
    }
    public String getHome() {
        return Home;
    }
    public boolean isEmpty(){
        boolean NaFlag =  (Nationality!=null)?Nationality.equals(""):true;
        boolean ConFlag = (Continent!=null)?Continent.equals(""):true;
        boolean CouFlag = (Country!=null)?Country.equals(""):true;
        boolean CiFlag = (City!=null)?City.equals(""):true;
        boolean StFlag = (Street!=null)?Street.equals(""):true;
        boolean HoFlag = (Home!=null)?Home.equals(""):true;
        return (NaFlag&&ConFlag&&CouFlag&&CiFlag&&StFlag&&HoFlag);
    }
}

