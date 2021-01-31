package com.alohagoha.developerslife.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alohagoha.developerslife.model.entities.GifCategory
import com.alohagoha.developerslife.ui.GifCardFragment

class CategoryCardFragmentAdapter(activity: FragmentActivity, private val category: List<GifCategory>) :
        FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = category.size

    override fun createFragment(position: Int): Fragment =
            GifCardFragment.newInstance(category[position])

}
