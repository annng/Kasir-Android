package com.artevak.kasirpos.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.artevak.kasirpos.model.UserPreference

abstract class BaseFragment : Fragment(){

    lateinit var mUserPref: UserPreference
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mUserPref = UserPreference(requireContext())
    }
}