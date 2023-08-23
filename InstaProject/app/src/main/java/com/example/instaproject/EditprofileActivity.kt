package com.example.instaproject

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.instaproject.databinding.ActivityEditprofileBinding
import com.example.instaproject.databinding.ActivityNaviBinding
import com.example.instaproject.navigation.MyPageFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient

class EditprofileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditprofileBinding // View Binding 변수 추가
    private lateinit var me: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Firebase.firestore



        binding = ActivityEditprofileBinding.inflate(layoutInflater) // View Binding 인스턴스 초기화
        setContentView(binding.root)

        binding.editprofileChangePhoto.setOnClickListener {
            binding.editprofileChangePhoto.text="bbbbddd"
            startActivity(Intent(this, MainActivity::class.java))
        }

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                Log.i(TAG, "사용자 정보 요청 성공")
                me = user.id.toString()

                var uid1 = db.collection("users").document(user.id.toString())
                uid1.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                            binding.nameEditText.setText(document.data?.get("name").toString())
                            binding.nicknameEditText.setText(document.data?.get("nickname").toString())
                            binding.bioEditText.setText(document.data?.get("bio").toString())

                        } else {
                            Log.d(TAG, "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "get failed with ", exception)
                    }

            }
        }



        binding.doneButton.setOnClickListener {
            val uid1 = hashMapOf(
                "bio" to binding.bioEditText.text.toString(),
                "name" to binding.nameEditText.text.toString(),
                "nickname" to binding.nicknameEditText.text.toString(),
                "profile_photo_URL" to ""
            )
            db.collection("users").document(me).set(uid1).addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) } //통으로 업데이트

            /*부분 업데이트
            val washingtonRef = db.collection("users").document(me)
// Set the "isCapital" field of the city 'DC'
            washingtonRef
                .update("name", binding.nameEditText.text.toString())
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }*/

            super.onBackPressed()
        }

        binding.cancelButton.setOnClickListener {
            super.onBackPressed()
        }

    }
}