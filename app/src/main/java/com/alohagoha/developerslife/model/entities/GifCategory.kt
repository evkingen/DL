package com.alohagoha.developerslife.model.entities

import androidx.annotation.StringRes
import com.alohagoha.developerslife.R

enum class GifCategory(@StringRes val stringId: Int, val key: String) {
    LATEST(R.string.latest_tab_name, "latest"),
    TOP(R.string.top_tab_name, "top"),
    HOT(R.string.hot_tab_name, "hot")
}
