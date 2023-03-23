package com.example.mcamicroinstagram

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mcamicroinstagram.data.Api
import com.example.mcamicroinstagram.data.Client
import com.example.mcamicroinstagram.model.Photos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class ThumbnailsListUIModel(val list: List<Photos>)

class MainViewModel : ViewModel() {

    private val _mainViewModel = MutableStateFlow(ThumbnailsListUIModel(arrayListOf()))
    val mainViewModel = _mainViewModel.asStateFlow()
    fun updateMainViewModel(list: List<Photos>){
        _mainViewModel.update {
            it.copy(list = list)
        }
    }

    fun fetchData() {
        var instaPhotos: ArrayList<Photos>
        val apiInterface: Api? =
            Client().getClient()?.create(Api::class.java)
        val call: Call<List<Photos>>? = apiInterface?.getPhotos()
        call?.enqueue(object : Callback<List<Photos>> {
            override fun onResponse(
                call: Call<List<Photos>>,
                response: Response<List<Photos>>
            ) {
                if (response.isSuccessful) {
                    instaPhotos = response.body() as java.util.ArrayList<Photos>
                    Log.d("TAG", "Response = $instaPhotos")
                    updateMainViewModel(instaPhotos)
                } else {
                    fetchData()
                }
            }

            override fun onFailure(
                call: Call<List<Photos>>,
                t: Throwable
            ) {
                Log.d("TAG", "Response = $t")
            }
        })
    }
}