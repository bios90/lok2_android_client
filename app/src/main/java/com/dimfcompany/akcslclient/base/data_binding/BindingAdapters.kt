package com.dimfcompany.akcslclient.base.data_binding

import android.graphics.drawable.GradientDrawable
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import com.dimfcompany.akcslclient.base.extensions.dp2pxInt
import com.dimfcompany.akcslclient.base.extensions.getColorMy

@BindingAdapter(value = ["my_divider_size", "my_divider_color"], requireAll = true)
fun setDivider(lal: LinearLayout, my_divider_size: Float, my_divider_color: Int)
{
    val drw = GradientDrawable()
    drw.shape = GradientDrawable.RECTANGLE
    if (lal.orientation == LinearLayout.VERTICAL)
    {
        drw.setSize(0, dp2pxInt(my_divider_size))
    }
    else
    {
        drw.setSize(dp2pxInt(my_divider_size), 0)
    }
    drw.setColor(getColorMy(my_divider_color))
    lal.dividerDrawable = drw
}