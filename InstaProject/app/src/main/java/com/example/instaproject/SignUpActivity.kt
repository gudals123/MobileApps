package com.example.instaproject

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.instaproject.databinding.ActivitySignUpBinding
import com.example.instaproject.databinding.ActivityStartBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    //private var userid ="ab"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))

            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e(ContentValues.TAG, "사용자 정보 요청 실패qwe", error)
                }
                else if (user != null) {

                    val db = Firebase.firestore
                    val uid = hashMapOf(
                        "bio" to "",
                        "name" to binding.nameEdittext.text.toString(),
                        "nickname" to binding.nicknameEdittext.text.toString(),
                        "profile_photo_URL" to ""
                    )
                    db.collection("users").document(user.id.toString()).set(uid).addOnSuccessListener { Log.d(
                        ContentValues.TAG, "DocumentSnapshot successfully written!") }
                        .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }

                }
            }

        }




    }
}