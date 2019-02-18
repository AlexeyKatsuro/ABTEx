package com.e.btex.ui.plot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.e.btex.R
import com.e.btex.base.BaseFragment
import com.e.btex.databinding.FragmentPlotBinding
import com.e.btex.util.extensions.executeAfter
import kotlin.reflect.KClass

class PlotFragment : BaseFragment<FragmentPlotBinding, PlotViewModel>() {


    override val viewModelClass: KClass<PlotViewModel>
        get() = PlotViewModel::class
    override val layoutId: Int
        get() = R.layout.fragment_plot

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        binding.toolbar.apply {
            inflateMenu(R.menu.menu_main)

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_settings -> {
                        navController.navigate(R.id.showSettingsFragment)
                        true
                    }
                    else -> false
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.lastSensors.observe(this, Observer {
            binding.executeAfter {
                sensors = it
            }
        })
    }

}