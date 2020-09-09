package com.dimfcompany.akcslclient.base.extensions

import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.SystemClock
import android.text.Html
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.dimfcompany.akcslclient.base.AppClass

fun AlertDialog.makeTransparentBg()
{
    this.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
}

fun TextView.setTextHtml(text: String)
{
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
    {
        this.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY))
    }
    else
    {
        this.setText(Html.fromHtml(text))
    }
}

fun EditText.getNullableText(): String?
{
    val text = this.text.toString().trim()
    if (TextUtils.isEmpty(text))
    {
        return null
    }

    return text
}

fun EditText.acceptIfNotMatches(text: String?)
{
    val current_et_text = this.getNullableText()

    if (current_et_text == null && text == null)
    {
        return
    }

    if (current_et_text.equals(text))
    {
        return
    }

    if (this.text.toString().equals(text))
    {
        return
    }

    this.setText(text)
}

fun Dialog.setNavigationBarColor(color: Int)
{
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    {
        val window = this.window ?: return
        val metrics = DisplayMetrics()
        window.getWindowManager().getDefaultDisplay().getMetrics(metrics)

        val dimDrawable = GradientDrawable()

        val navigationBarDrawable = GradientDrawable()
        navigationBarDrawable.shape = GradientDrawable.RECTANGLE
        navigationBarDrawable.setColor(color)

        val layers = arrayOf<Drawable>(dimDrawable, navigationBarDrawable)

        val windowBackground = LayerDrawable(layers)

        windowBackground.setLayerInsetTop(1, metrics.heightPixels)

        window.setBackgroundDrawable(windowBackground)
    }
}

fun Boolean.toVisibility(): Int
{
    if (this)
    {
        return View.VISIBLE
    }

    return View.GONE
}

fun EditText.addDoneAction(action: () -> Unit)
{
    this.imeOptions = EditorInfo.IME_ACTION_DONE
    this.setOnEditorActionListener(
        { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE)
            {
                action()
                true
            }
            else
            {
                false
            }
        })
}


fun RecyclerView.setDivider(color: Int, size: Int, orientation: Int = DividerItemDecoration.VERTICAL)
{
    val drw = GradientDrawable()
    drw.shape = GradientDrawable.RECTANGLE

    val itemDecorator: DividerItemDecoration
    if (orientation == DividerItemDecoration.VERTICAL)
    {
        drw.setSize(0, size)
        itemDecorator = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
    }
    else
    {
        drw.setSize(size, 0)
        itemDecorator = DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL)
    }

    drw.setColor(color)

    itemDecorator.setDrawable(drw)
    this.addItemDecoration(itemDecorator)
}

fun View.setHeightMy(height_to_set: Int)
{
    this.getLayoutParams().height = height_to_set
    this.requestLayout()
}

fun View.setWidthMy(width_to_set: Int)
{
    this.getLayoutParams().width = width_to_set
    this.requestLayout()
}

fun View.simulateSwipeLeft()
{
    val downTime = SystemClock.uptimeMillis()
    val x = 500.0f
    val y = 0.0f
    val metaState = 0

    val event_down = MotionEvent.obtain(
        downTime,
        downTime + 10,
        MotionEvent.ACTION_DOWN,
        x,
        y,
        metaState
    )

    val event_move = MotionEvent.obtain(
        downTime + 10,
        downTime + 100,
        MotionEvent.ACTION_MOVE,
        x - 250,
        y,
        metaState
    )

    val event_up = MotionEvent.obtain(
        downTime + 110,
        downTime + 120,
        MotionEvent.ACTION_UP,
        x - 250,
        y,
        metaState
    )

    this.dispatchTouchEvent(event_down)
    this.dispatchTouchEvent(event_move)
    this.dispatchTouchEvent(event_up)
}

fun getScreenWidth(): Int
{
    val displayMetrics = DisplayMetrics()
    val window_manager = AppClass.app.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    window_manager.getDefaultDisplay().getMetrics(displayMetrics)
    val width = displayMetrics.widthPixels
    return width
}

fun getScreenHeight(): Int
{
    val displayMetrics = DisplayMetrics()
    val window_manager = AppClass.app.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    window_manager.getDefaultDisplay().getMetrics(displayMetrics)
    val height = displayMetrics.heightPixels
    return height
}

fun View.isVisibleOnScreen(): Boolean
{
    if (!this.isShown())
    {
        return false
    }
    val actualPosition = Rect()
    this.getGlobalVisibleRect(actualPosition)
    val screen = Rect(0, 0, getScreenWidth(), getScreenHeight())
    return actualPosition.intersect(screen)
}

fun View.simulateSwipeRight()
{
    val downTime = SystemClock.uptimeMillis()
    val x = 0.0f
    val y = 0.0f
    val metaState = 0

    val event_down = MotionEvent.obtain(
        downTime,
        downTime + 10,
        MotionEvent.ACTION_DOWN,
        x,
        y,
        metaState
    )

    val event_move = MotionEvent.obtain(
        downTime + 10,
        downTime + 100,
        MotionEvent.ACTION_MOVE,
        x + 250,
        y,
        metaState
    )

    val event_up = MotionEvent.obtain(
        downTime + 110,
        downTime + 120,
        MotionEvent.ACTION_UP,
        x + 250,
        y,
        metaState
    )

    this.dispatchTouchEvent(event_down)
    this.dispatchTouchEvent(event_move)
    this.dispatchTouchEvent(event_up)
}

