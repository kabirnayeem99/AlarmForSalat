package io.github.kabirnayeem99.alarmforsalat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.SalatTiming
import io.github.kabirnayeem99.alarmforsalat.databinding.ListItemSalatBinding

class SalatTimingsRecyclerViewAdapter() :
    RecyclerView.Adapter<SalatTimingsRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ListItemSalatBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var diferCallBack: DiffUtil.ItemCallback<SalatTiming> =
        object : DiffUtil.ItemCallback<SalatTiming>() {
            override fun areItemsTheSame(oi: SalatTiming, ni: SalatTiming) = oi.id == ni.id

            override fun areContentsTheSame(oi: SalatTiming, ni: SalatTiming) = oi == ni
        }

    var differ = AsyncListDiffer(this, diferCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListItemSalatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val salatTiming: SalatTiming = differ.currentList[position]

        with(holder) {
            with(differ.currentList[position]) {
                binding.tvSalatName.text = name
                binding.tvSalatTime.text = time
                binding.lsAlarmToggle.isOn = toggle
            }
        }

    }

    override fun getItemCount(): Int = differ.currentList.size

}