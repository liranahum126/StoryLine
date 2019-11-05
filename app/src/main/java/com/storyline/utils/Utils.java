package com.storyline.utils;

import android.content.Context;

public class Utils {

    public static float dpTopixel(Context c, float dp) {
        float density = c.getResources().getDisplayMetrics().density;
        float pixel = dp * density;
        return pixel;
    }
}
