package com.example.temperapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather?")
    fun getCityByName(@Query("q") cityName:String, @Query("appid") apiKey:String): Call<CityModel.Response>
}