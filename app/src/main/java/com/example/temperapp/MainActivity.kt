package com.example.temperapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.temperapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var ant = 0 //0 - Celsius, 1 - Fahrenheit, 2 - Kelvin
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        var result = getData()
        var a = ""
        //setClima()
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        binding.btncelsius.setOnClickListener { ant = btnCelsiusClick(ant) }
        binding.btnfahrenheit.setOnClickListener { ant = btnFahrenheitClick(ant) }
        binding.btnkelvin.setOnClickListener { ant = btnKelvinClick(ant) }
    }

    fun getData() : String {
        val retrofit = NetworkUtils.getRetrofitInstance("https://api.openweathermap.org/")
        val endpoint = retrofit.create(WeatherApi::class.java)
        val callback = endpoint.getCityByName("Jakarta", getString(R.string.weather_key))
        var result = ""
        callback.enqueue(object : Callback<CityModel.Response> {
            override fun onResponse(call: Call<CityModel.Response>, response: Response<CityModel.Response>) {
                var model = response.body()
                result = model.toString()
            }

            override fun onFailure(call: Call<CityModel.Response>, t: Throwable) {
                result = t.message ?: ""
            }
        })

        return  result
    }

    @SuppressLint("ResourceAsColor")
    private fun btnCelsiusClick(ant :Int) : Int{
        resetBtnColors()
        binding.btncelsius.background = getDrawable(R.drawable.round_border_selected)
        binding.tvresulttemp.text = convertTemp(ant,0).toString() + "°C"
        return 0
    }

    @SuppressLint("ResourceAsColor")
    private fun btnFahrenheitClick(ant :Int) : Int{
        resetBtnColors()
        binding.btnfahrenheit.background = getDrawable(R.drawable.round_border_selected)
        binding.tvresulttemp.text = convertTemp(ant,1).toString() + "°F"
        return 1
    }

    @SuppressLint("ResourceAsColor")
    private fun btnKelvinClick(ant :Int) : Int{
        resetBtnColors()
        binding.btnkelvin.background = getDrawable(R.drawable.round_border_selected)
        binding.tvresulttemp.text = convertTemp(ant,2).toString() + "K"
        return 2
    }

    @SuppressLint("ResourceAsColor")
    private fun resetBtnColors(){
        binding.btncelsius.background = getDrawable(R.drawable.rounded_border)
        binding.btnfahrenheit.background = getDrawable(R.drawable.rounded_border)
        binding.btnkelvin.background = getDrawable(R.drawable.rounded_border)
    }


    private fun convertTemp(ant: Int, next : Int) : Double{
        var temp  = binding.tvresulttemp.text as String
        if(ant == 0){
            temp = temp.substring(0,temp.length-2)
            return when (next) {
                1 -> convertCtoF(temp)
                2 -> convertCtoK(temp)
                else -> temp.toDouble()
            }
        }
        else if (ant == 1){
            temp = temp.substring(0,temp.length-2)
            return when (next) {
                0 -> convertFtoC(temp)
                2 -> convertCtoK(convertFtoC(temp).toString())
                else -> temp.toDouble()
            }
        }
        else{
            temp = temp.substring(0,temp.length-1)
            return when (next) {
                0 -> convertKtoC(temp)
                1 -> convertCtoF(convertKtoC(temp).toString())
                else -> temp.toDouble()
            }
        }
    }

    private fun convertKtoC(temp : String) : Double{
        return String.format("%.1f", temp.toDouble() - 273).toDouble()
    }

    private fun convertCtoK(temp : String) : Double{
        return String.format("%.1f", temp.toDouble() + 273).toDouble()

    }

    private fun convertCtoF(temp : String) : Double{
        return String.format("%.1f", (temp.toDouble() * 1.8) + 32).toDouble()

    }

    private fun convertFtoC(temp : String) : Double{
        return String.format("%.1f", (temp.toDouble() - 32) / 1.8).toDouble()
    }


    private fun setClima(clima :String){
        when(clima){
            "Clear Sky" -> {
                binding.imvresultclima.setImageResource(R.drawable.ensolarado)
                binding.clresult.setBackgroundColor(resources.getColor(R.color.teal_200))}
            "Parcialmente Nublado" ->  {
                binding.imvresultclima.setImageResource(R.drawable.parcialmentenublado)
                binding.clresult.setBackgroundColor(resources.getColor(R.color.teal_200))}
            "Muito Nublado" -> {
                binding.imvresultclima.setImageResource(R.drawable.muitonublado)
                binding.clresult.setBackgroundColor(resources.getColor(R.color.teal_200))}
            "Clouds" -> {
                binding.imvresultclima.setImageResource(R.drawable.nublado)
                binding.clresult.setBackgroundColor(resources.getColor(R.color.azul))}
            "Garoa" -> {
                binding.imvresultclima.setImageResource(R.drawable.garoa)
                binding.clresult.setBackgroundColor(resources.getColor(R.color.azul))}
            "Rain" -> {
                binding.imvresultclima.setImageResource(R.drawable.chuvoso)
                binding.clresult.setBackgroundColor(resources.getColor(R.color.azul))}
            "Tempestade" -> {
                binding.imvresultclima.setImageResource(R.drawable.tempestade)
                binding.clresult.setBackgroundColor(resources.getColor(R.color.azulescuro))}
        }
    }
}

