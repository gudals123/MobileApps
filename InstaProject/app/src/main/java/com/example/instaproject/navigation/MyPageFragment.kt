package com.example.instaproject.navigation

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.instaproject.EditprofileActivity
import com.example.instaproject.R
import com.example.instaproject.databinding.ActivityMyprofileBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kakao.sdk.user.UserApiClient


class MyPageFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding = ActivityMyprofileBinding.inflate(layoutInflater) // View Binding 인스턴스 초기화
        //setContentView(binding.root) // root 뷰를 화면에 표시


        val db = Firebase.firestore

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.i(TAG, "사용자 정보 요청 성공")

                var uid1 = db.collection("users").document(user.id.toString())
                uid1.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                            binding.nickname.text = document.data?.get("nickname").toString()
                            binding.username.text = document.data?.get("name").toString()
                            binding.bio.text = document.data?.get("bio").toString()

                        } else {
                            Log.d(TAG, "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "get failed with ", exception)
                    }

            }
        }


        var storage = Firebase.storage
        // Create a storage reference from our app
        val storageRef = storage.reference

// Create a reference with an initial file path and name
        val pathReference = storageRef.child("profile/uid1profile.jpg")

// Create a reference to a file from a Google Cloud Storage URI
        val gsReference = storage.getReferenceFromUrl("gs://insta-d0334.appspot.com/박재정.jpg")


/*
        Glide.with(this)
            .load(gsReference)
            .into(binding.profileImage)
*/
        binding.profileImage.setImageResource(R.drawable.park)

        binding.profileSetting.setOnClickListener {
            val intent = Intent(
                activity,
                EditprofileActivity::class.java
            ) //fragment라서 activity intent와는 다른 방식
            startActivity(intent)
        }

        return binding.root
    }
}