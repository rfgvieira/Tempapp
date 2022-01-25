package com.example.temperapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.temperapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    
    
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ArrayAdapter.createFromResource(this,R.array.climarray,android.R.layout.simple_spinner_item).also{ adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spnInputclima.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        var clima = ""
        binding.spnInputclima.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
               clima = p0?.getItemAtPosition(p2).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        binding.btnInput.setOnClickListener {
            val intent = Intent(this,ResultActivity::class.java)
            intent.putExtra("clima",clima)
            intent.putExtra("temperatura",binding.edtInputtemp.text.toString())
            startActivity(intent)
        }
    }


}