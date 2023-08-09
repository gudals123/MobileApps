package com.example.instaproject

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.instaproject.databinding.ActivityUploadBinding

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    lateinit var bitmap: Bitmap
    private val GALLERY = 1
    private val REQUEST_IMAGE_CAPTURE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cancel 버튼 클릭 시 메인으로 돌아감
        val intent = Intent(this, MainActivity::class.java)
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

        // Next 버튼 클릭 시 사진, 글 업로드 후 메인으로
        binding.btnUploadNext.setOnClickListener {
            Toast.makeText(this, "업로드 완료", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }

    } //onCreate

    private val activityResultL: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK && it.data != null) {
            val uri = it.data!!.data

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
            bitmap = extras?.get("data") as Bitmap

            Glide.with(this)
                .load(bitmap)
                .into(binding.uploadSelectedImage)
        }
    }

}