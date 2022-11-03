package com.aajeevika.individual.view.suvidha.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.aajeevika.individual.databinding.ListItemSuvidhaViewPagerBinding

class SuvidhaViewPager(
    private val layoutInflater: LayoutInflater,
    private var dataList: ArrayList<String>
) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as View
    }

    override fun getCount() = dataList.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val viewDataBinding = ListItemSuvidhaViewPagerBinding.inflate(layoutInflater, container, false)
        viewDataBinding.image = BaseUrls.baseUrl + dataList[position]

        container.addView(viewDataBinding.root)
        return viewDataBinding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
