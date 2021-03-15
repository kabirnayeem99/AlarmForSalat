package io.github.kabirnayeem99.alarmforsalat.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import io.github.kabirnayeem99.alarmforsalat.R
import io.github.kabirnayeem99.alarmforsalat.databinding.FragmentAlarmBinding
import io.github.kabirnayeem99.alarmforsalat.databinding.FragmentDateBinding
import io.github.kabirnayeem99.alarmforsalat.ui.activities.AlarmForSalatActivity
import io.github.kabirnayeem99.alarmforsalat.utils.Resource

class DateFragment : Fragment() {
    /*
    follows https://developer.android.com/topic/libraries/view-binding
    for view binding
  */
    private var _binding: FragmentDateBinding? = null

    private val binding get() = _binding!!

    companion object {
        val instance: DateFragment by lazy(LazyThreadSafetyMode.PUBLICATION) { DateFragment() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = (activity as AlarmForSalatActivity).viewModel

        viewModel.adhanTime.observe(viewLifecycleOwner,
            { resources ->
                when (resources) {
                    is Resource.Success -> {
                        resources.data?.data?.date?.hijri.let {
                            it?.let {
                                with(it) {
                                    binding.tvHijriDate.text = date
                                    binding.tvHijriDay.text = day
                                    binding.tvHijriMonthEn.text = month.en
                                    binding.tvHijriMonthAr.text = month.ar
                                }
                            }
                        }
                    }

                    is Resource.Error -> {
                        resources.message.let {
                            Toast.makeText(context, "There was an error. \n$it", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    else -> {
                        //loading
                    }
                }
            })
    }

}