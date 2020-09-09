package com.dimfcompany.akcslclient.logic.utils.builders

import android.content.res.ColorStateList
import android.graphics.drawable.*
import android.view.View
import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.extensions.dp2px
import com.dimfcompany.akcslclient.base.extensions.getColorMy
import java.lang.RuntimeException

class BuilderBg
{
    private var is_dp_mode: Boolean = false
    private var view: View? = null
    private var stroke_width: Float = 0f
    private var stroke_color: Int = getColorMy(R.color.transparent)
    private var bg_color: Int = getColorMy(R.color.transparent)
    private var corner_radius: Float = 0f
    private var corners_radius: ArrayList<Float>? = null
    private var is_ripple: Boolean = false
    private var ripple_color: Int = getColorMy(R.color.red_trans_50)
    private var is_gradient: Boolean = false
    private var grad_colors: List<Int> = arrayListOf()
    private var grad_orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.LEFT_RIGHT

    fun setView(view: View): BuilderBg
    {
        this.view = view
        return this
    }

    fun setStrokeWidth(stroke_width: Float): BuilderBg
    {
        this.stroke_width = stroke_width
        return this
    }

    fun setStrokeColor(color: Int): BuilderBg
    {
        this.stroke_color = color
        return this
    }

    fun setBgColor(color: Int): BuilderBg
    {
        this.bg_color = color
        return this
    }

    fun setCorners(radius: Float): BuilderBg
    {
        this.corner_radius = radius
        return this
    }

    fun isRipple(is_ripple: Boolean): BuilderBg
    {
        this.is_ripple = is_ripple
        return this
    }

    fun setRippleColor(color: Int): BuilderBg
    {
        this.ripple_color = color
        return this
    }

    fun isDpMode(is_dp_mode: Boolean): BuilderBg
    {
        this.is_dp_mode = is_dp_mode
        return this
    }

    fun isGradient(is_gradient: Boolean): BuilderBg
    {
        this.is_gradient = is_gradient
        return this
    }

    fun setGradColors(colors: List<Int>): BuilderBg
    {
        this.grad_colors = colors
        return this
    }

    fun setGradOrientation(orientation: GradientDrawable.Orientation): BuilderBg
    {
        this.grad_orientation = orientation
        return this
    }

    fun setCornerRadiuses(left_top: Float, right_top: Float, right_bottom: Float, left_bottom: Float): BuilderBg
    {
        this.corners_radius = arrayListOf(left_top, right_top, right_bottom, left_bottom)
        return this
    }

    fun get(): Drawable
    {
        if (is_dp_mode)
        {
            remakeValuesForDpMode()
        }

        val drawable = GradientDrawable()
        drawable.setColor(bg_color)
        drawable.setStroke(stroke_width.toInt(), stroke_color)
        drawable.cornerRadius = corner_radius

        corners_radius?.let(
            {
                drawable.cornerRadii = floatArrayOf(it.get(0), it.get(0), it.get(1), it.get(1), it.get(2), it.get(2), it.get(3), it.get(3))
            })

        if (is_gradient)
        {
            drawable.setColors(grad_colors.toIntArray())
            drawable.orientation = grad_orientation
        }

        if (is_ripple)
        {
            var corners = FloatArray(8, { corner_radius })
            corners_radius?.let(
                {
                    corners = floatArrayOf(it.get(0), it.get(0), it.get(1), it.get(1), it.get(2), it.get(2), it.get(3), it.get(3))
                })

            val drawable_ripple = getRippleDrawable(ripple_color, ripple_color, drawable, corners, true)
            return drawable_ripple
        }

        return drawable
    }

    fun applyToView()
    {
        if (view == null)
        {
            throw RuntimeException("**** Error view not setted ****")
        }

        view!!.background = get()
    }

    private fun remakeValuesForDpMode()
    {
        stroke_width = dp2px(stroke_width)
        corner_radius = dp2px(corner_radius)
        corners_radius?.let(
            {
                val left_top = dp2px(it.get(0))
                val right_top = dp2px(it.get(1))
                val right_bottom = dp2px(it.get(2))
                val left_bottom = dp2px(it.get(3))

                corners_radius = arrayListOf(left_top, right_top, right_bottom, left_bottom)
            })
    }

