package com.example.mcamicroinstagram

import android.content.Context
import android.util.Log
import android.widget.Toast
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


class InstaDetailsViewModel : ViewModel() {

    private val _instaViewModel = MutableStateFlow(Photos(0,0,"","",""))
    val instaViewModel = _instaViewModel.asStateFlow()

    private var title = String()

    fun titleUpdate(newTitle: String){
        _instaViewModel.update { it.copy(title = newTitle) }
        title = newTitle
    }

    fun onDeleteItem(context: Context, item: Int){

        val apiInterface: Api? = Client().getClient()?.create(Api::class.java)

        val deleteRequest: Call<Void>? = apiInterface?.deletePhoto(item)

        deleteRequest?.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("SUCCESS", "Response = ${response.body()}")
                Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("FAIL", "Response = $t")
                Toast.makeText(context, "Cannot delete item", Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun onUpdateItem(context: Context, item: Int, title: String){
        val photo = Photos(0,0,title,"","")

        val apiInterface: Api? = Client().getClient()?.create(Api::class.java)

        val patchRequest: Call<Photos>? = apiInterface?.patchPhoto(item,photo)

        patchRequest?.enqueue(object : Callback<Photos> {
            override fun onResponse(call: Call<Photos>, response: Response<Photos>) {
                if (response.isSuccessful){
                Log.d("SUCCESS", "Response = ${response.body()}")
                    Toast
                        .makeText(context, "Changes saved", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<Photos>, t: Throwable) {
                Log.d("FAIL", "Response = $t")
                Toast.makeText(context, "Cannot change item", Toast.LENGTH_SHORT).show()
            }
        })
    }
}