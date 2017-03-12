package model.search;

import android.os.Parcel;
import android.os.Parcelable;

public class TimeListItem implements Parcelable{

    public final static int NOT_VISIBLE = 0;
    public final static int VISIBLE = 1;
    public final static int TEMPORARY = 2;

    public final static int DEFAULT_COLOR = -1;
    public final static String DEFAULT_TITLE = "Title";
    public final static String DEFAULT_ABOUT = "About";

    long id;
    String title;
    String about;
    int color;
    int visible;

    public TimeListItem(long id,String title,String about,int color,int visible) {
        this.id = id;
        this.title = title;
        this.about = about;
        this.color = color;
        this.visible = visible;
    }
    public TimeListItem(Parcel in) {
        id = in.readLong();
        color = in.readInt();
        visible = in.readInt();
        title = in.readString();
        about = in.readString();

    }

    public long getId() {
        return id;
    }
    public void setColor(int color) {
        this.color = color;
    }
    public int getColor() {
        return color;
    }
    public void setVisible(int visible) {
        this.visible = visible;
    }
    public int getVisible() {
        return visible;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    public void setAbout(String about) {
        this.about = about;
    }
    public String getAbout() {
        return about;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(color);
        dest.writeInt(visible);
        dest.writeString(title);
        dest.writeString(about);
    }
    public static final Parcelable.Creator<TimeListItem> CREATOR = new Parcelable.Creator<TimeListItem>() {

        @Override
        public TimeListItem createFromParcel(Parcel source) {
            return new TimeListItem(source);
        }

        @Override
        public TimeListItem[] newArray(int size) {
            return new TimeListItem[size];
        }
    };

}

