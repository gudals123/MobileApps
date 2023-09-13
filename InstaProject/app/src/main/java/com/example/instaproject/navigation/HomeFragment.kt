package com.example.instaproject.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instaproject.R
import com.example.instaproject.databinding.FragmentDetailBinding // Import View Binding
import com.example.instaproject.databinding.ItemDetailBinding // Import View Binding
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.example.instaproject.navigation.model.ContentDTO
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private var firestore: FirebaseFirestore? = null
    private var uid: String? = null
    private var _binding: FragmentDetailBinding? = null // View Binding 변수 추가
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false) // View Binding 초기화
        val view = binding.root
        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid

        binding.detailviewfragmentRecyclerview.adapter = DetailViewRecyclerViewAdapter()
        binding.detailviewfragmentRecyclerview.layoutManager = LinearLayoutManager(activity)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // View Binding 변수 해제
    }

    inner class DetailViewRecyclerViewAdapter : RecyclerView.Adapter<DetailViewRecyclerViewAdapter.CustomViewHolder>() {
        var contentDTOs: ArrayList<ContentDTO> = arrayListOf()
        var contentUidList: ArrayList<String> = arrayListOf()

        init {
            firestore?.collection("images")?.orderBy("timestamp")
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    contentDTOs.clear()
                    contentUidList.clear()
                    // Sometimes, This code returns null of querySnapshot when it signs out
                    if (querySnapshot == null) return@addSnapshotListener

                    for (snapshot in querySnapshot.documents) {
                        val item = snapshot.toObject(ContentDTO::class.java)
                        contentDTOs.add(item!!)
                        contentUidList.add(snapshot.id)
                    }
                    notifyDataSetChanged()
                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
            val binding = ItemDetailBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ) // ItemDetailBinding 초기화
            return CustomViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return contentDTOs.size
        }


        inner class CustomViewHolder(private val binding: ItemDetailBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(contentDTO: ContentDTO) {
                // UserId
                binding.detailviewitemProfileTextview.text = contentDTO.userId

                // Image
                Glide.with(binding.root.context).load(contentDTO.imageUrl)
                    .into(binding.detailviewitemImageviewContent)

                // Explain of content
                binding.detailviewitemExplainTextview.text = contentDTO.explain
                // Likes
                binding.detailviewitemFavoritecounterTextview.text =
                    "Likes ${contentDTO.favoriteCount}"

                // This code is when the button is clicked
                binding.detailviewitemFavoriteImageview.setOnClickListener {
                    favoriteEvent(adapterPosition)
                }

                // This code is when the page is loaded
                if (contentDTO.favorites.containsKey(uid)) {
                    // This is like status
                    binding.detailviewitemFavoriteImageview.setImageResource(R.drawable.ic_notif2)
                } else {
                    // This is unlike status
                    binding.detailviewitemFavoriteImageview.setImageResource(R.drawable.ic_notif)
                }

            }
        }



        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            val contentDTO = contentDTOs[position]
            holder.bind(contentDTO)
        }

        fun favoriteEvent(position: Int) {
            val tsDoc = firestore?.collection("images")?.document(contentUidList[position])
            firestore?.runTransaction { transaction ->

                val contentDTO = transaction.get(tsDoc!!).toObject(ContentDTO::class.java)

                if (contentDTO!!.favorites.containsKey(uid)) {
                    // When the button is clicked
                    contentDTO.favoriteCount = contentDTO.favoriteCount - 1
                    contentDTO.favorites.remove(uid)
                } else {
                    // When the button is not clicked
                    contentDTO.favoriteCount = contentDTO.favoriteCount + 1
                    contentDTO.favorites[uid!!] = true
                    //favoriteAlarm(contentDTOs[position].uid!!)

                }
                transaction.set(tsDoc, contentDTO)
            }
        }



    }
}