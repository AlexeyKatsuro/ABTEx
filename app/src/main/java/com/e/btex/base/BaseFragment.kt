package com.e.btex.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import com.e.btex.util.extensions.findNavController
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseFragment<VB : ViewDataBinding, VM : BaseViewModel> : DaggerFragment() {

    protected open lateinit var binding: VB
    protected open lateinit var viewModel: VM
    protected open lateinit var navController: NavController

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    abstract val viewModelClass: KClass<VM>
    abstract val layoutId: Int


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         viewModel = crateViewModel(viewModelFactory, viewModelClass)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = createDataBinding(inflater, container)
        binding.lifecycleOwner = viewLifecycleOwner
        lifecycle

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = findNavController()
    }

    protected open fun createDataBinding(inflater: LayoutInflater, container: ViewGroup?): VB =
        DataBindingUtil.inflate(inflater, layoutId, container, false)

    protected open fun crateViewModel(provider: ViewModelProvider.Factory, clazz: KClass<VM>): VM =
        ViewModelProviders.of(this, provider).get(clazz.java)
}