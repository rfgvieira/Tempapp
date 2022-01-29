package com.example.temperapp

import com.google.gson.annotations.SerializedName

class CityModel{
    data class Response (
        @SerializedName("coord")
        val coord: Coord,

        @SerializedName("weather")
        val weather: List<Weather>,

        @SerializedName("base")
        val base: String,

        @SerializedName("main")
        val main: Main,

        @SerializedName("visibility")
        val visibility: Long,

        @SerializedName("wind")
        val wind: Wind,

        @SerializedName("clouds")
        val clouds: Clouds,

        @SerializedName("dt")
        val dt: Long,

        @SerializedName("sys")
        val sys: Sys,

        @SerializedName("timezone")
        val timezone: Long,

        @SerializedName("id")
        val id: Long,

        @SerializedName("name")
        val name: String,

        @SerializedName("cod")
        val cod: Long
    ){
        override fun toString(): String {
            return "CityModel(weather=$weather, base='$base', main=$main, visibility=$visibility, wind=$wind, clouds=$clouds, dt=$dt, sys=$sys, timezone=$timezone, id=$id, name='$name', cod=$cod)"
        }

        fun desiredArray(): ArrayList<String> {
            return  ArrayList(listOf(weather[0].main,weather[0].description,main.temp.toString(),main.humidity.toString(),wind.speed.toString(),main.pressure.toString()))
        }
    }

    data class Clouds (
        @SerializedName("all")
        val all: Long
    )

    data class Coord (
        @SerializedName("lon")
        val lon: Double,

        @SerializedName("lat")
        val lat: Double
    )

    data class Main (
        @SerializedName("temp")
        val temp: Double,

        @SerializedName("fellsLike")
        val feelsLike: Double,

        @SerializedName("tempMin")
        val tempMin: Double,

        @SerializedName("tempMax")
        val tempMax: Double,

        @SerializedName("pressure")
        val pressure: Long,

        @SerializedName("humidity")
        val humidity: Long
    ){
        override fun toString(): String {
            return super.toString()
        }
    }

    data class Sys (
        @SerializedName("type")
        val type: Long,

        @SerializedName("id")
        val id: Long,

        @SerializedName("country")
        val country: String,

        @SerializedName("sunrise")
        val sunrise: Long,

        @SerializedName("sunset")
        val sunset: Long
    )

    data class Weather (
        @SerializedName("id")
        val id: Long,

        @SerializedName("main")
        val main: String,

        @SerializedName("description")
        val description: String,

        @SerializedName("icon")
        val icon: String
    )

    data class Wind (
        @SerializedName("speed")
        val speed: Double,

        @SerializedName("deg")
        val deg: Long
    )
}