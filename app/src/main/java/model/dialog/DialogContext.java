package model.dialog;

import android.os.Parcel;
import android.os.Parcelable;

public class DialogContext implements Parcelable{
    //not change order
    public static final int EVENT = 0;
    public static final int EVENT_LINKS = 1;
    public static final int ASSOCIATION = 2;
    public static final int PERSON = 3;
    public static final int AUTHOR = 4;
    public static final int CATEGORY = 5;
    public static final int GEO = 6;
    public static final int TIMELINE = 7;//only update

    public static final int ADD = 0;
    public static final int UPDATE = 1;

    int type;
    int actoin;
    long id;


    /**
     * @param type type of dialog
     *  action = add , id = -1
     */
    public DialogContext(int type){
        this(type,ADD,-1);
    }

    /**
     * @param type type of dialog
     *  action = update ,
     *  @param id of the content to update.
     */
    public DialogContext(int type,long id){
        this(type,UPDATE,id);
    }

    /**
     * for inner use
     * @param type type of dialog
     * @param action type of action
     * @param id used only if action = UPDATE else id = -1
     * */
    private DialogContext(int type,int action,long id){
        this.type = type;
        this.actoin = action;
        this.id = id;
    }

    public DialogContext(Parcel in) {
        type = in.readInt();
        actoin = in.readInt();
        id  = in.readLong();
    }

    public int getType() {
        return type;
    }

    public int getAction(){
        return actoin;
    }

    public long getId() {
        return id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeInt(actoin);
        dest.writeLong(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<DialogContext> CREATOR = new Parcelable.Creator<DialogContext>() {

        @Override
        public DialogContext createFromParcel(Parcel source) {
            return new DialogContext(source);
        }

        @Override
        public DialogContext[] newArray(int size) {
            return new DialogContext[size];
        }
    };
}
