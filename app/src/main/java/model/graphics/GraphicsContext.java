package model.graphics;

import android.os.Parcel;
import android.os.Parcelable;

public class GraphicsContext implements Parcelable{
    public static final int LINE_VIEW = 0;
    public static final int GRID_VIEW = 1;
    public static final int SWIPE_GALLERY_VIEW = 2;
    public static final int SWIPE_LIST_VIEW = 3;
    public static final int SWIPE_EVENT_VIEW = 4;
    public static final int MAP_VIEW = 5;


    int TypeOfView;//используеться для вызова активити по диалогу.

    LineView LineView;
    SwipeView EventSwipeView;
    SwipeView ListSwipeView;
    SwipeView GallerySwipeView;
    MapView MapView;

    //данные из таблицы Timelists получаються из базы в любой момент для всех View по этому не передаються здесь

    public GraphicsContext(
            LineView LineView,SwipeView EventSwipeView,
            SwipeView ListSwipeView,SwipeView GallerySwipeView,
            MapView MapView,int TypeOfView) {
        this.LineView = LineView;
        this.GallerySwipeView = GallerySwipeView;
        this.ListSwipeView = ListSwipeView;
        this.EventSwipeView = EventSwipeView;
        this.MapView = MapView;
        this.TypeOfView = TypeOfView;
    }
    public GraphicsContext(Parcel in) {
        TypeOfView=in.readInt();
        //LineView
        LineView = new LineView(in.readInt(),in.readInt());
        //GallerySwipeView
        ListSwipeView = loadSwipe(in);
        //ListSwipeView
        ListSwipeView = loadSwipe(in);
        //EventSwipeView
        EventSwipeView = loadSwipe(in);
        //MapView
        MapView = new MapView(in.readInt(), in.readInt(), in.readInt());
    }

    SwipeView loadSwipe(Parcel in){
        int Page = in.readInt();
        int Scrollpage[] = new int[in.readInt()];
        in.readIntArray(Scrollpage);
        return new SwipeView(Page, Scrollpage);
    }

    void saveSwipe(SwipeView view,Parcel dest){
        dest.writeInt(view.Page);
        dest.writeInt(view.ScrollPosition.size());
        dest.writeIntArray(view.getScrollPosition());
    }

    public LineView getLineView() {
        return LineView;
    }

    public SwipeView getGallerySwipeView() {
        return GallerySwipeView;
    }

    public SwipeView getListSwipeView() {
        return ListSwipeView;
    }

    public SwipeView getEventSwipeView() {
        return EventSwipeView;
    }

    public MapView getMapView() {
        return MapView;
    }

    public int getTypeOfView() {
        return TypeOfView;
    }

    public void setTypeOfView(int typeOfView) {
        TypeOfView = typeOfView;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(TypeOfView);
        //LineView
        dest.writeInt(LineView.ScrollPosition);
        dest.writeInt(LineView.Zoom);
        //GallerySwipeView
        saveSwipe(ListSwipeView,dest);
        //ListSwipeView
        saveSwipe(ListSwipeView,dest);
        //EventSwipeView
        saveSwipe(EventSwipeView,dest);
        //MapView
        dest.writeInt(MapView.CoordinateX);
        dest.writeInt(MapView.CoordinateY);
        dest.writeInt(MapView.Zoom);

    }
    public static final Parcelable.Creator<GraphicsContext> CREATOR = new Parcelable.Creator<GraphicsContext>() {

        @Override
        public GraphicsContext createFromParcel(Parcel source) {
            return new GraphicsContext(source);
        }

        @Override
        public GraphicsContext[] newArray(int size) {
            return new GraphicsContext[size];
        }
    };
}
