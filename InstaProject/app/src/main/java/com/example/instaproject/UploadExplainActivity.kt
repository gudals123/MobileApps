package com.example.instaproject

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.instaproject.databinding.ActivityUploadExplainBinding


class UploadExplainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadExplainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadExplainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var imageType = intent.getIntExtra("typeNum", 1)
        if(imageType == 1){
            val uri = intent.getParcelableExtra<Uri>("path")
            Glide.with(this)
                .load(uri)
                .into(binding.pageExplainImage)
        }else if(imageType == 2){
            val bitmap = intent.getParcelableExtra<Bitmap>("path")
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

        }

    }
}