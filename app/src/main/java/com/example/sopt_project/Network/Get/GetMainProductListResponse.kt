package com.example.sopt_project.Network.Get

import com.example.sopt_project.Data.ProductOverviewData
import java.sql.ClientInfoStatus

data class GetMainProductListResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: ArrayList <ProductOverviewData>?
)