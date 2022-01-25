package com.example.temperapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.temperapp.databinding.ActivityLocationBinding
import com.google.android.gms.location.*


import java.util.jar.Manifest

class LocationActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLocationBinding
    private lateinit var  fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onResume() {
        super.onResume()
        binding.btncity.setOnClickListener {
            getLastLocation()
        }
    }

    private fun getLastLocation(){
        if(checkPermission()){
            if(isLocationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener{ task ->
                    var location = task.result
                    if(location == null){
                        getNewLocation()
                    } else{
                        binding.tvcity.text = "Sua localização é: \nLat:" + location.latitude + "; Long:" + location.longitude
                    }


                }
            } else
                Toast.makeText(this,"Habilite a localização imediatamente",Toast.LENGTH_SHORT).show()
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
            var lastLocation = p0.lastLocation
            binding.tvcity.text = "Sua localização é: \nLat:" + lastLocation.latitude + "; Long:" + lastLocation.longitude

        }
    }

    private fun checkPermission() : Boolean{
        if( ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED   )
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
        return (getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER) || (getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER)
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