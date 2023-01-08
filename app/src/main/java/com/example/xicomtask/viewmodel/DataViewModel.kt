package com.example.innobuzztask.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.innobuzztask.repo.DataRepo
import com.example.innobuzztask.utils.Resource
import com.example.xicomtask.model.ImageModel
import com.example.xicomtask.model.UserModel
import com.example.xicomtask.model.UserResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response

class DataViewModel(val repo: DataRepo,val context: Application):AndroidViewModel(context) {

    val getData: MutableLiveData<Resource<ImageModel>> = MutableLiveData()
    val saveData: MutableLiveData<Resource<UserResponse>> = MutableLiveData()
    var offsetM = 0
    var getDataResponse : ImageModel? = null


    val _firstName = MutableLiveData("")
    val _lastName = MutableLiveData("")
    val _email = MutableLiveData("")
    val _phoneNum = MutableLiveData("")


    fun checkUserDetail():Boolean{
        if (_firstName.value.toString().isNotEmpty()) {
            if (_lastName.value.toString().isNotEmpty()) {
                if (_email.value.toString().isNotEmpty()) {
                    if (_phoneNum.value.toString().isNotEmpty()) {
                        return true
                    } else {
                        Toast.makeText(context, "Please Enter a Phone Number", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(context, "Please Enter a Email", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Please Enter a Last Name", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Please Enter a First Name.", Toast.LENGTH_SHORT).show()
        }
        return false
    }


    fun getDataResponse(user_id:String,type:String ) =viewModelScope.launch {
        getData.postValue(Resource.Loading())
        val response = repo.getImageData(user_id,offsetM, type)
        getData.postValue(handleGetNetworkResponse(response))
    }

    private fun handleGetNetworkResponse(response: Response<ImageModel>) : Resource<ImageModel>{
        if (response.isSuccessful){
            response.body()?.let {networkResponse ->
                offsetM++
                if (getDataResponse == null){
                    getDataResponse = networkResponse
                }else{
                    val oldImage = getDataResponse?.images
                    val newImage = networkResponse.images
                    oldImage?.addAll(newImage)

                }
                return Resource.Success(getDataResponse ?: networkResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveUserDataResponse(first_name: String,last_name:String,email:String,phone:String, user_image: MultipartBody.Part) =viewModelScope.launch {
        saveData.postValue(Resource.Loading())
        val response = repo.saveData(first_name,last_name, email, phone,user_image)
        saveData.postValue(handleSaveUserResponse(response))
    }

    private fun handleSaveUserResponse(response: Response<UserResponse>) : Resource<UserResponse>{
        if (response.isSuccessful){
            response.body()?.let {networkResponse ->
                return Resource.Success(networkResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveUserData(userModel: UserModel){
        viewModelScope.launch {
            repo.upsert(userModel)
        }
    }
}