package com.vrem.wifianalyzer;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<ScanResult> {

    public ListViewAdapter(Activity activity, List<ScanResult> scanResults) {
        super(activity, android.R.layout.simple_list_item_1, scanResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        if (convertView == null){
            convertView = inflater.inflate(R.layout.column_row, null);
        }

        convertView.setBackgroundResource(position % 2 == 0
                ? R.color.bright_foreground_material_dark
                : R.color.background_material_light);

        ScanResult scanResult = getItem(position);

        ((TextView) convertView.findViewById(R.id.ssid)).setText(
                (scanResult.SSID.length() == 0 ? "HIDDEN" : scanResult.SSID) + " (" + scanResult.BSSID + ")");

        WifiLevel wifiLevel = WifiLevel.find(scanResult.level);
        Security security = Security.find(scanResult.capabilities);
        Frequency frequency = Frequency.find(scanResult.frequency);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.levelImage);
        imageView.setImageResource(wifiLevel.getImageResource());

        ImageView securityImage = (ImageView) convertView.findViewById(R.id.securityImage);
        securityImage.setImageResource(security.getImageResource());

        TextView textLevel = (TextView) convertView.findViewById(R.id.level);
        textLevel.setText(scanResult.level + "dBm");
        textLevel.setTextColor(getContext().getResources().getColor(wifiLevel.getColorResource()));

        ((TextView) convertView.findViewById(R.id.channel)).setText("" + frequency.channel(scanResult.frequency));
        ((TextView) convertView.findViewById(R.id.frequency)).setText(" (" + scanResult.frequency + "MHz)");
        ((TextView) convertView.findViewById(R.id.capabilities)).setText(scanResult.capabilities);

        return convertView;
    }
}
