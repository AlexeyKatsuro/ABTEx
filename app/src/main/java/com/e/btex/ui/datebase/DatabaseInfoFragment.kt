package com.e.btex.ui.datebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.e.btex.R
import com.e.btex.base.BaseFragment
import com.e.btex.data.ServiceState
import com.e.btex.data.ServiceStates
import com.e.btex.databinding.FragmentDatebaseBinding
import com.e.btex.util.extensions.executeAfter
import com.e.btex.util.extensions.observeValue
import kotlin.reflect.KClass

class DatabaseInfoFragment : BaseFragment<FragmentDatebaseBinding, DatabaseInfoViewModel>() {

    override val viewModelClass: KClass<DatabaseInfoViewModel>
        get() = DatabaseInfoViewModel::class
    override val layoutId: Int
        get() = R.layout.fragment_datebase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        with(binding) {
            lifecycleOwner = viewLifecycleOwner

            toolBar.setNavigationOnClickListener {
                navController.navigateUp()
            }

            buttonRemoveLocale.setOnClickListener {
                viewModel.resetLocaleStore()
            }

            buttonRemoveAll.setOnClickListener {
                viewModel.resetRemoteStore()
                viewModel.resetLocaleStore()
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.dataBaseSize.observeValue(viewLifecycleOwner){
            binding.executeAfter { databaseSize = it }
        }
        viewModel.lastSensor.observe(viewLifecycleOwner, Observer {
            binding.executeAfter {
                lastId = it?.id?.toString() ?: "-"
                lastTime = it?.timeText ?: "-"
            }
        })

        viewModel.isServiceOnline.observeValue(viewLifecycleOwner){
            binding.executeAfter {
                isDeviceOnline = it
            }
        }
    }
}