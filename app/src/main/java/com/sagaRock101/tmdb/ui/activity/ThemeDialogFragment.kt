package com.sagaRock101.tmdb.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.sagaRock101.tmdb.R
import com.sagaRock101.tmdb.databinding.DilaogThemsBinding

class ThemeDialogFragment : DialogFragment() {

    lateinit var binding: DilaogThemsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dilaog_thems, container, false)
        return binding.root
    }
}