package com.dimfcompany.akcslclient.base.enums

import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.dimfcompany.akcslclient.R

enum class TypeActivityAnim
{
    FADE;

    fun animateWithActivity(activity: AppCompatActivity)
    {
        when (this)
        {
            FADE ->
            {
                activity.overridePendingTransition(R.anim.anim_fade_out, R.anim.anim_fade_in)
            }
        }
    }
}