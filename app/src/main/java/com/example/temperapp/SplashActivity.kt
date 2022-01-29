package com.example.temperapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.temperapp.databinding.ActivitySplashBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        getData(this)
        setContentView(binding.root)
    }

    private fun getData(context: Context) {
        val retrofit = NetworkUtils.getRetrofitInstance("https://api.openweathermap.org/")
        val endpoint = retrofit.create(WeatherApi::class.java)
        val callback = endpoint.getCityByName("Jakarta", getString(R.string.weather_key))

        callback.enqueue(object : Callback<CityModel.Response> {
            override fun onResponse(call: Call<CityModel.Response>, response: Response<CityModel.Response>) {
                var model = response.body()
                if (model != null) {
                    val intent = Intent(context,MainActivity::class.java)
                    intent.putStringArrayListExtra("model",model.desiredArray())//[clima,descrição do clima,temperatura,umidade,vento,pressão]
                    startActivity(intent)
                }
                else{
                    Log.d("nullApi","API Nula")
                }
            }

            override fun onFailure(call: Call<CityModel.Response>, t: Throwable) {
                Log.d("error", "${t.message}")
            }
        })
    }
}