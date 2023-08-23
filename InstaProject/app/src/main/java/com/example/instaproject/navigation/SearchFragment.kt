package com.example.instaproject.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.instaproject.R
import com.example.instaproject.databinding.ActivityMyprofileBinding
import com.example.instaproject.databinding.ActivitySearchBinding


class SearchFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var binding = ActivitySearchBinding.inflate(layoutInflater)
        var view = LayoutInflater.from(activity).inflate(R.layout.activity_search,container,false)
        return view
    }
}