package com.vrem.wifianalyzer;

import android.net.wifi.WifiManager;

enum Security {
    NONE(R.drawable.empty),
    WEP(R.drawable.unlock),
    WPA2(R.drawable.lock),
    WPA(R.drawable.lock);

    private final int imageResource;

    private Security(int imageResource) {
        this.imageResource = imageResource;
    }

    int getImageResource() {
        return imageResource;
    }

    static Security find(String value) {
        if (value != null) {
            value = value.toUpperCase();
            for (Security security: Security.values()) {
                if (value.contains(security.name())) {
                    return security;
                }
            }
        }
        return Security.NONE;
    }
}
