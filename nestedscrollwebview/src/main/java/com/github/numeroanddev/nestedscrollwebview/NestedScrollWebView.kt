package com.github.numeroanddev.nestedscrollwebview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.WebView
import androidx.core.view.NestedScrollingChild3
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat
import kotlin.math.abs

public open class NestedScrollWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.webViewStyle,
) : WebView(context, attrs, defStyleAttr), NestedScrollingChild3 {

    private val childHelper: NestedScrollingChildHelper = NestedScrollingChildHelper(this)
    private val scrollOffset = IntArray(2)
    private val scrollConsumed = IntArray(2)

    private var lastMotionY: Float = 0f
    private var nestedYOffset: Float = 0f
    private var remainderY: Float = 0f

    private var velocityTracker: android.view.VelocityTracker? = null
    private var minimumVelocity = 0
    private var maximumVelocity = 0

    init {
        isNestedScrollingEnabled = true
        val configuration = android.view.ViewConfiguration.get(context)
        minimumVelocity = configuration.scaledMinimumFlingVelocity
        maximumVelocity = configuration.scaledMaximumFlingVelocity
    }

    @android.annotation.SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (velocityTracker == null) {
            velocityTracker = android.view.VelocityTracker.obtain()
        }
        var result: Boolean
        val trackedEvent = MotionEvent.obtain(event)
        val action = event.actionMasked

        if (action == MotionEvent.ACTION_DOWN) {
            nestedYOffset = 0f
            remainderY = 0f
        }

        val y = event.y
        trackedEvent.offsetLocation(0f, nestedYOffset)

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
                lastMotionY = y
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_TOUCH)
                result = super.onTouchEvent(trackedEvent)
                velocityTracker?.addMovement(trackedEvent)
            }

            MotionEvent.ACTION_MOVE -> {
                val deltaY = lastMotionY - y + remainderY
                var deltaYInt = deltaY.toInt()
                remainderY = deltaY - deltaYInt

                if (
                    dispatchNestedPreScroll(
                        0,
                        deltaYInt,
                        scrollConsumed,
                        scrollOffset,
                        ViewCompat.TYPE_TOUCH
                    )
                ) {
                    deltaYInt -= scrollConsumed[1]
                    trackedEvent.offsetLocation(0f, scrollConsumed[1].toFloat())
                    nestedYOffset += scrollConsumed[1].toFloat() + scrollOffset[1].toFloat()
                }

                lastMotionY = y - scrollOffset[1]

                val oldScrollY = scrollY
                result = super.onTouchEvent(trackedEvent)

                val scrolledY = scrollY - oldScrollY
                val unconsumedY = deltaYInt - scrolledY

                val actualUnconsumedY = if (unconsumedY > 0 && canScrollVertically(1)) {
                    0
                } else if (unconsumedY < 0 && canScrollVertically(-1)) {
                    0
                } else {
                    unconsumedY
                }

                dispatchNestedScroll(
                    0, scrolledY,
                    0, actualUnconsumedY,
                    scrollOffset,
                    ViewCompat.TYPE_TOUCH,
                    scrollConsumed
                )

                lastMotionY -= scrollOffset[1]
                nestedYOffset += scrollOffset[1].toFloat()
                trackedEvent.offsetLocation(0f, scrollOffset[1].toFloat())

                velocityTracker?.addMovement(trackedEvent)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                velocityTracker?.addMovement(trackedEvent)
                velocityTracker?.computeCurrentVelocity(1000, maximumVelocity.toFloat())
                var yVelocity = -(velocityTracker?.yVelocity ?: 0f)

                if (abs(yVelocity) < minimumVelocity) {
                    yVelocity = 0f
                }

                val consumed = dispatchNestedPreFling(0f, yVelocity)
                if (!consumed) {
                    dispatchNestedFling(0f, yVelocity, true)
                }

                if (consumed && action == MotionEvent.ACTION_UP) {
                    trackedEvent.action = MotionEvent.ACTION_CANCEL
                }

                result = super.onTouchEvent(trackedEvent)
                stopNestedScroll(ViewCompat.TYPE_TOUCH)

                velocityTracker?.recycle()
                velocityTracker = null
            }

            else -> {
                result = super.onTouchEvent(trackedEvent)
            }
        }

        trackedEvent.recycle()
        return result
    }

    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean, clampedY: Boolean) {
        if (clampedX || clampedY) {
            parent.requestDisallowInterceptTouchEvent(false)
        }
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY)
    }

    override fun setNestedScrollingEnabled(enabled: Boolean) {
        childHelper.isNestedScrollingEnabled = enabled
    }

    override fun isNestedScrollingEnabled(): Boolean {
        return childHelper.isNestedScrollingEnabled
    }

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        return childHelper.startNestedScroll(axes, type)
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return startNestedScroll(axes, ViewCompat.TYPE_TOUCH)
    }

    override fun stopNestedScroll(type: Int) {
        childHelper.stopNestedScroll(type)
    }

    override fun stopNestedScroll() {
        stopNestedScroll(ViewCompat.TYPE_TOUCH)
    }

    override fun hasNestedScrollingParent(type: Int): Boolean {
        return childHelper.hasNestedScrollingParent(type)
    }

    override fun hasNestedScrollingParent(): Boolean {
        return hasNestedScrollingParent(ViewCompat.TYPE_TOUCH)
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int, dyConsumed: Int,
        dxUnconsumed: Int, dyUnconsumed: Int,
        offsetInWindow: IntArray?, type: Int, consumed: IntArray
    ) {
        childHelper.dispatchNestedScroll(
            dxConsumed, dyConsumed,
            dxUnconsumed, dyUnconsumed,
            offsetInWindow, type, consumed
        )
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int, dyConsumed: Int,
        dxUnconsumed: Int, dyUnconsumed: Int,
        offsetInWindow: IntArray?, type: Int
    ): Boolean {
        return childHelper.dispatchNestedScroll(
            dxConsumed, dyConsumed,
            dxUnconsumed, dyUnconsumed,
            offsetInWindow, type
        )
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int, dyConsumed: Int,
        dxUnconsumed: Int, dyUnconsumed: Int,
        offsetInWindow: IntArray?
    ): Boolean {
        return dispatchNestedScroll(
            dxConsumed, dyConsumed,
            dxUnconsumed, dyUnconsumed,
            offsetInWindow, ViewCompat.TYPE_TOUCH
        )
    }

    override fun dispatchNestedPreScroll(
        dx: Int, dy: Int,
        consumed: IntArray?, offsetInWindow: IntArray?, type: Int
    ): Boolean {
        return childHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)
    }

    override fun dispatchNestedPreScroll(
        dx: Int, dy: Int,
        consumed: IntArray?, offsetInWindow: IntArray?
    ): Boolean {
        return dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, ViewCompat.TYPE_TOUCH)
    }

    override fun dispatchNestedFling(
        velocityX: Float, velocityY: Float, consumed: Boolean
    ): Boolean {
        return childHelper.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return childHelper.dispatchNestedPreFling(velocityX, velocityY)
    }
}