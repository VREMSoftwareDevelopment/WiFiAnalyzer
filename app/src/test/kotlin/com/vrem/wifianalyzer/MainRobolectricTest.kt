package com.vrem.wifianalyzer

import android.Manifest
import android.app.AlertDialog
import android.app.Application
import android.os.Build
import android.os.Looper
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.material.navigation.NavigationView
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.Shadows
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowAlertDialog

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.VANILLA_ICE_CREAM])
class MainRobolectricTest {
    private val pause = "Pause"
    private val play = "Play"

    private lateinit var activityController: ActivityController<MainActivity>
    private lateinit var activity: MainActivity

    @Before
    fun setUp() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        val shadowApplication = Shadows.shadowOf(application)
        shadowApplication.grantPermissions(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE
        )
        activityController = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .start()
            .resume()
            .visible()
        activity = activityController.get()
        Shadows.shadowOf(Looper.getMainLooper()).idle()
    }

    @After
    fun tearDown() {
        activityController.destroy()
    }

    @Test
    fun navigation() {
        val navigationView = activity.findViewById<NavigationView>(R.id.nav_drawer)
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)

        fun selectMenuItem(index: Int, expectedTitle: String) {
            val menuItem = navigationView.menu.getItem(index)
            activity.onNavigationItemSelected(menuItem)
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            Assertions.assertThat(toolbar.title.toString()).isEqualTo(expectedTitle)
        }

        fun selectMenuItemAndGoBack(index: Int, expectedTitle: String) {
            selectMenuItem(index, expectedTitle)
            activity.onBackPressedDispatcher.onBackPressed()
            Shadows.shadowOf(Looper.getMainLooper()).idle()
        }

        listOf(
            1 to "Channel Rating",
            2 to "Channel Graph",
            3 to "Time Graph",
            0 to "Access Points",
            5 to "Available Channels",
            6 to "Vendors"
        ).forEach { (id, title) ->
            selectMenuItem(id, title)
        }

        listOf(
            7 to "Settings",
            8 to "About"
        ).forEach { (id, title) ->
            selectMenuItemAndGoBack(id, title)
        }
    }

    @Test
    fun scanner() {
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)
        val scannerMenuItem = toolbar.menu.findItem(R.id.action_scanner)
        Assertions.assertThat(scannerMenuItem).isNotNull
        Assertions.assertThat(scannerMenuItem.isVisible).isTrue

        val scannerActionView = toolbar.findViewById<View>(R.id.action_scanner)
        Assertions.assertThat(scannerActionView.contentDescription).isEqualTo(pause)

        activity.onOptionsItemSelected(scannerMenuItem)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        Assertions.assertThat(scannerActionView.contentDescription).isEqualTo(play)

        activity.onOptionsItemSelected(scannerMenuItem)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        Assertions.assertThat(scannerActionView.contentDescription).isEqualTo(pause)
    }

    @Test
    fun filter() {
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)
        val filterMenuItem = toolbar.menu.findItem(R.id.action_filter)
        Assertions.assertThat(filterMenuItem).isNotNull

        activity.onOptionsItemSelected(filterMenuItem)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        Assertions.assertThat(dialog).isNotNull
        Assertions.assertThat(dialog.isShowing).isTrue

        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).performClick()
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        Assertions.assertThat(dialog.isShowing).isFalse
    }
}