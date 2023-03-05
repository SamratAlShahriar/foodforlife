package tws.foodforlife.app.others;

import android.graphics.Bitmap;

import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;

public class ImageColorParser {

    public static Palette.Swatch generateColor(Bitmap bitmap){
        Palette palette = Palette.from(bitmap).generate();
        Palette.Swatch vibrant = palette.getVibrantSwatch();
        return vibrant;
    }

    public static int colorArgb(int color){
        return ColorUtils.setAlphaComponent(color, 190);
    }


}