    companion object
    {
        //Special function for getting color in xml databinding
        @JvmStatic
        fun gc(color_res_id: Int): Int
        {
            return getColorMy(color_res_id)
        }

        @JvmStatic
        fun getSimpleDrawable(radius: Float, color: Int): Drawable
        {
            return BuilderBg()
                    .setBgColor(color)
                    .setCorners(radius)
                    .isDpMode(true)
                    .get()
        }

        @JvmStatic
        fun getSimpleDrawableRipple(radius: Float, color: Int, color_ripple: Int): Drawable
        {
            return BuilderBg()
                    .setBgColor(color)
                    .setCorners(radius)
                    .isDpMode(true)
                    .isRipple(true)
                    .setRippleColor(color_ripple)
                    .get()
        }

        @JvmStatic
        fun getSimpleDrawableStrokedRipple(radius: Float, color: Int, color_ripple: Int, color_stroke: Int, stroke_width: Float): Drawable
        {
            return BuilderBg()
                    .setBgColor(color)
                    .setCorners(radius)
                    .setStrokeWidth(stroke_width)
                    .setStrokeColor(color_stroke)
                    .isDpMode(true)
                    .isRipple(true)
                    .setRippleColor(color_ripple)
                    .get()
        }

        @JvmStatic
        fun getSimpleEmptyRipple(radius: Float, color: Int): Drawable
        {
            return BuilderBg()
                    .isDpMode(true)
                    .setStrokeWidth(1f)
                    .setStrokeColor(color)
                    .setCorners(radius)
                    .setBgColor(getColorMy(R.color.transparent))
                    .isRipple(true)
                    .setRippleColor(color)
                    .get()
        }

        @JvmStatic
        fun getSquareRippleTransWhite(): Drawable
        {
            return BuilderBg()
                    .isDpMode(true)
                    .setBgColor(getColorMy(R.color.transparent))
                    .isRipple(true)
                    .setRippleColor(getColorMy(R.color.white_trans_50))
                    .get()
        }


        @JvmStatic
        fun getStrokedEt(): Drawable
        {
            return BuilderBg()
                    .setCorners(4f)
                    .isDpMode(true)
                    .setBgColor(getColorMy(R.color.white))
                    .isRipple(false)
                    .setStrokeColor(getColorMy(R.color.gray4))
                    .setStrokeWidth(1f)
                    .get()
        }

        @JvmStatic
        fun getSquareRippleTransRed(): Drawable
        {
            return BuilderBg()
                    .isDpMode(true)
                    .setBgColor(getColorMy(R.color.transparent))
                    .isRipple(true)
                    .setRippleColor(getColorMy(R.color.red_trans_50))
                    .get()
        }

        @JvmStatic
        fun getRounded4White(): Drawable
        {
            return BuilderBg()
                    .setBgColor(getColorMy(R.color.white))
                    .setCorners(4f)
                    .isDpMode(true)
                    .isRipple(true)
                    .setRippleColor(getColorMy(R.color.gray2))
                    .get()
        }
    }
}

fun getStateColorList(color_normal: Int, color_pressed: Int): ColorStateList
{
    return ColorStateList(
        arrayOf(
            intArrayOf(android.R.attr.state_pressed),
            intArrayOf(android.R.attr.state_focused),
            intArrayOf(android.R.attr.state_activated),
            intArrayOf())
        , intArrayOf(
            color_pressed,
            color_pressed,
            color_pressed,
            color_normal)
    )
}

fun getRippleDrawable(color_normal: Int, color_pressed: Int, inner_drawable: Drawable, corner_radius: FloatArray, is_trans: Boolean): RippleDrawable
{
    val drawbale_mask = GradientDrawable()
    drawbale_mask.setColor(color_normal)
    drawbale_mask.cornerRadii = corner_radius
    (drawbale_mask.mutate() as GradientDrawable).cornerRadii = corner_radius

    val colors_list: ColorStateList = getStateColorList(color_normal, color_pressed)
    return RippleDrawable(colors_list, inner_drawable, drawbale_mask)
}