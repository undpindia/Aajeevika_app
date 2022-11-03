package com.aajeevika.individual.location.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aajeevika.individual.databinding.ListItemPlaceBinding
import com.aajeevika.individual.location.model.SearchLocationResponse

class AutoCompleteAdapter(
    private val list: ArrayList<SearchLocationResponse.Predictions>,
    private val clickListener: (Int) -> Unit
) : RecyclerView.Adapter<AutoCompleteAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            ListItemPlaceBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    inner class MyViewHolder(private val bind: ListItemPlaceBinding) :
        RecyclerView.ViewHolder(bind.root) {
        fun onBind(position: Int) {
            val place = list[position]
            bind.tvPlaceName.text = place.structured_formatting.main_text
            bind.tvPlaceAddress.text = place.description
            bind.root.setOnClickListener {
                clickListener(position)
            }
        }
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) = holder.onBind(position)
}