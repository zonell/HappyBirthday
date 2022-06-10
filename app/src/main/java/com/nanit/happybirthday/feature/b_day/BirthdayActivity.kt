package com.nanit.happybirthday.feature.b_day

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.nanit.happybirthday.R
import com.nanit.happybirthday.feature.main.model.Baby

class BirthdayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_birthday)
    }

    private fun getBaby() = Gson().fromJson(intent.getStringExtra(BABY), Baby::class.java)

    companion object {
        private const val BABY = "baby"

        fun newIntent(context: Context, baby: Baby): Intent { 
            val intent = Intent(context, BirthdayActivity::class.java)
            intent.putExtra(BABY, Gson().toJson(baby))
            return intent
        }
    }
}