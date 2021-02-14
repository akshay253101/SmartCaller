package com.beetlestance.smartcaller.ui.dashboard

import android.os.Bundle
import com.beetlestance.smartcaller.R
import com.beetlestance.smartcaller.databinding.FragmentDashboardBinding
import com.beetlestance.smartcaller.ui.base.SmartCallerFragment
import com.beetlestance.smartcaller.utils.extensions.setupWithNavController

class DashboardFragment :
    SmartCallerFragment<FragmentDashboardBinding>(R.layout.fragment_dashboard) {

    override fun onViewCreated(binding: FragmentDashboardBinding, savedInstanceState: Bundle?) {
        binding.fragmentDashboardBottomNavigation.setItemSelected(R.id.nav_graph_blocked, true)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        requireBinding().fragmentDashboardBottomNavigation.setupWithNavController(
            navGraphIds = NAV_GRAPH_IDS,
            containerId = requireBinding().fragmentDashboardNavHost.id,
            fragmentManager = childFragmentManager
        )
    }

    companion object {
        private val NAV_GRAPH_IDS = listOf(
            R.navigation.nav_graph_blocked,
            R.navigation.nav_graph_call_logs,
            R.navigation.nav_graph_contacts
        )
    }
}