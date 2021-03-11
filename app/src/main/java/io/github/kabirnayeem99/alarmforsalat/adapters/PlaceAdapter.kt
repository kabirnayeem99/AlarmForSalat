package io.github.kabirnayeem99.alarmforsalat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.City
import io.github.kabirnayeem99.alarmforsalat.databinding.ListItemPlaceBinding
import java.util.*


class PlaceAdapter : RecyclerView.Adapter<PlaceAdapter.ViewHolder>(), Filterable {
    inner class ViewHolder(val binding: ListItemPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {}


    private val diffCallback = object : DiffUtil.ItemCallback<City>() {
        override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
            return oldItem.city == newItem.city
        }
    }
    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ListItemPlaceBinding = ListItemPlaceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(differ.currentList[position]) {
                with(binding) {
                    tvCityName.text = city
                    tvCountryName.text = country
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getFilter(): Filter {
        return filter
    }

    private var filter: Filter = object : Filter() {
        override fun performFiltering(searchEntry: CharSequence?): FilterResults {
            val filteredCities: MutableList<City> = mutableListOf()

            if (searchEntry == null || searchEntry.isEmpty()) {
                filteredCities.addAll(differ.currentList)
            } else {
                val searchEntryPattern = searchEntry.toString().toLowerCase().trim()
                for (city in differ.currentList) {
                    if (city.city.toLowerCase(Locale.ROOT)
                            .contains(searchEntryPattern) || city.country.toLowerCase()
                            .contains(searchEntryPattern)
                    ) {
                        filteredCities.add(city)
                    }
                }
            }
            val filterResults = FilterResults()
            filterResults.values = filteredCities
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            differ.submitList(results?.values as List<City>)
        }
    }
}