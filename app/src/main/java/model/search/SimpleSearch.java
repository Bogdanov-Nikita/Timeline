package model.search;

public class SimpleSearch {
    /**
     * All tables - 0
     * Author - 1,
     * Category - 2,
     * Mark (TimeLine) - 3,
     * Person - 4,
     * GeoLocation - { Nationality - 5,  Continent - 6,  Country - 7,  City - 8,  Street - 9  }
     */
    int Id;//может быть и -1, означает что данный параметр не используеться
    int TypeOfContext;

    String Word;

    public SimpleSearch(String Word,int TypeOfContext,int Id) {
        this.Word = Word;
        this.TypeOfContext = TypeOfContext;
        this.Id = Id;
    }
    public void setTypeOfContext(int typeOfContext) {
        TypeOfContext = typeOfContext;
    }
    public int getId() {
        return Id;
    }
    public String getWord() {
        return Word;
    }
    public int getTypeOfContext() {
        return TypeOfContext;
    }
}

