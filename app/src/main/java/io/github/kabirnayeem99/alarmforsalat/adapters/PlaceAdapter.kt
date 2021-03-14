package io.github.kabirnayeem99.alarmforsalat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.City
import io.github.kabirnayeem99.alarmforsalat.databinding.ListItemPlaceBinding
import java.util.*


class PlaceAdapter(private val listener: (City) -> Unit) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>(), Filterable {


    inner class ViewHolder(val binding: ListItemPlaceBinding) :
        RecyclerView.ViewHolder(binding.root)


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
                itemView.setOnClickListener { listener(this) }
            }
        }
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    /*
    Search Filter functionality
     */
    override fun getFilter(): Filter {
        return filter
    }


    private var filter: Filter = object : Filter() {

        /*
        Invoked in a worker thread to filter the data according
        to the constraint. Results computed by the filtering
        operation are returned as a Filter
         */
        override fun performFiltering(searchEntry: CharSequence?): FilterResults {
            val filteredCities: MutableList<City> = mutableListOf()

            if (searchEntry == null || searchEntry.isEmpty()) {
                filteredCities.addAll(differ.currentList)
            } else {
                val searchEntryPattern =
                    searchEntry.toString().toLowerCase(Locale.getDefault()).trim()
                for (city in differ.currentList) {
                    if (city.city.toLowerCase(Locale.ROOT)
                            .contains(searchEntryPattern) || city.country.toLowerCase(Locale.getDefault())
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