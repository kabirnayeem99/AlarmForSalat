package io.github.kabirnayeem99.alarmforsalat.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import io.github.kabirnayeem99.alarmforsalat.R
import io.github.kabirnayeem99.alarmforsalat.ui.fragments.AlarmFragment
import io.github.kabirnayeem99.alarmforsalat.ui.fragments.LocationFragment


class PagerAdapter(fm: FragmentManager, var context: Context) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
//        return Tabs.values().size
        return 2
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment = AlarmFragment()
        when (position) {
            0 -> fragment = AlarmFragment()
            1 -> fragment = LocationFragment()
        }
        return fragment
    }

    private val imageResId = intArrayOf(
        R.drawable.ic_alarm_clock,
        R.drawable.ic_location,
    )

    override fun getPageTitle(position: Int): CharSequence? {
//        // Generate title based on item position
//        // return tabTitles[position];
//        // Generate title based on item position
//        // return tabTitles[position];
//        val image: Drawable = context.resources.getDrawable(imageResId.get(position))
//        image.setBounds(0, 0, image.intrinsicWidth, image.intrinsicHeight)
//        val sb = SpannableString(" ")
//        val imageSpan = ImageSpan(image, ImageSpan.ALIGN_BOTTOM)
//        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        return sb
//        lateinit var title: String
//        if (position == 0) {
//            title = "Alarm"
//
//        } else if (position == 1) {
//            title = "Location"
//        }
//        return title
        return null
    }

}