package com.vrem.wifianalyzer.apalias.model;

import android.content.Context;

public class APAliasServiceFactory
{
    private APAliasServiceFactory() {
        throw new IllegalStateException("Factory class");
    }

    public static APAliasService makeNamingService(Context context) {
        return new APAliasDB(context);
    }
}
