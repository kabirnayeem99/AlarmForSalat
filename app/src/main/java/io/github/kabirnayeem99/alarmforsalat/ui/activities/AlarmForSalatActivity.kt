package io.github.kabirnayeem99.alarmforsalat.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.kabirnayeem99.alarmforsalat.databinding.ActivityAlarmForSalatBinding

class AlarmForSalatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAlarmForSalatBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}