package com.aajeevika.individual.view.other.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.individual.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.individual.baseclasses.BaseViewHolder
import com.aajeevika.individual.databinding.ListItemFaqBinding
import com.aajeevika.individual.model.data_model.FaqData
import com.aajeevika.individual.view.other.ActivityFaqQuestion

class FaqsRecyclerViewAdapter( var mList :ArrayList<FaqData>) : BaseRecyclerViewAdapter() {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        FaqViewHolder(ListItemFaqBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = mList.size

    inner class FaqViewHolder(private val viewDataBinding: ListItemFaqBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            viewDataBinding.faq = mList[adapterPosition]
            viewDataBinding.root.setOnClickListener {
                val intent  = Intent(it.context,ActivityFaqQuestion::class.java)
                intent.putExtra("ques",mList[adapterPosition].get_question)
                intent.putExtra("title",mList[adapterPosition].title)
                it.context.startActivity(intent)
            }
            viewDataBinding.executePendingBindings()
        }
    }
}