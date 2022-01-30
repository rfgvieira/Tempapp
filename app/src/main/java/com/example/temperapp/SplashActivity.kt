package com.example.temperapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.temperapp.databinding.ActivitySplashBinding
import com.google.android.gms.location.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SplashActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var  fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        context = this
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
        setContentView(binding.root)

    }

    private fun getLastLocation(){
        if(checkPermission()){
            if(isLocationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener{ task ->
                    var location = task.result
                    if(location == null){
                        getNewLocation()
                    } else{
                        getData(context,getCity(location.latitude,location.longitude))
                    }
                }
            } else
                Toast.makeText(this,"Habilite a localização imediatamente", Toast.LENGTH_SHORT).show()
        } else
            requestPermission()
    }



    private fun getNewLocation(){
        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 1000
            fastestInterval = 100
            numUpdates = 2
        }
        if(checkPermission()) {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.myLooper()!!
            )
        } else
            requestPermission()

    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)

            var lastLocation = p0.lastLocation
            getData(context, getCity(lastLocation.latitude,lastLocation.longitude))

        }
    }

    private fun getCity(lat : Double, long : Double) : String{
        var city = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var adress: MutableList<Address> = geoCoder.getFromLocation(lat,long,10)

        if (adress != null && adress.size > 0) {
            adress.forEach {
                if (it.getLocality() != null && it.getLocality().length > 0) {
                    city = it.getLocality();
                }
            }
        }

        return city
    }





    private fun getData(context: Context, city: String) {
        val retrofit = NetworkUtils.getRetrofitInstance("https://api.openweathermap.org/")
        val endpoint = retrofit.create(WeatherApi::class.java)
        val callback = endpoint.getCityByName("Jakarta", getString(R.string.weather_key))
        var cidade = city

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


    private fun checkPermission() : Boolean{
        if( ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            return true
        return false
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.INTERNET),
            44
        )
    }

    private fun isLocationEnabled() : Boolean{
        return (getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(
            LocationManager.GPS_PROVIDER) || (getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 44){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Permission","Tem permissão")
            }
        }
    }
}