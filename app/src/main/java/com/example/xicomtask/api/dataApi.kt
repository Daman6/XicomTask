package com.example.innobuzztask.api

import com.example.xicomtask.model.ImageModel
import com.example.xicomtask.model.UserModel
import com.example.xicomtask.model.UserResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface dataApi {

    @FormUrlEncoded
    @POST("/xttest/getdata.php")
    suspend fun getData(
        @Field("user_id") user_id: String,
        @Field("offset") offset:Int,
        @Field("type") type:String,
    ): Response<ImageModel>


    @Multipart
    @POST("/xttest/savedata.php")
    suspend fun saveData(
        @Part("first_name") first_name: String,
        @Part("last_name") last_name:String,
        @Part("email") email:String,
        @Part("phone") phone:String,
        @Part user_image: MultipartBody.Part,
    ): Response<UserResponse>

}

