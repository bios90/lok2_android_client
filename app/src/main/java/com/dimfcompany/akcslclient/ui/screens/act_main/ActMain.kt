package com.dimfcompany.akcslclient.ui.screens.act_main

import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.dimfcompany.akcsl.base.adapters.AdapterVpUniversal
import com.dimfcompany.akcsl.ui.screens.act_main.VmActMain
import com.dimfcompany.akcsl.ui.screens.act_main.tabs.tab_categories.TabCategories
import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.BaseActivity
import com.dimfcompany.akcslclient.base.Constants
import com.dimfcompany.akcslclient.base.enums.TypeTab
import com.dimfcompany.akcslclient.base.extensions.*
import com.dimfcompany.akcslclient.databinding.ActMainBinding
import com.dimfcompany.akcslclient.logic.utils.MyPush
import com.dimfcompany.akcslclient.ui.screens.act_main.tabs.tab_favorite.TabFavorite
import com.dimfcompany.akcslclient.ui.screens.act_main.tabs.tab_profile.TabProfile
import com.dimfcompany.akcslclient.ui.screens.act_main.tabs.tab_search.TabSearch

class ActMain : BaseActivity()
{
    lateinit var bnd_act_main: ActMainBinding
    lateinit var vm_act_main: VmActMain
    private val adapter_vp = AdapterVpUniversal()

    private lateinit var tab_categories: TabCategories
    private lateinit var tab_favorite: TabFavorite
    private lateinit var tab_search: TabSearch
    private lateinit var tab_profile: TabProfile

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        getActivityComponent().inject(this)
        bnd_act_main = DataBindingUtil.setContentView(this, R.layout.act_main)
        vm_act_main = my_vm_factory.getViewModel(VmActMain::class.java)
        setBaseVmActions(vm_act_main)

        setViewPager()
        setEvents()
        setListeners()
        bnd_act_main.viewFakeStatus.setHeightMy(getStatusBarHeight())
    }

    fun setNavStatus()
    {
        is_full_screen = true
        is_below_nav_bar = false
        color_status_bar = getColorMy(R.color.transparent)
        color_nav_bar = getColorMy(R.color.white)
        is_light_status_bar = false
        is_light_nav_bar = false

    }

    private fun setViewPager()
    {
        bnd_act_main.vpMain.adapter = adapter_vp
        bnd_act_main.vpMain.offscreenPageLimit = 3

        tab_categories = TabCategories(this)
        tab_favorite = TabFavorite(this)
        tab_search = TabSearch(this)
        tab_profile = TabProfile(this)

        val views = arrayListOf(
            tab_categories.bnd_tab_categories.root,
            tab_favorite.bnd_tab_favorite.root,
            tab_search.bnd_tab_search.root,
            tab_profile.bnd_tab_profile.root)

        adapter_vp.setViews(views)
    }

    private fun setEvents()
    {
        vm_act_main.bs_scroll_to_tab
                .mainThreaded()
                .subscribe(
                    {
                        changeColorsOfTabs(it)
                        bnd_act_main.vpMain.setCurrentItem(it.getPos(), true)
                    })
                .disposeBy(composite_disposable)
    }

    private fun setListeners()
    {
        bnd_act_main.laBottomNav.larCategories.setOnClickListener(
            {
                vm_act_main.ViewListener().clickedTab(TypeTab.CATEGORIES)
            })

        bnd_act_main.laBottomNav.larFavorite.setOnClickListener(
            {
                vm_act_main.ViewListener().clickedTab(TypeTab.FAVORITE)
            })

        bnd_act_main.laBottomNav.larSearch.setOnClickListener(
            {
                vm_act_main.ViewListener().clickedTab(TypeTab.SEARCH)
            })

        bnd_act_main.laBottomNav.larProfile.setOnClickListener(
            {
                vm_act_main.ViewListener().clickedTab(TypeTab.PROFILE)
            })
    }

    private fun changeColorsOfTabs(tab: TypeTab)
    {
        val lar_bottom_nav = bnd_act_main.laBottomNav.root as LinearLayout
        val tabs_count = lar_bottom_nav.childCount
        val color_red = getColorMy(R.color.red)
        val color_gray_5 = getColorMy(R.color.gray5)

        for (i in 0 until tabs_count)
        {
            val lal = lar_bottom_nav.getChildAt(i) as ViewGroup
            val tv_faw = lal.getChildAt(0) as TextView
            val tv_text = lal.getChildAt(1) as TextView

            if (tab.getPos() == i)
            {
                tv_faw.setTextColor(color_red)
                tv_text.setTextColor(color_red)
            }
            else
            {
                tv_faw.setTextColor(color_gray_5)
                tv_text.setTextColor(color_gray_5)
            }
        }
    }
}