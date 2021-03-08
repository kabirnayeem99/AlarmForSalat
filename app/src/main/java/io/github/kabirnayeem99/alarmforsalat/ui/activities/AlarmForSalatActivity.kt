package io.github.kabirnayeem99.alarmforsalat.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.kabirnayeem99.alarmforsalat.R
import io.github.kabirnayeem99.alarmforsalat.adapters.PagerAdapter
import io.github.kabirnayeem99.alarmforsalat.databinding.ActivityAlarmForSalatBinding
import io.github.kabirnayeem99.alarmforsalat.ui.fragments.AlarmFragment
import io.github.kabirnayeem99.alarmforsalat.ui.fragments.LocationFragment


class AlarmForSalatActivity : AppCompatActivity() {
    private lateinit var fragmentAlarm: AlarmFragment
    lateinit var fragmentLocation: LocationFragment
    private lateinit var binding: ActivityAlarmForSalatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmForSalatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFragments()
        initTabLayout()
    }

    private fun initTabLayout() {
        with(binding) {
            pager.apply {
                adapter = PagerAdapter(supportFragmentManager, this@AlarmForSalatActivity)
            }
            tabs.setupWithViewPager(binding.pager)
            tabs.getTabAt(0)?.setIcon(R.drawable.ic_alarm_clock)
            tabs.getTabAt(1)?.setIcon(R.drawable.ic_location)
        }

    }

    private fun initFragments() {
        fragmentAlarm = AlarmFragment()
        fragmentLocation = LocationFragment()
    }

//    private fun makeCurrentFragment(fragment: Fragment) {
//        supportFragmentManager.beginTransaction().apply {
//            replace(binding.flFragmentHolder.id, fragment)
//            commit()
//        }
//    }
}