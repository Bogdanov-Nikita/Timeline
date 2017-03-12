package model.search;

public class Date {
    String start;
    String end;

    public Date() {
        start = null;
        end = null;
    }
    public Date(String StartDate,String EndDate) {
        start = StartDate;
        end = EndDate;
    }
    public String getStart() {
        return start;
    }
    public String getEnd() {
        return end;
    }
}

