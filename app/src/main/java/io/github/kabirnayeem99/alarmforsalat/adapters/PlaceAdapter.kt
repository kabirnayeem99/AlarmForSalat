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


/**
 * Adapter class for Place Recycler View
 *
 * Adapter is a bridge between UI component and data source
 * that helps us to fill data in UI component.
 *
 * It implements [Filterable] which adds the getFilter() function
 * that can be used to constrain data with a filtering pattern.
 *
 * @param listener which is a lambda to implement onClickListener on Place
 *
 */
class PlaceAdapter(private val listener: (City) -> Unit) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>(), Filterable {


    inner class ViewHolder(val binding: ListItemPlaceBinding) :
        RecyclerView.ViewHolder(binding.root)


    /*
    Call when any data is changed
     */
    private val diffCallback = object : DiffUtil.ItemCallback<City>() {
        override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
            return oldItem.city == newItem.city
        }
    }


    /*
    The AsyncListDiffer can consume the values from a LiveData of
     List and present the data simply for an adapter.
     It computes differences in list contents via DiffUtil on a background thread
      as new List are received.
     */
    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ListItemPlaceBinding = ListItemPlaceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    /*
     * onBindViewHolder is called by RecyclerView to
     * display the data at the position parameter.
     * @param holder [ViewHolder]
     * @param position [Int]
     */
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


    /*
    Count how many item is in the list
     */
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