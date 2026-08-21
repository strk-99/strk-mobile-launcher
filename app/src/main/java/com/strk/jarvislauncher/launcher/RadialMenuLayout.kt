package com.strk.jarvislauncher.launcher

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/**
 * Lays out one center "reactor" child plus N ring children evenly spaced
 * around it, starting at 12 o'clock and going clockwise — the JARVIS-style
 * radial home screen. Convention: the FIRST child added is always the
 * center button; every child after that is a ring item.
 *
 * Deliberately a plain ViewGroup (not a Canvas-drawn view) so each child
 * keeps normal click handling, ripple, and accessibility for free.
 */
class RadialMenuLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {

    private val ringEdgeInsetPx = (RING_EDGE_INSET_DP * resources.displayMetrics.density).toInt()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
            MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount == 0) return

        val width = r - l
        val height = b - t
        val cx = width / 2
        val cy = height / 2

        layoutChildCentered(getChildAt(0), cx, cy)

        val ringChildren = (1 until childCount).map { getChildAt(it) }
        if (ringChildren.isEmpty()) return

        val ringItemSize = ringChildren.maxOf { max(it.measuredWidth, it.measuredHeight) }
        val maxRadius = min(width, height) / 2 - ringItemSize / 2 - ringEdgeInsetPx
        val radius = max(maxRadius, ringItemSize)

        val angleStep = 360f / ringChildren.size
        ringChildren.forEachIndexed { index, child ->
            val angleRad = Math.toRadians((-90f + index * angleStep).toDouble())
            val x = cx + (radius * cos(angleRad)).toInt()
            val y = cy + (radius * sin(angleRad)).toInt()
            layoutChildCentered(child, x, y)
        }
    }

    private fun layoutChildCentered(child: View, cx: Int, cy: Int) {
        val halfW = child.measuredWidth / 2
        val halfH = child.measuredHeight / 2
        child.layout(cx - halfW, cy - halfH, cx + halfW, cy + halfH)
    }

    companion object {
        private const val RING_EDGE_INSET_DP = 24
    }
}
