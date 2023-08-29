package com.example.instaproject

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.instaproject.databinding.ActivityUploadExplainBinding
import com.example.model.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date


class UploadExplainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadExplainBinding
    var storage: FirebaseStorage? = null
    var photoUri: Uri? = null
    var bitmap: Bitmap? = null
    var firestore : FirebaseFirestore? = null
    var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadExplainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Init Storage
        storage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()

        //이전 화면에서 받아온 사진 띄우기
        var imageType = intent.getIntExtra("typeNum", 1)
        if (imageType == 1) {
            photoUri = intent.getParcelableExtra<Uri>("path")
            Glide.with(this)
                .load(photoUri)
                .into(binding.pageExplainImage)
        } else if (imageType == 2) {
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
            //사진 업로드 기능
            if (imageType == 1) {
                //갤러리 선택 사진 업로드
                contentUploadLib()
            } else if (imageType == 2) {
                //카메라 선택 사진 업로드
                contentUploadCam()
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    fun contentUploadLib() {
        //Make filename
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_" + timestamp + "_.png"
        var storageRef = storage?.reference?.child("images")?.child(imageFileName)

        //FileUpload
        storageRef?.putFile(photoUri!!)?.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                var ContentDTO = ContentDTO()

                //Insert downloadUrl of image
                ContentDTO.imageUrl = uri.toString()
                //Insert uid of user
                ContentDTO.uid = auth?.currentUser?.uid
                //Insert userId
                ContentDTO.userId = auth?.currentUser?.email
                //Insert explain
                ContentDTO.explain = binding.editEdittext.text.toString()
                //Insert tag
                ContentDTO.tag = binding.insertTagEdittext.text.toString()
                //Insert timestamp
                ContentDTO.timestamp = System.currentTimeMillis()

                firestore?.collection("images")?.document()?.set(ContentDTO)

                setResult(Activity.RESULT_OK)
                Toast.makeText(this, "업로드 완료", Toast.LENGTH_SHORT).show()

                finish()
            }
        }
    }

    fun contentUploadCam() {
        //Make filename
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_" + timestamp + "_.png"
        var storageRef = storage?.reference?.child("images")?.child(imageFileName)

        //카메라 선택 사진 업로드
        val bitmapFile = bitmap
        val baos = ByteArrayOutputStream()
        bitmapFile!!.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()
        storageRef?.putBytes(data!!)?.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                var ContentDTO = ContentDTO()

                //Insert downloadUrl of image
                ContentDTO.imageUrl = uri.toString()
                //Insert uid of user
                ContentDTO.uid = auth?.currentUser?.uid
                //Insert userId
                ContentDTO.userId = auth?.currentUser?.email
                //Insert explain
                ContentDTO.explain = binding.editEdittext.text.toString()
                //Insert tag
                ContentDTO.tag = binding.insertTagEdittext.text.toString()
                //Insert timestamp
                ContentDTO.timestamp = System.currentTimeMillis()

                firestore?.collection("images")?.document()?.set(ContentDTO)

                setResult(Activity.RESULT_OK)
                Toast.makeText(this, "업로드 완료", Toast.LENGTH_SHORT).show()

                finish()
            }
        }
    }
}