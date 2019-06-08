package com.example.sopt_project.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sopt_project.Adapter.ProductOverviewRecyclerViewAdapter
import com.example.sopt_project.Data.ProductOverviewData
import com.example.sopt_project.Network.ApplicationController
import com.example.sopt_project.Network.Get.GetMainProductListResponse
import com.example.sopt_project.Network.NetworkService

import com.example.sopt_project.R
import kotlinx.android.synthetic.main.fragment_all_product_main.*
import kotlinx.android.synthetic.main.fragment_end_product_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EndProductMainFragment : Fragment() {

    val networkService : NetworkService by lazy{
        ApplicationController.instance.networkService
    }

    lateinit var productOverviewRecyclerViewAdapter: ProductOverviewRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_end_product_main, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var dataList: ArrayList<ProductOverviewData> = ArrayList()

        productOverviewRecyclerViewAdapter = ProductOverviewRecyclerViewAdapter(context!!, dataList)
        rv_product_overview_end.adapter = productOverviewRecyclerViewAdapter
        rv_product_overview_end.layoutManager = GridLayoutManager(context!!, 3)

        getMainProductListResponse()
    }

    private fun getMainProductListResponse(){
        val getMainProductListResponse = networkService.getMainProductListResponse("application/json", 2)
        getMainProductListResponse.enqueue(object: Callback<GetMainProductListResponse> {
            override fun onFailure(call: Call<GetMainProductListResponse>, t: Throwable) {
                Log.e("AllMainProductListFail", t.toString())
            }

            override fun onResponse(
                call: Call<GetMainProductListResponse>,
                response: Response<GetMainProductListResponse>
            ) {
                if(response.isSuccessful){
                    if(response.body()!!.status == 200){
                        val tmp: ArrayList<ProductOverviewData> = response.body()!!.data!!
                        productOverviewRecyclerViewAdapter.dataList = tmp
                        productOverviewRecyclerViewAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }
}
