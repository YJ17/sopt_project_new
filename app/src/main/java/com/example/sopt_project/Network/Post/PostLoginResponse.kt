package com.example.sopt_project.Network.Post

import java.sql.ClientInfoStatus

data class PostLoginResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: String?
)