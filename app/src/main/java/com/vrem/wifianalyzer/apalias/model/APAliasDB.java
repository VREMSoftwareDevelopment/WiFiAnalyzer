package com.vrem.wifianalyzer.apalias.model;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

class APAliasDB implements APAliasService
{
    private final Context context;
    private final Map<String, String> bssIdMap;
    private boolean loaded;

    public APAliasDB(Context context)
    {
        this.context = context;
        this.bssIdMap = new HashMap<>();
        this.loaded = false;
    }

    @Override
    public String findAliasForBSSID(String BSSID)
    {
        load(context);

        for (String pattern : bssIdMap.keySet())
        {
            if (Pattern.matches(pattern, BSSID)) return bssIdMap.get(pattern);
        }

        return null;
    }

    @Override
    public String findAliasForPattern(String pattern)
    {
        load(context);
        return bssIdMap.get(pattern);
    }

    @Override
    public void addAliasForBSSID(String BSSID, String alias)
    {
        bssIdMap.put(BSSID, alias);
        save(context);
    }

    @Override
    public void removeAliasForBSSID(String BSSID)
    {
        bssIdMap.remove(BSSID);
        save(context);
    }

    @Override
    public Map<String, String> getBSSIDMap()
    {
        return new HashMap<>(bssIdMap);
    }

    @Override
    public List<String> getBSSIDs()
    {
        return new ArrayList<>(bssIdMap.keySet());
    }

    private void save(@NonNull Context context)
    {
        JSONArray array = new JSONArray();

        if (!bssIdMap.isEmpty())
        {
            for (String key : bssIdMap.keySet())
            {
                JSONObject pairs = new JSONObject();
                try
                {
                    pairs.put("key", key);
                    pairs.put("value", bssIdMap.get(key));

                    array.put(pairs);
                }
                catch (JSONException e)
                {
                }
            }
        }

        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("NameDB", array.toString()).apply();
    }

    private void load(@NonNull Context context)
    {
        if (!loaded)
        {
            loaded = true;
            bssIdMap.clear();
            String nameDB = PreferenceManager.getDefaultSharedPreferences(context).getString("NameDB", null);

            if (nameDB != null)
            {
                try
                {
                    JSONArray array = new JSONArray(nameDB);
                    for (int i = 0;i < array.length();i++)
                    {
                        JSONObject obj = array.getJSONObject(i);
                        bssIdMap.put(obj.getString("key"), obj.getString("value"));
                    }
                }
                catch (JSONException e)
                {
                }
            }
        }
    }
}
