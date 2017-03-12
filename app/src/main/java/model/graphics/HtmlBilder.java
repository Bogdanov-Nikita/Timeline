package model.graphics;

public class HtmlBilder {

    public final static String EventEdit1= "edit1";
    public final static String EventEdit2= "edit2";

    public HtmlBilder() {
    }

    static String addLinks(String Link[]){
        String LinkBlock="<ol>";
        for(int i=0;i<Link.length;i++){
            String temp = "<li>"+"<h4><a href=\""+Link[i]+"\">"+Link[i]+"</a></h4>"+"</li>";
            LinkBlock=LinkBlock+temp;
        }
        LinkBlock=LinkBlock+"</ol>";
        return LinkBlock;
    }

    public static String bildHtml(
            String EventHeader,
            String Edit,
            String Time_start,
            String Time_end,
            String Geo,
            String EventText,
            String LinksTitle,
            String Link[],
            String DateMark[]){
        String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\">"+
                "<head>"+"<title>"+EventHeader+"</title>"+"</head>"+"<body>"+
                "<h2>"+EventHeader+"</h2>"+
                "<h5 align=\"right\">[<a href=\""+EventEdit1+"\">"+Edit+"</a>]</h5>"+
                "<hr />"+
                "<p>"+bildTime(Time_start, Time_end,DateMark[0],DateMark[1],DateMark[2]) +"</p>"+
                "<p>"+Geo+"</p>"+
                "<p>"+EventText+"</p>"+
                "<p>&nbsp;</p>"+
                "<h3>"+LinksTitle+"</h3>"+
                "<h5 align=\"right\">[<a href=\""+EventEdit2+"\">"+Edit+"</a>]</h5>"+
                "<hr />"+addLinks(Link)+"</body>"+"</html>";
        return html;
    }
    static String bildTime(String Time_start, String Time_end,String Date_mark,String Start_mark,String End_mark){
        String temp = "";
        if(Time_start==null && Time_start==null){
            return "";
        }
        if(Time_start!=null){
            if(!Time_start.equals("")){
                temp = Date_mark+":<br>" + Start_mark+" " + Time_start + "<br>";
            }
        }
        if(Time_end!=null){
            if(!Time_end.equals("")){
                if(temp.equals("")){
                    temp = Date_mark+":<br>" + End_mark+" " + Time_start ;
                }else{
                    temp = temp + End_mark+" " + Time_end ;
                }
            }
        }
        return temp;
    }
    public static String bildGEO(
            String Coordinates,
            String Nationality_id,
            String Continent_id,
            String Country_id,
            String City_id,
            String Street_id,
            String Home,String geo_mark[] ){
        String tempGeo="";
        if(Coordinates!=null){
            if(!Coordinates.equals("")){
                tempGeo = geo_mark[0] + ": " + Coordinates+"<br>";}}
        if(Nationality_id!=null){
            if(!Nationality_id.equals("")){
                tempGeo = tempGeo + geo_mark[1] + ": " + Nationality_id + "<br>";}}
        if(Continent_id!=null){
            if(!Continent_id.equals("")){
                tempGeo = tempGeo + geo_mark[2] + ": " + Continent_id + "<br>";}}
        if(Country_id!=null){
            if(!Country_id.equals("")){
                tempGeo = tempGeo + geo_mark[3] + ": " + Country_id + "<br>";}}
        if(City_id!=null){
            if(!City_id.equals("")){
                tempGeo = tempGeo + geo_mark[4] + ": " + City_id + "<br>";}}
        if(Street_id!=null){
            if(!Street_id.equals("")){
                tempGeo = tempGeo + geo_mark[5] + ": " + Street_id + "<br>";}}
        if(Home!=null){
            if(!Home.equals("")){
                tempGeo = tempGeo + geo_mark[6] + ": " + Home + "<br>";}}
        return tempGeo;
    }

}

