package com.example.instaproject.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.instaproject.R
import com.example.instaproject.navigation.model.ContentDTO
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment(){
    var firestore : FirebaseFirestore? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_detail,container,false)
        firestore = FirebaseFirestore.getInstance()
        return view
    }
    inner class DetailViewrecyclerviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var contentDTOs : ArrayList<ContentDTO> = arrayListOf()
        var contentUidList : ArrayList<String> = arrayListOf()

        init {

        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            TODO("Not yet implemented")
        }

        override fun getItemCount(): Int {
            TODO("Not yet implemented")
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            TODO("Not yet implemented")
        }

    }

}