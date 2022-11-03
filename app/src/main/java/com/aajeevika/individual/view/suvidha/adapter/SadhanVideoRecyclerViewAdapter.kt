package com.aajeevika.individual.view.suvidha.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aajeevika.individual.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.individual.baseclasses.BaseViewHolder
import com.aajeevika.individual.databinding.ListItemSadhanVideoBinding
import com.aajeevika.individual.model.data_model.VideoData
import com.aajeevika.individual.utility.Constant
import com.aajeevika.individual.utility.UtilityActions
import com.aajeevika.individual.view.suvidha.ActivityVideoPlayer

class SadhanVideoRecyclerViewAdapter(private val dataList: ArrayList<VideoData>) : BaseRecyclerViewAdapter() {
    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        SadhanVideoListViewHolder(ListItemSadhanVideoBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    private inner class SadhanVideoListViewHolder(val dataBinding: ListItemSadhanVideoBinding) : BaseViewHolder(dataBinding) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]

            dataBinding.title = data.title_en
            dataBinding.video = "http://img.youtube.com/vi/${data.youtube_url}/0.jpg"
            UtilityActions.parseInterestDate(data.created_at)?.run {
                dataBinding.date = UtilityActions.formatVideoDate(this)
            }

            (dataBinding.root.layoutParams as RecyclerView.LayoutParams).width = (UtilityActions.windowWidth() * 0.4).toInt()

            dataBinding.root.setOnClickListener {
                val intent = Intent(context, ActivityVideoPlayer::class.java)
                intent.putExtra(Constant.VIDEOID, data.youtube_url)
                intent.putExtra(Constant.TITLE, data.title_en)
                context.startActivity(intent)
            }
        }
    }
}