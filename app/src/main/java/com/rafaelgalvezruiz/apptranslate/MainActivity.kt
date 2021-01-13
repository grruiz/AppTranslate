package com.rafaelgalvezruiz.apptranslate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.rafaelgalvezruiz.apptranslate.api.API.retrofitService
import com.rafaelgalvezruiz.apptranslate.databinding.ActivityMainBinding
import com.rafaelgalvezruiz.apptranslate.dataclass.DetectionResponse
import com.rafaelgalvezruiz.apptranslate.dataclass.Language
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    var language = emptyList<Language>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initListener()
        getLanguages()
    }

    private fun initListener() {
        binding.btnDetectLanguage.setOnClickListener {
            val text: String = binding.etDescription.toString()
            if(text.isNotEmpty()){
                showLoading()
                getTextLanguage(text)
            }
        }
    }

    private fun showLoading() {
        binding.progressbar.visibility = View.VISIBLE
    }
    private fun hideLoading() {
        runOnUiThread {
            binding.progressbar.visibility = View.GONE
        }

    }

    private fun getTextLanguage(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = retrofitService.getTextLanguage(text)
            if (result.isSuccessful){
                checkResult(result.body())
            }else{
                showError()
            }
            cleanText()
            hideLoading()
        }
    }

    private fun cleanText() {
        binding.etDescription.setText("")
    }


    private fun checkResult(detectionResponse: DetectionResponse?) {
        if(detectionResponse != null && !detectionResponse.data.detections.isNullOrEmpty()){
            val correctLanguages = detectionResponse.data.detections.filter { it.isReliable }
            if(correctLanguages.isNotEmpty()) {
                val languageName = language.find { it.code == correctLanguages.first().language }
                if (languageName != null) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "El idioma es ${languageName.name}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getLanguages() {
        CoroutineScope(Dispatchers.IO).launch {
            val languages = retrofitService.getLanguage()
            if(languages.isSuccessful){
                language = languages.body()?: emptyList()
                showSucces()
            }else{
                showError()
            }
        }
    }

    private fun showSucces() {
        runOnUiThread {
            Toast.makeText(this@MainActivity,"Se hizo la llamada",Toast.LENGTH_SHORT).show()
        }
    }

    private fun showError() {
        runOnUiThread {
            Toast.makeText(this@MainActivity,"Error hacer la llamada",Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView() {

    }
}