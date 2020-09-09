package com.dimfcompany.akcsl.base.adapters

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class AdapterVpUniversal() : PagerAdapter()
{
    private var views: ArrayList<View> = ArrayList()

    fun setViews(views: ArrayList<View>)
    {
        this.views = views
        notifyDataSetChanged()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any
    {
        val view = views.get(position)
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean
    {
        return view == obj
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any)
    {
        container.removeView(obj as View)
    }

    override fun getCount(): Int
    {
        return views.count()
    }
}