package com.example.instaproject

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.instaproject.databinding.ActivityUploadBinding


lateinit var bitmap: Bitmap
lateinit var imageUri: Uri
class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    var imageType = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cancel 버튼 클릭 시 메인으로 돌아감
        var intent = Intent(this, MainActivity::class.java)
        binding.btnUploadCancel.setOnClickListener {
            startActivity(intent)
            Toast.makeText(this, "게시물 작성 취소", Toast.LENGTH_SHORT).show()
        }

        // Library 버튼 클릭 시 갤러리로
        binding.btnUploadLibrary.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResultL.launch(intent)
            //startActivityForResult(intent, GALLERY)
        }

        // Camera 버튼 클릭 시 카메라로
        binding.btnUploadCamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activityResultC.launch(intent)
            /*
                if (checkPermission()) {
                    dispatchTakePictureIntent()
                } else {
                    requestPermission()
                }
                 */
        }

        // Next 버튼 클릭 시 사진가지고 설명,태그 작성 화면으로 넘어감
        binding.btnUploadNext.setOnClickListener {
            if(imageType == 1){
                intent = Intent(applicationContext, UploadExplainActivity::class.java)
                intent.putExtra("path", imageUri)
                intent.putExtra("typeNum", imageType)
                startActivity(intent)
            }else if(imageType == 2){
                intent = Intent(applicationContext, UploadExplainActivity::class.java)
                intent.putExtra("path", bitmap)
                intent.putExtra("typeNum", imageType)
                startActivity(intent)
            }


        }

    } //onCreate

    private val activityResultL: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK && it.data != null) {
            val uri = it.data!!.data
            imageType = 1
            imageUri = uri!!
            //bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

            Glide.with(this)
                .load(uri)
                .into(binding.uploadSelectedImage)
        }
    }

    private val activityResultC: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK && it.data != null) {
            val extras = it.data!!.extras
            imageType = 2
            bitmap = extras?.get("data") as Bitmap

            Glide.with(this)
                .load(bitmap)
                .into(binding.uploadSelectedImage)
        }
    }

}
