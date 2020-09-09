package com.justordercompany.barista.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.extensions.*
import com.dimfcompany.akcslclient.databinding.DialogBottomSheetRoundedBinding
import com.dimfcompany.akcslclient.databinding.LaBottomSheetBtnBinding
import com.dimfcompany.akcslclient.logic.utils.builders.BuilderBg
import com.dimfcompany.barista.logic.builders.BuilderDialogBottom
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class DialogBottomSheetRounded(
        val builder: BuilderDialogBottom
) : BottomSheetDialogFragment()
{
    lateinit var bnd_dialog: DialogBottomSheetRoundedBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        bnd_dialog = DataBindingUtil.inflate(inflater, R.layout.dialog_bottom_sheet_rounded, container, false)

        val bg = BuilderBg()
                .setBgColor(getColorMy(R.color.white))
                .setRippleColor(getColorMy(R.color.red_trans_50))
                .isRipple(true)
                .isDpMode(true)
                .setCornerRadiuses(12f, 12f, 0f, 0f)
                .get()

        bnd_dialog.root.background = bg

        return bnd_dialog.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(
            {
                val dialog = it as BottomSheetDialog;
                val bottomSheet: FrameLayout = dialog.findViewById(R.id.design_bottom_sheet) ?: return@setOnShowListener
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED)
                BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(true)
                BottomSheetBehavior.from(bottomSheet).setHideable(true)
            })

        return bottomSheetDialog;
    }

    override fun onStart()
    {
        super.onStart()

        dialog?.setNavigationBarColor(getColorMy(R.color.white))


        if (builder.title == null && builder.text == null)
        {
            bnd_dialog.lalTop.visibility = View.GONE
            bnd_dialog.laBtns.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        }
        else
        {
            bnd_dialog.tvTitle.text = builder.title
            bnd_dialog.tvTitle.visibility = (builder.title != null).toVisibility()
            bnd_dialog.tvText.text = builder.text
            bnd_dialog.tvText.visibility = (builder.text != null).toVisibility()

            bnd_dialog.lalTop.visibility = View.VISIBLE
            bnd_dialog.laBtns.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE or LinearLayout.SHOW_DIVIDER_BEGINNING
        }

        builder.btns.forEachIndexed(
            { index, btn_action ->

                val lar_btn: LaBottomSheetBtnBinding = DataBindingUtil.inflate(layoutInflater, R.layout.la_bottom_sheet_btn, bnd_dialog.laBtns, false)

                lar_btn.tvText.text = btn_action.text
                lar_btn.tvFaw.text = btn_action.faw_text

                val bg = BuilderBg()
                        .setBgColor(getColorMy(R.color.transparent))
                        .setRippleColor(getColorMy(R.color.red_trans_50))
                        .isRipple(true)
                        .isDpMode(true)

                if (index == 0 && builder.title == null && builder.text == null)
                {
                    bg.setCornerRadiuses(12f, 12f, 0f, 0f)
                }

                lar_btn.root.background = bg.get()
                lar_btn.root.setOnClickListener(
                    {
                        dialog?.dismiss()
                        btn_action.action?.invoke()
                    })

                bnd_dialog.laBtns.addView(lar_btn.root)
            })


        if (builder.show_cancel)
        {
            val lar_btn: LaBottomSheetBtnBinding = DataBindingUtil.inflate(layoutInflater, R.layout.la_bottom_sheet_btn, bnd_dialog.laBtns, false)
            lar_btn.tvText.text = getStringMy(R.string.cancel)

            val bg = BuilderBg()
                    .setBgColor(getColorMy(R.color.white).applyTransparency(80))
                    .setRippleColor(getColorMy(R.color.red_trans_50))
                    .isRipple(true)
                    .setCorners(4f)
                    .isDpMode(true)
                    .get()

            lar_btn.tvText.background = bg

            lar_btn.tvText.setOnClickListener(
                {
                    dialog?.dismiss()
                })

            bnd_dialog.laBtns.addView(lar_btn.root)
        }
    }
}