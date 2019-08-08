package com.hector.nailnewdexter

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//When modified colorAccent and add theme of material in style, watch this, because AppCompatActivity is for versions - 5
//The theme Material is for Android >= 5
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
