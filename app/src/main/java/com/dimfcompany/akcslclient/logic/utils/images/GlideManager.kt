package com.rucode.autopass.logic.utils.images

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.AppClass
import com.dimfcompany.akcslclient.base.extensions.dp2pxInt
import com.dimfcompany.akcslclient.base.extensions.getColorMy
import com.dimfcompany.akcslclient.base.extensions.isNetworkAvailable
import com.github.ybq.android.spinkit.SpinKitView

class GlideManager
{
    companion object
    {
        fun getProgressDrawable(): CircularProgressDrawable
        {
            val circularProgressDrawable = CircularProgressDrawable(AppClass.app)
            circularProgressDrawable.strokeWidth = dp2pxInt(3f).toFloat()
            circularProgressDrawable.centerRadius = dp2pxInt(18).toFloat()
            circularProgressDrawable.strokeCap = Paint.Cap.ROUND
            circularProgressDrawable.setColorSchemeColors(getColorMy(R.color.red))
            circularProgressDrawable.start()
            return circularProgressDrawable
        }

        fun loadImage(imageView: ImageView, url: String?, img_width: Int? = null, img_height: Int? = null,show_failed_images:Boolean = true)
        {
            var builder = Glide.with(imageView)
                    .load(url)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .placeholder(getProgressDrawable())


            builder = builder.addListener(object : RequestListener<Drawable>
            {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean
                {
                    if(show_failed_images)
                    {
                        if (!isNetworkAvailable())
                        {
                            imageView.setImageResource(R.drawable.img_no_internet)
                        }
                        else
                        {
                            imageView.setImageResource(R.drawable.drw_load_failed)
                        }
                    }

                    return true
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean
                {
                    return false
                }
            })

            if (img_width != null && img_height != null)
            {
                builder = builder.override(img_width, img_height)
            }

            builder.into(imageView)
        }
    }
}