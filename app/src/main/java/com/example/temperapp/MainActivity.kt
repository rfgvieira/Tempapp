package com.example.temperapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.temperapp.databinding.ActivityMainBinding
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var temp: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)


        val model = intent.getStringArrayListExtra("model")



        setValues(model)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        binding.btncelsius.setOnClickListener { btnCelsiusClick() }
        binding.btnfahrenheit.setOnClickListener { btnFahrenheitClick() }
        binding.btnkelvin.setOnClickListener { btnKelvinClick() }
    }


    private fun setValues(model: ArrayList<String>?) {
        setClima(model?.get(0),model?.get(1))
        temp = model?.get(2)?.toDouble() ?: 0.0
        btnCelsiusClick()
        binding.tvumidade.text = "${model?.get(3)}%"
        binding.tvvento.text = "${model?.get(4)} m/s"
        binding.tvpressao.text = "${model?.get(5)} hPa"

    }

    @SuppressLint("ResourceAsColor")
    private fun btnCelsiusClick(){
        resetBtnColors()
        binding.btncelsius.background = getDrawable(R.drawable.round_border_selected)
        binding.tvresulttemp.text = convertKtoC(temp) + "°C"
    }

    @SuppressLint("ResourceAsColor")
    private fun btnFahrenheitClick(){
        resetBtnColors()
        binding.btnfahrenheit.background = getDrawable(R.drawable.round_border_selected)
        var tempCelcius = convertKtoC(temp)
        if(tempCelcius.contains(","))
            tempCelcius = tempCelcius.replace(",",".")
        binding.tvresulttemp.text = convertCtoF(tempCelcius.toDouble())+ "°F"
    }

    @SuppressLint("ResourceAsColor")
    private fun btnKelvinClick(){
        resetBtnColors()
        binding.btnkelvin.background = getDrawable(R.drawable.round_border_selected)
        binding.tvresulttemp.text =String.format("%.1f", temp.toDouble())  + "K"
    }

    @SuppressLint("ResourceAsColor")
    private fun resetBtnColors(){
        binding.btncelsius.background = getDrawable(R.drawable.rounded_border)
        binding.btnfahrenheit.background = getDrawable(R.drawable.rounded_border)
        binding.btnkelvin.background = getDrawable(R.drawable.rounded_border)
    }

    private fun convertKtoC(temp : Double) : String{
        return String.format("%.1f", temp - 273)
    }

    private fun convertCtoK(temp : Double) : String{
        return String.format("%.1f", temp + 273)

    }

    private fun convertCtoF(temp : Double) : String{
        return String.format("%.1f", (temp * 1.8) + 32)

    }

    private fun convertFtoC(temp : Double) : String{
        return String.format("%.1f", (temp - 32) / 1.8)
    }


    private fun setClima(clima: String?, climadesc: String?){
        when(clima){
            "Clear" -> {
                binding.imvresultclima.setImageResource(R.drawable.ensolarado)
                binding.clresult.setBackgroundColor(resources.getColor(R.color.teal_200))
                binding.tvresultclima.text = "Ensolarado"}
            "Clouds" -> {
                if(climadesc == "few clouds: 11-25%"){
                    binding.imvresultclima.setImageResource(R.drawable.parcialmentenublado)
                    binding.clresult.setBackgroundColor(resources.getColor(R.color.teal_200))
                    binding.tvresultclima.text = "Parcialmente Nublado"
                } else{
                    binding.imvresultclima.setImageResource(R.drawable.nublado)
                    binding.clresult.setBackgroundColor(resources.getColor(R.color.azul))
                    binding.tvresultclima.text = "Nublado"
                }}
            "Drizzle" -> {
                binding.imvresultclima.setImageResource(R.drawable.garoa)
                binding.clresult.setBackgroundColor(resources.getColor(R.color.azul))
                binding.tvresultclima.text = "Garoa"}
            "Rain" -> {
                binding.imvresultclima.setImageResource(R.drawable.chuvoso)
                binding.clresult.setBackgroundColor(resources.getColor(R.color.azul))
                binding.tvresultclima.text = "Chuvoso"}
            "Thunderstorm" -> {
                binding.imvresultclima.setImageResource(R.drawable.tempestade)
                binding.clresult.setBackgroundColor(resources.getColor(R.color.azulescuro))
                binding.tvresultclima.text = "Tempestade"}
            "Snow" -> { binding.imvresultclima.setImageResource(R.drawable.neve)
                binding.clresult.setBackgroundColor(resources.getColor(R.color.azulescuro))
                binding.tvresultclima.text = "Neve"}
            "Mist","Smoke","Haze","Dust","Fog","Sand","Ash","Squall","Tornado" -> {
                binding.imvresultclima.setImageResource(R.drawable.nevoa)
                binding.clresult.setBackgroundColor(resources.getColor(R.color.azulescuro))
                binding.tvresultclima.text = clima}
        }
    }
}

