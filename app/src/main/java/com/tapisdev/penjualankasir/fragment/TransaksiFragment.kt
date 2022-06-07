package com.tapisdev.penjualankasir.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.facebook.shimmer.ShimmerFrameLayout
import com.tapisdev.penjualankasir.activity.TambahBarangActivity
import com.tapisdev.penjualankasir.databinding.*

class TransaksiFragment : Fragment() {

    private var _binding: FragmentTransaksiBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    //lateinit var binding_shimmer : ShimmerSuratBinding
    lateinit var shimmerFrameLayout : ShimmerFrameLayout


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTransaksiBinding.inflate(inflater, container, false)
        val root: View = binding.root




        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): TransaksiFragment{
            val fragment = TransaksiFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}