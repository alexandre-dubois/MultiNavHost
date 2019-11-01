package me.moallemi.multinavhost.navigation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_main.bottomNavigationView
import kotlinx.android.synthetic.main.activity_main.dashboardTabContainer
import kotlinx.android.synthetic.main.activity_main.homeTabContainer
import kotlinx.android.synthetic.main.activity_main.notificationsTabContainer
import me.moallemi.multinavhost.MainActivity
import me.moallemi.multinavhost.R

class TabManager(private val mainActivity: MainActivity) {

    private val startDestinations = mapOf(
            R.id.navigation_home to R.id.homeFragment,
            R.id.navigation_dashboard to R.id.dashboardFragment,
            R.id.navigation_notifications to R.id.notificationsFragment
    )
    private var currentTabId: Int = R.id.navigation_home
    var currentController: NavController? = null

    fun initDefaultController() {
        currentController = navHomeController
    }

    private val navHomeController: NavController by lazy {
        mainActivity.findNavController(R.id.homeTab).apply {
            graph = navInflater.inflate(R.navigation.navigation_graph_main).apply {
                startDestination = startDestinations.getValue(R.id.navigation_home)
            }
        }
    }
    private val navDashboardController: NavController by lazy {
        mainActivity.findNavController(R.id.dashboardTab).apply {
            graph = navInflater.inflate(R.navigation.navigation_graph_main).apply {
                startDestination = startDestinations.getValue(R.id.navigation_dashboard)
            }
        }
    }
    private val navNotificationsController: NavController by lazy {
        mainActivity.findNavController(R.id.notificationsTab).apply {
            graph = navInflater.inflate(R.navigation.navigation_graph_main).apply {
                startDestination = startDestinations.getValue(R.id.navigation_notifications)
            }
        }
    }

    private val homeTabContainer: View by lazy { mainActivity.homeTabContainer }
    private val dashboardTabContainer: View by lazy { mainActivity.dashboardTabContainer }
    private val notificationsTabContainer: View by lazy { mainActivity.notificationsTabContainer }

    fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            switchTab(mainActivity.bottomNavigationView.selectedItemId)
        }
    }

    fun supportNavigateUpTo(upIntent: Intent) {
        currentController?.navigateUp()
    }

    fun onBackPressed() {
        currentController?.let {
            if (it.currentDestination?.id == startDestinations.getValue(currentTabId)) {
                mainActivity.finish()
            }
            it.popBackStack()
        } ?: run {
            mainActivity.finish()
        }
    }

    fun switchTab(tabId: Int) {
        currentTabId = tabId

        when (tabId) {
            R.id.navigation_home -> {
                currentController = navHomeController
                invisibleTabContainerExcept(homeTabContainer)
            }
            R.id.navigation_dashboard -> {
                currentController = navDashboardController
                invisibleTabContainerExcept(dashboardTabContainer)
            }
            R.id.navigation_notifications -> {
                currentController = navNotificationsController
                invisibleTabContainerExcept(notificationsTabContainer)
            }
        }
    }

    private fun invisibleTabContainerExcept(container: View) {
        homeTabContainer.isInvisible = true
        dashboardTabContainer.isInvisible = true
        notificationsTabContainer.isInvisible = true

        container.isInvisible = false
    }

}