package com.example.sopt_project.Activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.opengl.ETC1.isValid
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.sopt_project.DB.SharedPreferenceController
import com.example.sopt_project.Network.ApplicationController
import com.example.sopt_project.Network.NetworkService
import com.example.sopt_project.Network.Post.PostCommentResponse
import com.example.sopt_project.R
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

class CommentWriteActivity : AppCompatActivity() {

    val REQUEST_CODE_SELECT_IMAGE: Int = 1004

    val networkService: NetworkService by lazy{
        ApplicationController.instance.networkService
    }

    lateinit var selectedPicUri: Uri
    var product_id = -1
    var episode_id = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_write)

        configureToolbar()
    }

    private fun configureToolbar(){
        product_id = intent.getIntExtra("idx", -1)
        episode_id = intent.getIntExtra("episode_id", -1)
        if(product_id == -1 || episode_id == -1) finish()


        txt_toolbar_wirte_comment_cancel.setOnClickListener {
            finish()
        }

        txt_toolbar_wirte_comment_submit.setOnClickListener{
            postCommentResponse()
        }

        img_bottom_wirte_comment_carmera.setOnClickListener{

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
            intent.data = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_SELECT_IMAGE){
            if(requestCode == Activity.RESULT_OK){
                data?.let{

                    selectedPicUri = it.data
                    Glide.with(this).load(selectedPicUri)
                        .thumbnail(0.1f).into(img_comment_write_roll)
                }
            }
        }
    }

    private fun postCommentResponse(){
        val token = SharedPreferenceController.getUserToken(this)
        val title = edt_comment_write_title.text.toString()
        val comment = edt_comment_write_comment.text.toString()
        if(isValid(token, title, comment)){
            val title_rb = RequestBody.create(MediaType.parse("text/plain"), title)
            val comment_rb = RequestBody.create(MediaType.parse("text/plain"), comment)

            val options = BitmapFactory.Options()
            val inputStream: InputStream = contentResolver.openInputStream(selectedPicUri)
            val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
            val photoBody = RequestBody.create(MediaType.parse("image/jpg"), byteArrayOutputStream.toByteArray())
            val picture_rb = MultipartBody.Part.createFormData("cmtImg", File(selectedPicUri.toString()).name, photoBody)
            val postCommentResponse = networkService.postCommentResponse(token, episode_id, comment_rb, picture_rb)

            postCommentResponse.enqueue(object: Callback<PostCommentResponse> {
                override  fun onFailure(call: Call<PostCommentResponse>, t: Throwable) {
                    Log.e("write comment failed", t.toString())
                }

                override fun onResponse(call: Call<PostCommentResponse>, response: Response<PostCommentResponse>){
                    if(response.isSuccessful){
                        toast(response.body()!!.message)
                        if(response.body()!!.status == 200)
                            finish()
                    }
                }
            })
        }
    }
}
