package io.github.kabirnayeem99.alarmforsalat.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import io.github.kabirnayeem99.alarmforsalat.ui.fragments.AlarmFragment
import io.github.kabirnayeem99.alarmforsalat.ui.fragments.DateFragment
import io.github.kabirnayeem99.alarmforsalat.ui.fragments.MapsFragment


/**
 * Implementation of PagerAdapter that represents each page as a
 * Fragment that is persistently kept in the fragment manager as
 * long as the user can return to the page.
 *
 * The fragment of each page the user visits will be kept in memory,
 * though its view hierarchy may be destroyed when not visible.
 * @param fm [FragmentManager]
 * @param context [Context]
 */
class PagerAdapter(fm: FragmentManager, var context: Context) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return 3
    }

    /*
    returns item based on the position of the tab
    for instance, for first tab, it will return AlarmFragment and so on.
     */
    override fun getItem(position: Int): Fragment {
        var fragment: Fragment = AlarmFragment()
        when (position) {
            0 -> fragment = AlarmFragment.instance
            1 -> fragment = DateFragment.instance
            2 -> fragment = MapsFragment.instance
        }
        return fragment
    }


}