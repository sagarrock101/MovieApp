package com.example.tmdb.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.tmdb.R
import com.example.tmdb.Utils
import com.example.tmdb.databinding.DilaogThemsBinding

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