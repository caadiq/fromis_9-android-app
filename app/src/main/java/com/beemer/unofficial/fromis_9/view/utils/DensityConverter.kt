package com.beemer.unofficial.fromis_9.view.utils

import android.content.Context
import kotlin.math.roundToInt

object DensityConverter {
    fun dpToPx(context: Context, dp: Float): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).roundToInt()
    }
}