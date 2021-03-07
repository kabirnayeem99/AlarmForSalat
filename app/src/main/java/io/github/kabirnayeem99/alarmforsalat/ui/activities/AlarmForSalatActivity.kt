package io.github.kabirnayeem99.alarmforsalat.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import io.github.kabirnayeem99.alarmforsalat.R
import io.github.kabirnayeem99.alarmforsalat.databinding.ActivityAlarmForSalatBinding
import io.github.kabirnayeem99.alarmforsalat.ui.fragments.AlarmFragment

class AlarmForSalatActivity : AppCompatActivity() {
    lateinit var fragmentAlarm: AlarmFragment
    private lateinit var binding: ActivityAlarmForSalatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmForSalatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFragments()

        makeCurrentFragment(fragmentAlarm)
    }

    private fun initFragments() {
        fragmentAlarm = AlarmFragment()
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(binding.flFragmentHolder.id, fragment)
            commit()
        }
    }
}