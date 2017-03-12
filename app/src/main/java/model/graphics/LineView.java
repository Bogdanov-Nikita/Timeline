package model.graphics;

public class LineView {

    int ScrollPosition;
    int Zoom;

    public LineView() {
        ScrollPosition=0;
        Zoom = 0;
    }
    public LineView(int ScrollPosition,int Zoom) {
        this.ScrollPosition = ScrollPosition;
        this.Zoom = Zoom;
    }
    public int getScrollPosition() {
        return ScrollPosition;
    }
    public int getZoom() {
        return Zoom;
    }
}

