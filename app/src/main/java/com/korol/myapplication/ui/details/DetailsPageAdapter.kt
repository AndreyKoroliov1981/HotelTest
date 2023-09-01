package com.korol.myapplication.ui.details

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class DetailsPageAdapter(fm: Fragment)  : FragmentStateAdapter(fm) {
    private val mFragmentList: MutableList<Fragment> = ArrayList()
    private val mFragmentTitleList: MutableList<String> = ArrayList()

    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }

    fun getTabTitle(position : Int): String{
        return mFragmentTitleList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }
}