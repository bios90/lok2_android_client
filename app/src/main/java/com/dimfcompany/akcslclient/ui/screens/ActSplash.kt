package com.dimfcompany.akcslclient.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.BaseActivity
import com.dimfcompany.akcslclient.base.extensions.getColorMy
import com.dimfcompany.akcslclient.base.extensions.runActionWithDelay
import com.dimfcompany.akcslclient.logic.SharedPrefsManager
import com.dimfcompany.akcslclient.ui.screens.act_first.ActFirst
import com.dimfcompany.akcslclient.ui.screens.act_main.ActMain
import kotlinx.android.synthetic.main.act_splash.*

class ActSplash : BaseActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_splash)

        Glide.with(img_splash).load(R.drawable.img_splash).into(img_splash)

        runActionWithDelay(lifecycleScope, 3000,
            {
                val intent: Intent
                if (SharedPrefsManager.pref_current_user.get().value != null)
                {
                    intent = Intent(this, ActMain::class.java)
                }
                else
                {
                    intent = Intent(this, ActFirst::class.java)
                }

                startActivity(intent)
                finish()
            })
    }

    fun setNavStatus()
    {
        is_full_screen = true
        is_below_nav_bar = true
        color_status_bar = getColorMy(R.color.transparent)
        color_nav_bar = getColorMy(R.color.transparent)
        is_light_status_bar = true
        is_light_nav_bar = true
    }
}