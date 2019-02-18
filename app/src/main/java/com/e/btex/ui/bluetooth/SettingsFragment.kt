package com.e.btex.ui.bluetooth

import android.os.Bundle
import android.view.View
import com.e.btex.R
import com.e.btex.base.BaseFragment
import com.e.btex.databinding.FragmentSettingsBinding
import com.e.btex.util.extensions.hideKeyboard
import kotlin.reflect.KClass


class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>(){
    override val viewModelClass: KClass<SettingsViewModel>
        get() = SettingsViewModel::class
    override val layoutId: Int
        get() = R.layout.fragment_settings

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            hideKeyboard()
            navController.navigateUp()
        }
    }
}