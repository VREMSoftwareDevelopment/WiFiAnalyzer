package com.vrem.wifianalyzer.apalias.model;

import java.util.List;
import java.util.Map;

/**
 * Service Interface to identify a Wifi access point
 */
public interface APAliasService
{
    /**
     * Looks up the alias database whether the given BSSID has an associated alias name
     *
     * @param BSSID the BSSID of the given Access Point
     * @return the name if found in the database or NULL if not found
     */
    String findAliasForBSSID(String BSSID);

    /**
     * Looks up the alias database based on the given pattern, and returns the associated alias name
     *
     * @param pattern the BSSID search pattern (regexp)
     * @return the name if the given AP based on the pattern
     */
    String findAliasForPattern(String pattern);

    /**
     * Adds a new BSSID -> alias association or changes the alias if an association with the given BSSID
     * already exists
     * @param BSSID the BSSID of the Access Point
     * @param alias the alias to be added or changed
     */
    void addAliasForBSSID(String BSSID, String alias);

    /**
     * Removes the alias association for the given BSSID
     *
     * @param BSSID the BSSID to be removed from the alias association
     */
    void removeAliasForBSSID(String BSSID);

    /**
     * Returns the BSSID - alias associations
     * @return a copy of the BSSID - alias associations map
     */
    Map<String, String> getBSSIDMap();

    /**
     * Returns the BSSIDs which have an alias
     * @return list of BSSIDs
     */
    List<String> getBSSIDs();
}
