package com.vrem.wifianalyzer;

import android.net.wifi.WifiManager;

enum WifiLevel {
    ZERO(R.drawable.wifi0, R.color.wifi0),
    ONE(R.drawable.wifi1, R.color.wifi1),
    TWO(R.drawable.wifi2, R.color.wifi2),
    THREE(R.drawable.wifi3, R.color.wifi3),
    FOUR(R.drawable.wifi4, R.color.wifi4);

    private final int imageResource;
    private final int colorResource;

    private WifiLevel(int imageResource, int colorResource) {
        this.imageResource = imageResource;
        this.colorResource = colorResource;
    }

    int getColorResource() {
        return colorResource;
    }

    int getImageResource() {
        return imageResource;
    }

    static WifiLevel find(int level) {
        int maxLevel = WifiLevel.values().length;
        int signalLevel = WifiManager.calculateSignalLevel(level, maxLevel);
        if (signalLevel < 0) {
            return WifiLevel.ZERO;
        }
        if (signalLevel >= maxLevel) {
            return WifiLevel.FOUR;
        }
        return WifiLevel.values()[signalLevel];
    }
}
