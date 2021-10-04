package com.vrem.wifianalyzer.apalias;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.apalias.model.APAliasService;

public class APAliasAdapter extends ArrayAdapter<String>
{
    private final APAliasService aPAliasService;

    APAliasAdapter(@NonNull Context context, @NonNull APAliasService aPAliasService) {
        super(context, R.layout.apalias_details, aPAliasService.getBSSIDs());
        this.aPAliasService = aPAliasService;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = MainContext.INSTANCE.getLayoutInflater();
            view = layoutInflater.inflate(R.layout.apalias_details, parent, false);
        }
        String bssId = getItem(position);
        view.<TextView>findViewById(R.id.apalias_bssid)
                .setText(bssId);
        view.<TextView>findViewById(R.id.apalias_alias)
                .setText(aPAliasService.findAliasForPattern(bssId));
        return view;
    }
}
