package model.graphics;

public class MapView {

    int CoordinateX;
    int CoordinateY;
    int Zoom;

    public MapView() {
        CoordinateX=0;
        CoordinateY=0;
        Zoom=0;
    }
    public MapView(int CoordinateX,int CoordinateY,int Zoom) {
        this.CoordinateX = CoordinateX;
        this.CoordinateY = CoordinateY;
        this.Zoom = Zoom;
    }

    public int getZoom() {
        return Zoom;
    }
    public int getX() {
        return CoordinateX;
    }
    public int getY() {
        return CoordinateY;
    }

}

