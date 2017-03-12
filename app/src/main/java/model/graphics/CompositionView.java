package model.graphics;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
//import android.os.Build;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

//import com.android.nik.timeline.R;


/**
 * Program layout composition
 */
@SuppressWarnings("ResourceType")
public class CompositionView {

    public static final int WindowsLayoutId = 0x6f000002;
    //used only 4 but 5 number reserved fo future.
    public static final int WindowId[] = {0x6f000003,0x6f000004,0x6f000005,0x6f000006,0x6f000007};
    public static final int WindowSpinnerId[] = {0x6f000008,0x6f000009,0x6f00000A,0x6f00000B,0x6f00000C};
    public static final int ContainerId[] = {0x6f00000D,0x6f00000E,0x6f00000F,0x6f000010,0x6f000011};

    public LinearLayout RootLayout;
    public LinearLayout WindowsLayout;
    public LinearLayout WindowLayout[];
    public Spinner WindowSpinner[];
    public FrameLayout WindowFrame[];

    private int ActionBarSize;
    private int WidthPixels;
    private int HeightPixels;
    private int windows;

    /**
     * @param activity - current screen activity
     * @param ContainerLayoutId - view for
     * @param orientation - LinearLayout.HORIZONTAL (true) : LinearLayout.VERTICAL (false)
     * @param window - number of windows;
    */
    public CompositionView(Activity activity,int ContainerLayoutId, boolean orientation, int window){
        windows = window;
        Context context = activity.getApplicationContext();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        TypedArray styledAttributes = activity.getTheme().obtainStyledAttributes(
                new int[]{
                        android.support.v7.appcompat.R.attr.actionBarSize,
                        android.support.v7.appcompat.R.attr.colorPrimary});
        ActionBarSize = styledAttributes.getDimensionPixelSize(0,(int)(64 * displaymetrics.density));
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels - ActionBarSize;
        WidthPixels = width;
        HeightPixels = height;

        RootLayout = (LinearLayout)activity.findViewById(ContainerLayoutId);
        WindowsLayout = new LinearLayout(context){
            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                if(WidthPixels != getMeasuredWidth() || HeightPixels != getMeasuredHeight()){
                    WidthPixels = getMeasuredWidth();
                    HeightPixels = getMeasuredHeight();
                    update((getOrientation() == LinearLayout.HORIZONTAL));
                }
            }
        };
        initWindowsLayout(context,orientation);
        initWindows(context, orientation, width, height);
        RootLayout.addView(WindowsLayout);
    }

    void initWindows(Context context,boolean orientation,int width, int height){

        int widthPart = width/windows;
        int heightPart = height/windows;

        int WindowHeight = (orientation)?(height):(heightPart);
        int WindowWidth = (orientation)?(widthPart):(width);

        int FrameHeight = WindowHeight - ActionBarSize;
        //int FrameWidth = WindowWidth; excessive local variable

        WindowLayout = new LinearLayout[windows];
        WindowSpinner = new Spinner[windows];
        WindowFrame = new FrameLayout[windows];

        for(int i=0; i < windows; i++){
            initWindow(context, i, WindowWidth, WindowHeight);
            initSpinner(context, i, WindowWidth);
            initFrame(context, i, WindowWidth, FrameHeight);
            WindowLayout[i].addView(WindowSpinner[i]);
            WindowLayout[i].addView(WindowFrame[i]);
            WindowsLayout.addView(WindowLayout[i]);
        }
    }

    void initWindow(Context context,int position,int width, int height){
        WindowLayout[position] = new LinearLayout(context);
        WindowLayout[position].setLayoutParams(
                new LinearLayout.LayoutParams(
                        width,
                        height
                )
        );
        WindowLayout[position].setOrientation(LinearLayout.VERTICAL);
        WindowLayout[position].setId(WindowId[position]);
    }

    void initSpinner(Context context,int position, int width){
        WindowSpinner[position] = new Spinner(context);
        WindowSpinner[position].setLayoutParams(
                new ViewGroup.LayoutParams(
                        width,
                        ActionBarSize)
        );
        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            WindowSpinner[position].setElevation(context.getResources().getDimension(R.dimen.toolbar_elevation));
        }*/
        //if need rewrite color
        WindowSpinner[position].getPopupBackground().setColorFilter(0xFFFFFFFF, PorterDuff.Mode.SRC);
        WindowSpinner[position].setId(WindowSpinnerId[position]);
    }

    void initFrame(Context context,int position,int width, int height){
        WindowFrame[position] = new FrameLayout(context);
        WindowFrame[position].setLayoutParams(
                new FrameLayout.LayoutParams(
                        width,
                        height
                )
        );
        WindowFrame[position].setId(ContainerId[position]);
    }

    void initWindowsLayout(Context context,boolean orientation){
        WindowsLayout = new LinearLayout(context);
        WindowsLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
        WindowsLayout.setOrientation((orientation) ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
        WindowsLayout.setId(WindowsLayoutId);
    }

    public void update(boolean orientation){setOrientation(orientation);}
    public void setOrientation(boolean orientation){

        int widthPart = WidthPixels/windows;
        int heightPart = HeightPixels/windows;

        int WindowHeight = (orientation)?(HeightPixels):(heightPart);
        int WindowWidth = (orientation)?(widthPart):(WidthPixels);

        int FrameHeight = WindowHeight - ActionBarSize;
        //int FrameWidth = WindowWidth; excessive local variable
        WindowsLayout.setOrientation((orientation) ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
        for(int i = 0; i < windows; i++){
            ViewGroup.LayoutParams layoutParams = WindowLayout[i].getLayoutParams();
            layoutParams.width = WindowWidth;
            layoutParams.height = WindowHeight;
            WindowLayout[i].setLayoutParams(layoutParams);
            ViewGroup.LayoutParams spinnerParams = WindowSpinner[i].getLayoutParams();
            spinnerParams.width = WindowWidth;
            spinnerParams.height = ActionBarSize;
            WindowSpinner[i].setLayoutParams(spinnerParams);
            ViewGroup.LayoutParams frameParams = WindowFrame[i].getLayoutParams();
            frameParams.width = WindowWidth;
            frameParams.height = FrameHeight;
            WindowFrame[i].setLayoutParams(frameParams);
        }
    }

}
