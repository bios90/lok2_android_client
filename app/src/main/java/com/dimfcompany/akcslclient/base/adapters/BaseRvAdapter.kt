package com.dimfcompany.akcslclient.base.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dimfcompany.akcsl.base.FeedDisplayInfo
import com.dimfcompany.akcsl.base.LoadBehavior
import com.dimfcompany.akcsl.logic.models.ModelCategory
import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.ObjectWithId
import com.dimfcompany.akcslclient.base.diff.BaseDiff
import com.dimfcompany.akcslclient.base.extensions.getStringMy
import com.dimfcompany.akcslclient.base.getPosOfObject
import com.dimfcompany.akcslclient.databinding.ItemCategoryBinding
import com.dimfcompany.akcslclient.logic.utils.formatToString
import com.rucode.autopass.logic.utils.images.GlideManager

class BaseRvAdapter<B : ViewDataBinding, M : ObjectWithId, L : BaseCardListener<B, M>>
    (val layout_id: Int,
     val card_class: Class<out BaseCard<B, M, L>>,
     val diff_class: Class<out BaseDiff<M>>,
     val bnd_class: Class<B>,
     val listener: L?)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    var items: ArrayList<M> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        val inflater = LayoutInflater.from(parent.context)
        val bnd: B = DataBindingUtil.inflate(inflater, layout_id, parent, false)

        val card_new = card_class.getDeclaredConstructor(bnd_class).newInstance(bnd)
        return card_new
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
        val item = items.get(position)
        (holder as BaseCard<B, M, L>).bindItem(item, listener)
    }

    fun updateItemWithId(id: Long)
    {
        val pos = items.getPosOfObject(id) ?: return
        notifyItemChanged(pos)
    }

    override fun getItemCount(): Int
    {
        return items.size
    }

    fun setItems(rec_info: FeedDisplayInfo<M>)
    {
        if (rec_info.load_behavior == LoadBehavior.FULL_RELOAD)
        {
            this.items = ArrayList(rec_info.items)
            notifyDataSetChanged()
            return
        }

        val diff_callback = diff_class.getDeclaredConstructor(List::class.java, List::class.java).newInstance(rec_info.items, items)
        val diff_result = DiffUtil.calculateDiff(diff_callback)
        diff_result.dispatchUpdatesTo(this)
        this.items = ArrayList(rec_info.items)
    }
}

abstract class BaseCard<B : ViewDataBinding, M : ObjectWithId, L : BaseCardListener<B, M>>(val bnd: B) : RecyclerView.ViewHolder(bnd.root)
{
    open fun bindItem(item: M, listener: L?)
    {
        bnd.root.setOnClickListener(
            {
                listener?.clickedCard(item, bnd)
            })
//        if(listener == null)
//        {
//            Log.e("BaseCard", "Listenererer nullll")
//        }
//        Log.e("BaseCard", "bindItem: Bindinga base caed")
//        if (listener != null)
//        {
//            Log.e("BaseCard", "bindItem: hrrhrhrh")
//            bnd.root.setOnClickListener(
//                {
//                    Log.e("BaseCard", "bindItem: 22222")
//                    listener.clickedCard(item, bnd)
//                })
//        }

    }
}

interface BaseCardListener<B : ViewDataBinding, M : ObjectWithId>
{
    fun clickedCard(item: M, bnd: B)
}
