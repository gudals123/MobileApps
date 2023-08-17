package com.example.instaproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.instaproject.databinding.ActivityUploadExplainBinding

class UploadExplainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadExplainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadExplainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Back 버튼 클릭 시 사진 선택 화면으로 돌아감
        binding.btnUploadBack.setOnClickListener{
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }

        //Done 버튼 클릭 시 사진, 글 내용, 태그 업로드
        binding.btnUploadDone.setOnClickListener{

        }

    }
}