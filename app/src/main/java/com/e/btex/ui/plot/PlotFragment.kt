package com.e.btex.ui.plot

import com.e.btex.R
import com.e.btex.databinding.FragmentPlotBinding
import com.e.btex.util.BtFragment
import kotlin.reflect.KClass

class PlotFragment : BtFragment<FragmentPlotBinding, PlotViewModel>() {

    override val viewModelClass: KClass<PlotViewModel>
        get() = PlotViewModel::class
    override val layoutId: Int
        get() = R.layout.fragment_plot

}