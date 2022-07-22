package com.artevak.kasirpos.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.artevak.kasirpos.data.model.shared.SharedPref
import org.koin.androidx.viewmodel.ext.android.viewModel
abstract class BaseFragment : Fragment(){
    val viewModel : BaseViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}