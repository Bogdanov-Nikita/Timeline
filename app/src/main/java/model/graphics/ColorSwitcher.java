package model.graphics;

import android.content.res.Resources;

import com.android.nik.timeline.R;

/**
 * Created by Nik on 12.03.2015.
 * Palette color switcher
 */
public class ColorSwitcher {
    //PaletteIndex
    public static final int color_50 = 0;
    public static final int color_100 = 1;
    public static final int color_200 = 2;
    public static final int color_300 = 3;
    public static final int color_400 = 4;
    public static final int color_500 = 5;
    public static final int color_600 = 6;
    public static final int color_700 = 7;
    public static final int color_800 = 8;
    public static final int color_900 = 9;
    public static final int color_A100 = 10;
    public static final int color_A200 = 11;
    public static final int color_A300 = 12;
    public static final int color_A400 = 13;
    public static final int color_A700 = 14;

    public static int getPrimaryColorByIndex(int ColorIndex,Resources res){
        int colors[] = res.getIntArray(R.array.primary_colors);
        if(colors != null && ColorIndex > 0 && ColorIndex <= colors.length){
            return colors[ColorIndex];
        }else{
            return 0;
        }
    }

    public static int getIndexByPrimaryColor(int Color,Resources res){
        int colors[] = res.getIntArray(R.array.primary_colors);
        if(colors != null){
            for(int i=0; i < colors.length; i++){
                if(colors[i] == Color){
                     return i;
                }
            }
            return 0;
        }else{
            return 0;
        }
    }

    public static int getColorFromPaletByPrimary(int PrimaryColor,int PaletteIndex,Resources res){
        int primarys[] = res.getIntArray(R.array.primary_colors);
        int index = 5;
        for(int i=0; i < primarys.length; i++){
            if(primarys[i] == PrimaryColor){
                index = i - 1;///т.к. первый white, а его в списке палитр нет
            }
        }

        int ColorPallet[] = null;

        switch (index){
            case 0:
                ColorPallet = res.getIntArray(R.array.red_palette);
                break;
            case 1:
                ColorPallet = res.getIntArray(R.array.pink_palette);
                break;
            case 2:
                ColorPallet = res.getIntArray(R.array.purple_palette);
                break;
            case 3:
                ColorPallet = res.getIntArray(R.array.deep_purple_palette);
                break;
            case 4:
                ColorPallet = res.getIntArray(R.array.indigo_palette);
                break;
            case 5:
                ColorPallet = res.getIntArray(R.array.blue_palette);
                break;
            case 6:
                ColorPallet = res.getIntArray(R.array.light_blue_palette);
                break;
            case 7:
                ColorPallet = res.getIntArray(R.array.cyan_palette);
                break;
            case 8:
                ColorPallet = res.getIntArray(R.array.teal_palette);
                break;
            case 9:
                ColorPallet = res.getIntArray(R.array.green_palette);
                break;
            case 10:
                ColorPallet = res.getIntArray(R.array.light_green_palette);
                break;
            case 11:
                ColorPallet = res.getIntArray(R.array.lime_palette);
                break;
            case 12:
                ColorPallet = res.getIntArray(R.array.yellow_palette);
                break;
            case 13:
                ColorPallet = res.getIntArray(R.array.amber_palette);
                break;
            case 14:
                ColorPallet = res.getIntArray(R.array.orange_palette);
                break;
            case 15:
                ColorPallet = res.getIntArray(R.array.deep_orange_palette);
                break;
            case 16:
                ColorPallet = res.getIntArray(R.array.brown_palette);
                break;
            case 17:
                ColorPallet = res.getIntArray(R.array.grey_palette);
                break;
            case 18:
                ColorPallet = res.getIntArray(R.array.blue_grey_palette);
                break;
        }

        if(ColorPallet != null && PaletteIndex >= 0 && PaletteIndex < ColorPallet.length) {
            return ColorPallet[PaletteIndex];
        }else{
            return PrimaryColor;
        }
    }
}
