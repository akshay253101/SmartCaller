package com.beetlestance.smartcaller.ui.dashboard

import android.os.Bundle
import com.beetlestance.smartcaller.R
import com.beetlestance.smartcaller.databinding.FragmentDashboardBinding
import com.beetlestance.smartcaller.ui.base.SmartCallerFragment

class DashboardFragment :
    SmartCallerFragment<FragmentDashboardBinding>(R.layout.fragment_dashboard) {

    override fun onViewCreated(binding: FragmentDashboardBinding, savedInstanceState: Bundle?) {
        binding.fragmentDashboardBottomNavigation.setItemSelected(R.id.blocked_contacts, true)
    }

}