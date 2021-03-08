package io.github.kabirnayeem99.alarmforsalat.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import io.github.kabirnayeem99.alarmforsalat.ui.fragments.AlarmFragment
import io.github.kabirnayeem99.alarmforsalat.ui.fragments.MapsFragment


class PagerAdapter(fm: FragmentManager, var context: Context) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment = AlarmFragment()
        when (position) {
            0 -> fragment = AlarmFragment.instance
            1 -> fragment = MapsFragment.instance
        }
        return fragment
    }


}