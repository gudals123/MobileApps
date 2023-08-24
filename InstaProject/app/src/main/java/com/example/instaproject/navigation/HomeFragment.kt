package com.example.instaproject.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.instaproject.R
import com.google.firebase.firestore.FirebaseFirestore
import com.example.instaproject.navigation.model.ContentDTO
import com.google.firebase.auth.FirebaseAuth

class HomeFragment: Fragment(){
    var firestore : FirebaseFirestore? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_detail, container, false)
        firestore = FirebaseFirestore.getInstance()
        return view
    }

    inner class DetailViewRecyclerViewAdapter : RecyclerView.Adapter<ViewHolder>(){
        var contentDTOs: ArrayList<ContentDTO> = arrayListOf()
        var contentUidList: ArrayList<String> = arrayListOf()
        init {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val currentUserId = currentUser?.uid

            firestore?.collection("users")?.document(currentUserId!!)
                ?.get()
                ?.addOnSuccessListener { userDocument ->
                    val currentUserNickname = userDocument.getString("nickname")
                    firestore?.collection("posts")
                        ?.document(currentUserId)
                        ?.collection("userPosts")
                        ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                            contentDTOs.clear()
                            contentUidList.clear()
                            // Sometimes, This code returns null of querySnapshot when it signs out
                            if (querySnapshot == null) return@addSnapshotListener

                            for (snapshot in querySnapshot.documents) {
                                val postId = snapshot.id
                                val caption = snapshot.getString("caption")
                                val downloadURL = snapshot.getString("downloadURL")
                                val creation = snapshot.getLong("creation")

                                val contentDTO = ContentDTO(
                                    uid = currentUserId,
                                    nikname = currentUserNickname,
                                    caption = caption,
                                    downloadURL = downloadURL,
                                    creation = creation
                                )

                                firestore?.collection("posts")
                                    ?.document(currentUserId)
                                    ?.collection("userPosts")
                                    ?.document(postId)
                                    ?.collection("comments")
                                    ?.addSnapshotListener { commentQuerySnapshot, commentException ->
                                        if (commentQuerySnapshot == null) return@addSnapshotListener

                                        val comments = mutableListOf<ContentDTO.Comment>()
                                        for (commentDocument in commentQuerySnapshot.documents) {
                                            val commentUid = commentDocument.id
                                            val commentNikname = commentDocument.getString("nikname")
                                            val comment = commentDocument.getString("comment")
                                            val timestamp = commentDocument.getLong("timestamp")

                                            val commentData = ContentDTO.Comment(
                                                uid = commentUid,
                                                nikname = commentNikname,
                                                comment = comment,
                                                timestamp = timestamp
                                            )

                                            comments.add(commentData)
                                        }

                                        contentDTO.Comments = comments
                                        contentDTOs.add(contentDTO)
                                        contentUidList.add(postId)
                                        notifyDataSetChanged()
                                    }
                            }
                        }
                }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        }

        override fun getItemCount(): Int {
            TODO("Not yet implemented")
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            TODO("Not yet implemented")
        }

    }
}