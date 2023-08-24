package com.example.instaproject.navigation.model

data class ContentDTO(var caption : String? = null,
                      var downloadURL : String? = null,
                      var uid : String? = null,
                      var nikname : String? = null,
                      var creation : Long? = null
                      ){
    data class Comment(var uid : String? = null,
                       var nikname : String? = null,
                       var comment : String? = null,
                       var timestamp : Long? = null)
}