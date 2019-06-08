package com.example.sopt_project.Data

data class CommentData(
    var product_id: Int,
    var episode_id: Int,
    var comment_id: Int,
    var img_url: String,
    var author: String,
    var comment: String,
    var publish_date: String
)