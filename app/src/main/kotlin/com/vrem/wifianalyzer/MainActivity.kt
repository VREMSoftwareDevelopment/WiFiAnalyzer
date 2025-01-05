/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2025 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.vrem.wifianalyzer

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.vrem.annotation.OpenClass
import com.vrem.util.EMPTY
import com.vrem.util.createContext
import com.vrem.util.findOne
import com.vrem.wifianalyzer.navigation.NavigationMenu
import com.vrem.wifianalyzer.navigation.NavigationMenuControl
import com.vrem.wifianalyzer.navigation.NavigationMenuController
import com.vrem.wifianalyzer.navigation.options.OptionMenu
import com.vrem.wifianalyzer.settings.Repository
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.wifi.accesspoint.ConnectionView
import com.vrem.wifianalyzer.wifi.scanner.ScannerService

@OpenClass
class MainActivity : AppCompatActivity(), NavigationMenuControl, OnSharedPreferenceChangeListener {
    internal lateinit var drawerNavigation: DrawerNavigation
    internal lateinit var mainReload: MainReload
    internal lateinit var navigationMenuController: NavigationMenuController
    internal lateinit var optionMenu: OptionMenu
    internal lateinit var connectionView: ConnectionView

    private var currentCountryCode: String = String.EMPTY

    override fun attachBaseContext(newBase: Context) =
        super.attachBaseContext(newBase.createContext(Settings(Repository(newBase)).languageLocale()))

    override fun onCreate(savedInstanceState: Bundle?) {
        val mainContext = MainContext.INSTANCE
        mainContext.initialize(this, largeScreen)

        val settings = mainContext.settings
        settings.initializeDefaultValues()
        setTheme(settings.themeStyle().themeNoActionBar)
        setWiFiChannelPairs(mainContext)

        mainReload = MainReload(settings)

        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.main_activity)

        settings.registerOnSharedPreferenceChangeListener(this)
        optionMenu = OptionMenu()

        keepScreenOn()

        val toolbar = setupToolbar()
        drawerNavigation = DrawerNavigation(this, toolbar)
        drawerNavigation.create()

        navigationMenuController = NavigationMenuController(this)
        navigationMenuController.currentNavigationMenu(settings.selectedMenu())
        onNavigationItemSelected(currentMenuItem())

        connectionView = ConnectionView(this)

        onBackPressedDispatcher.addCallback(this, MainActivityBackPressed(this))
    }

    public override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerNavigation.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerNavigation.onConfigurationChanged(newConfig)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (!MainContext.INSTANCE.permissionService.granted(requestCode, grantResults)) {
            finish()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun setWiFiChannelPairs(mainContext: MainContext) {
        val settings = mainContext.settings
        val countryCode = settings.countryCode()
        if (countryCode != currentCountryCode) {
            mainContext.configuration.wiFiChannelPair(countryCode)
            currentCountryCode = countryCode
        }
    }

    private val largeScreen: Boolean
        get() {
            val configuration = resources.configuration
            val screenLayoutSize = configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
            return screenLayoutSize == Configuration.SCREENLAYOUT_SIZE_LARGE ||
                screenLayoutSize == Configuration.SCREENLAYOUT_SIZE_XLARGE
        }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        val mainContext = MainContext.INSTANCE
        if (mainReload.shouldReload(mainContext.settings)) {
            MainContext.INSTANCE.scannerService.stop()
            recreate()
        } else {
            keepScreenOn()
            setWiFiChannelPairs(mainContext)
            update()
        }
    }

    fun update() {
        MainContext.INSTANCE.scannerService.update()
        updateActionBar()
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        closeDrawer()
        val currentNavigationMenu = findOne(NavigationMenu.entries, menuItem.itemId, NavigationMenu.ACCESS_POINTS)
        currentNavigationMenu.activateNavigationMenu(this, menuItem)
        return true
    }

    fun closeDrawer(): Boolean {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
            return true
        }
        return false
    }

    public override fun onPause() {
        val scannerService: ScannerService = MainContext.INSTANCE.scannerService
        scannerService.pause()
        scannerService.unregister(connectionView)
        updateActionBar()
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
        val scannerService: ScannerService = MainContext.INSTANCE.scannerService
        if (MainContext.INSTANCE.permissionService.permissionGranted()) {
            if (!MainContext.INSTANCE.permissionService.locationEnabled()) {
                startLocationSettings()
            }
            scannerService.resume()
        } else {
            scannerService.pause()
        }
        updateActionBar()
        scannerService.register(connectionView)
    }

    public override fun onStop() {
        MainContext.INSTANCE.scannerService.stop()
        super.onStop()
    }

    public override fun onStart() {
        super.onStart()
        if (MainContext.INSTANCE.permissionService.permissionGranted()) {
            MainContext.INSTANCE.scannerService.resume()
        } else {
            MainContext.INSTANCE.permissionService.check()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        optionMenu.create(this, menu)
        updateActionBar()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        optionMenu.select(item)
        updateActionBar()
        return true
    }

    fun updateActionBar() = currentNavigationMenu().activateOptions(this)

    override fun currentMenuItem(): MenuItem = navigationMenuController.currentMenuItem()

    override fun currentNavigationMenu(): NavigationMenu = navigationMenuController.currentNavigationMenu()

    override fun currentNavigationMenu(navigationMenu: NavigationMenu) {
        navigationMenuController.currentNavigationMenu(navigationMenu)
        MainContext.INSTANCE.settings.saveSelectedMenu(navigationMenu)
    }

    override fun navigationView(): NavigationView = navigationMenuController.navigationView

    fun mainConnectionVisibility(visibility: Int) {
        findViewById<View>(R.id.main_connection).visibility = visibility
    }

}