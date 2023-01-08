package com.example.innobuzztask.repo

import androidx.lifecycle.LiveData
import com.example.innobuzztask.api.RetrofitInstance
import com.example.newsapi.db.UserDatabase
import com.example.xicomtask.model.UserModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Field

class DataRepo(val db : UserDatabase) {

    suspend fun getImageData(user_id:String,offset:Int,type:String)= RetrofitInstance.api.getData(user_id,offset,type)


    suspend fun saveData(first_name: String,last_name:String,email:String,phone:String, user_image: MultipartBody.Part)= RetrofitInstance.api.saveData(first_name,last_name,email, phone,user_image)

    suspend fun upsert(userModel: UserModel) = db.getUserDao().upsert(userModel)

}
