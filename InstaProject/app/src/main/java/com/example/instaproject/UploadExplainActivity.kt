package com.example.instaproject

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.instaproject.databinding.ActivityUploadExplainBinding
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date


class UploadExplainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadExplainBinding
    var storage : FirebaseStorage? = null
    var photoUri : Uri? = null
    var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadExplainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Init Storage
        storage = FirebaseStorage.getInstance()

        var imageType = intent.getIntExtra("typeNum", 1)
        if(imageType == 1){
            photoUri = intent.getParcelableExtra<Uri>("path")
            Glide.with(this)
                .load(photoUri)
                .into(binding.pageExplainImage)
        }else if(imageType == 2){
            bitmap = intent.getParcelableExtra<Bitmap>("path")
            Glide.with(this)
                .load(bitmap)
                .into(binding.pageExplainImage)
        }


        //Back 버튼 클릭 시 사진 선택 화면으로 돌아감
        binding.btnUploadBack.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }

        //Done 버튼 클릭 시 사진, 글 내용, 태그 업로드
        binding.btnUploadDone.setOnClickListener {
            //Make filename
            var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            var imageFileName = "IMAGE_" + timestamp + "_.png"
            var storageRef = storage?.reference?.child("images")?.child(imageFileName)

            if(imageType == 1){
                //FileUpload
                storageRef?.putFile(photoUri!!)?.addOnSuccessListener {
                    Toast.makeText(this, "업로드 완료",Toast.LENGTH_LONG).show()
                }

            }else if(imageType == 2){


            }

            contentUpload()

        }

    }

    fun contentUpload(){
        //Make filename
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_" + timestamp + "_.png"

        var storageRef = storage?.reference?.child("images")?.child(imageFileName)

        //FileUpload
        storageRef?.putFile(photoUri!!)?.addOnSuccessListener {
            Toast.makeText(this, "업로드 완료",Toast.LENGTH_LONG).show()
        }
    }
}