package com.example.mymoviesdb

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val FAVORITE = "favorite"

class SharedImplementation(context: Context) {


    private val sharedPrefs: SharedPreferences = context.getSharedPreferences(
        "favorite",
        Context.MODE_PRIVATE
    )

    fun saveFavorites(movie: List<com.example.mymoviesdb.network.dto.Result>) {
        sharedPrefs
            .edit()
            ?.putString(FAVORITE, Gson().toJson(movie))
            ?.apply()
    }

    fun getSelectedMovies(): MutableList<com.example.mymoviesdb.network.dto.Result> {

        val json = sharedPrefs.getString(FAVORITE, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<com.example.mymoviesdb.network.dto.Result>>() {}.type
            Gson().fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

}