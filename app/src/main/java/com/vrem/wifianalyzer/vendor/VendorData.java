package com.vrem.wifianalyzer.vendor;

import android.support.annotation.NonNull;

class VendorData {
    private final String name;
    private final String mac;
    private long id;

    VendorData(long id, @NonNull String name, @NonNull String mac) {
        this.id = id;
        this.name = name;
        this.mac = mac;
    }

    long getId() {
        return id;
    }

    String getName() {
        return name;
    }

    String getMac() {
        return mac;
    }

}