# WiFiAnalyzer User Manual

## Table of Contents

- [Basic Navigation](#basic-navigation)
- [Basic Operations](#basic-operations)
- [Settings and Customization](#settings-and-customization)
  - [Advanced Settings](#advanced-settings)
- [Interpreting Wi-Fi Data](#interpreting-wi-fi-data)
  - [Signal Strength](#signal-strength)
  - [Channel Graph](#channel-graph)
  - [Time Graph](#time-graph)
  - [Channel Rating](#channel-rating)
  - [Wi-Fi Bands and Channel Width (HT/VHT)](#wi-fi-bands-and-channel-width-htvht)
  - [Vendor/OUI Lookup](#vendoroui-lookup)
  - [Estimated Distance](#estimated-distance)

## Basic Navigation

*   **Switching Bands:** Tap the title bar to switch between 2.4, 5 and 6 GHz Wi-Fi bands.
*   **Hidden SSIDs:** An SSID with `***` or `*hidden*` means the network is hidden.
*   **Security Icons:**
    *   <img src="images/ic_lock_black_18dp.png" alt="WPA2/WPA" height="20"> WPA3(SAE)/WPA2/WPA
    *   <img src="images/ic_lock_outline_black_18dp.png" alt="WEP/WPS" height="20"> WEP/WPS
    *   <img src="images/ic_lock_open_black_18dp.png" alt="Disabled" height="20"> Disabled
*   **Wi-Fi Standards (Requires Android OS 11+):**
    *   4 - <img src="images/ic_wifi_4.png" alt="4" height="20"> - 802.11n
    *   5 - <img src="images/ic_wifi_5.png" alt="5" height="20"> - 802.11ac
    *   6 - <img src="images/ic_wifi_6.png" alt="6" height="20"> - 802.11ax
    *   7 - <img src="images/ic_wifi_7.png" alt="7" height="20"> - 802.11be

## Basic Operations

*   **Real-time Analysis:** The main screen displays a list of nearby Wi-Fi networks with their signal strength and other details.
*   **Channel Graph:** Switch to the channel graph to see how access points are distributed across different Wi-Fi channels.
*   **Time Graph:** Select an access point and switch to the time graph to monitor its signal strength over time.
*   **Pause/Resume Scanning:** Use the pause and resume buttons to control the live scanning of Wi-Fi networks.
*   **Export Access Point Details:** You can export the details of the scanned access points for offline analysis. This option is usually found in the application's menu.

## Filtering

The filter dialog lets you focus on Wi-Fi networks that matter most to you by combining multiple filters.

**Available filter options:**

- **SSID:** Filter networks by their name (SSID). Enter all or part of a network name to show only matching networks.
- **WiFi Band:** Select which Wi-Fi bands to display (2.4 GHz, 5 GHz, 6 GHz). This option is available in the Access Points view.
- **Signal Strength:** Show only networks within a specific signal strength range.
- **Security Type:** Filter networks by their security protocol (e.g. WPA2, WPA3, etc).

**Filter dialog actions:**

- **Apply:** Apply the selected filters to update the network list.
- **Reset:** Clear all filters and return to the default view.
- **Close:** Close the filter dialog without making changes.

## Settings and Customization

WiFiAnalyzer offers several options to customize your experience. These are typically found in the app's settings menu.

*   **Themes:** Choose between Dark, Light, and System themes to match your preference.
*   **Access Point View:** You can switch between a complete view with all details for each access point, or a compact view that shows only the most essential information.
*   **Connection View:** Select the layout for displaying connection details (detailed or compact view).

### Advanced Settings

*   **Scan Speed:** Adjust the speed of Wi-Fi scanning. A faster speed will update the data more frequently, but may use more battery.
*   **Sort and Group Access Points:** Customize how the access point list is displayed. You can choose to sort the list by signal strength, SSID, or channel, and group related access points together.
*   **Graph Settings:**
    *   **Maximum Y-axis Value:** Set the maximum value for the Y-axis on the graphs to get a better view of the signal strength range.
    *   **Graph Legends:** Configure the legends for both the channel and time graphs to show or hide specific information.
*   **Wi-Fi Control:**
    *   **Turn Off Wi-Fi on Exit:** If enabled, the app will automatically turn off your device's Wi-Fi when you exit the application.
    *   **Keep Screen On:** You can choose to keep your device's screen on while using the app.
*   **Localization:**
    *   **Country Code:** Set your country to ensure the app uses the correct Wi-Fi channels for your region.
    *   **Language:** Change the display language of the application.
*   **Reset Settings:** This option allows you to reset all settings to their original default values.

## Interpreting Wi-Fi Data

This section helps you understand the data presented in WiFiAnalyzer, so you can make informed decisions about your Wi-Fi network.

### Signal Strength

Signal strength is measured in dBm (decibels per milliwatt) and typically ranges from -30 to -100.

*   **-30 dBm:** Excellent signal (very close to the router).
*   **-50 dBm:** Good signal.
*   **-67 dBm:** Reliable signal for most online activities.
*   **-70 dBm:** Weak signal. You may experience dropouts.
*   **-80 dBm:** Very weak signal. Unlikely to be usable.
*   **-90 dBm:** Unusable signal.

The closer the value is to 0, the stronger the signal.

### Channel Graph

The channel graph visualizes the Wi-Fi channels and the access points on them.

*   **X-axis:** Wi-Fi channels.
*   **Y-axis:** Signal strength (dBm).
*   **Each shape:** Represents an access point's signal. The width of the shape shows the channel width (20, 40, 80, 160 or 320 MHz) it's using.

An ideal Wi-Fi setup has your access point on a channel with little to no overlap from other networks. Look for channels that are less crowded to improve your Wi-Fi performance.

**Note:** On 5/6 GHz bands, pinch to zoom (in/out) and swipe (left/right) to scroll the channel graph’s X-axis.

### Time Graph

This graph shows the signal strength of a selected access point over time. It's useful for:

*   Finding dead spots in your home or office. As you move around, you can see how the signal strength changes.
*   Detecting interference. Sudden drops in signal strength might indicate interference from other devices.

### Channel Rating

WiFiAnalyzer analyzes the Wi-Fi networks and rates the channels. The rating is based on factors like signal strength, congestion, and interference. Use this rating to choose the best channel for your router. A higher star rating indicates a better channel.

### Wi-Fi Bands and Channel Width (HT/VHT)

*   **Bands (2.4, 5, 6 GHz):**
    *   **2.4 GHz:** Longer range, but more prone to interference from other devices (microwaves, cordless phones). Slower speeds.
    *   **5 GHz:** Shorter range, but less interference and faster speeds.
    *   **6 GHz:** Even shorter range, very low interference, and highest speeds (Wi-Fi 6E and newer).
*   **Channel Width (20, 40, 80, 160, 320 MHz):**
    *   A wider channel allows for higher data rates (faster Wi-Fi).
    *   However, wider channels are more susceptible to interference as they occupy more of the spectrum.
    *   HT/VHT detection shows you the channel width used by an access point.

### Vendor/OUI Lookup

The Vendor/OUI (Organizationally Unique Identifier) lookup feature helps you identify the manufacturer of the router or access point. This can be useful for:

*   Identifying unknown devices on your network.
*   Assessing the capabilities of a router based on its manufacturer.

The vendor name is displayed next to the access point in the list view.

### Estimated Distance

WiFiAnalyzer provides an estimated distance to each access point. This calculation is based on the signal strength and the frequency of the Wi-Fi signal, using the Free-space path loss model.

Keep in mind that this is an **estimation**. The accuracy can be affected by obstacles like walls, furniture, and other electronic devices. Use it as a general guideline to understand the proximity of different access points.
