package com.alohagoha.developerslife

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alohagoha.developerslife.databinding.ActivityMainBinding
import com.alohagoha.developerslife.model.entities.GifCategory
import com.alohagoha.developerslife.ui.adapters.CategoryCardFragmentAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        val categoryList = listOf(GifCategory.LATEST, GifCategory.TOP, GifCategory.HOT)
        setSupportActionBar(binding.appToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.apply {
            categoryPager.adapter = CategoryCardFragmentAdapter(this@MainActivity, categoryList)
            TabLayoutMediator(categoryTabs, categoryPager) { tab: TabLayout.Tab, i: Int ->
                tab.text = getString(categoryList[i].stringId)
            }.attach()
        }
    }
}
