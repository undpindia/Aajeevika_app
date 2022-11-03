package com.aajeevika.individual.view.other.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.individual.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.individual.baseclasses.BaseViewHolder
import com.aajeevika.individual.databinding.ListItemFaqBinding
import com.aajeevika.individual.databinding.ListItemFaqQuesBinding
import com.aajeevika.individual.model.data_model.FaqData
import com.aajeevika.individual.model.data_model.FaqQuestions
import com.aajeevika.individual.view.other.ActivityFaqDescription
import com.aajeevika.individual.view.other.ActivityFaqQuestion

class FaqsQuestionAdapter(var mList :ArrayList<FaqQuestions>) : BaseRecyclerViewAdapter() {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        FaqViewHolder(ListItemFaqQuesBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = mList.size

    inner class FaqViewHolder(private val viewDataBinding: ListItemFaqQuesBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            viewDataBinding.question = mList[adapterPosition]
            viewDataBinding.root.setOnClickListener {
                val intent  = Intent(it.context, ActivityFaqDescription::class.java)
                intent.putExtra("desc",mList[adapterPosition].desc)
                intent.putExtra("title",mList[adapterPosition].question)
                it.context.startActivity(intent)
            }
            viewDataBinding.executePendingBindings()
        }
    }
}