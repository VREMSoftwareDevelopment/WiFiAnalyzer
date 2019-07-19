package com.vrem.util;

import android.os.Build;

public class BuildUtils {
    private BuildUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isMinVersionQ() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

    public static boolean isMinVersionP() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }

    public static boolean isVersionP() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.P;
    }

    public static boolean isMinVersionN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static boolean isMinVersionM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isMinVersionL() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

}
