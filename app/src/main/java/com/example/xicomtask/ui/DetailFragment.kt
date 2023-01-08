package com.example.xicomtask.ui

import android.app.DownloadManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.bumptech.glide.Glide
import com.example.innobuzztask.utils.Resource
import com.example.innobuzztask.viewModel.DataViewModel
import com.example.xicomtask.MainActivity
import com.example.xicomtask.R
import com.example.xicomtask.adapter.ImageRecyAdapter
import com.example.xicomtask.databinding.FragmentDetailBinding
import com.example.xicomtask.databinding.FragmentHomeBinding
import com.example.xicomtask.model.UserModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URI


class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var imageUrl: String
    private lateinit var viewModel: DataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageUrl = requireArguments().getString("URL").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        binding.dataviewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.submitBtn.setOnClickListener {

            if (viewModel.checkUserDetail()){
                lifecycleScope.launch {
                    val dataI = upload(getImageUri(getBitmap()))
                    viewModel.saveUserDataResponse(viewModel._firstName.value.toString(),viewModel._lastName.value.toString(),viewModel._email.value.toString(),viewModel._phoneNum.value.toString(),dataI)
                }

                viewModel.saveData.observe(viewLifecycleOwner, Observer { response ->
                    when (response) {
                        is Resource.Success -> {
                            if(response.data?.status=="success"){
                                Snackbar.make(it,"SAVED",Snackbar.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(requireContext(), response.data?.message.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                        is Resource.Error -> {
                            response.message.let { message ->
                                Toast.makeText(
                                    requireContext(),
                                    "Error occured $message",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                        is Resource.Loading -> {

                        }
                    }

                })
                viewModel.saveUserData(UserModel(0,viewModel._firstName.value.toString(),viewModel._lastName.value.toString(),viewModel._email.value.toString(),viewModel._phoneNum.value.toString()))
            }
        }

        Glide.with(requireContext()).load(imageUrl).into(binding.imageView)

    }

    fun  upload(imageUri: Uri):MultipartBody.Part{
        val filesDir = requireContext().applicationContext.filesDir
        val file = File(filesDir,"image.png")

        val inputStream = requireContext().contentResolver.openInputStream(imageUri)
        val outputStream = FileOutputStream(file)
        inputStream!!.copyTo(outputStream)

        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("user_image",file.name,requestBody)

        return part
    }

    fun downloadImage(filename:String,imageurl:String){
        try {
            var downloadManager : DownloadManager?
            downloadManager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?

            var downloadUri = Uri.parse(imageurl)
            val request : DownloadManager.Request = DownloadManager.Request(downloadUri)

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(filename)
                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    File.separator + filename + ".jpg"
                )
            downloadManager?.enqueue(request)

        }catch (e:java.lang.Exception){
            Toast.makeText(requireContext(), "Erorr occured", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun getBitmap():Bitmap{
        val loading = ImageLoader(requireContext())
        val request = ImageRequest.Builder(requireContext())
            .data(imageUrl)
            .build()

        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

    private fun getImageUri(inImage: Bitmap): Uri {

        val tempFile = File.createTempFile("temprentpk", ".png")
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val bitmapData = bytes.toByteArray()

        val fileOutPut = FileOutputStream(tempFile)
        fileOutPut.write(bitmapData)
        fileOutPut.flush()
        fileOutPut.close()
        return Uri.fromFile(tempFile)
    }
}
