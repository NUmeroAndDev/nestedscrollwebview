package com.github.numeroanddev.nestedscrollwebview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.webkit.WebView
import android.widget.OverScroller
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
    private val scroller: OverScroller = OverScroller(context)

    private var lastFlingY: Int = 0
    private var lastMotionY: Float = 0f
    private var nestedYOffset: Float = 0f
    private var remainderY: Float = 0f
    private var totalScrollOffset: Float = 0f

    private var velocityTracker: VelocityTracker? = null
    private var minimumVelocity = 0
    private var maximumVelocity = 0

    init {
        isNestedScrollingEnabled = true
        val configuration = ViewConfiguration.get(context)
        minimumVelocity = configuration.scaledMinimumFlingVelocity
        maximumVelocity = configuration.scaledMaximumFlingVelocity
    }

    @android.annotation.SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain()
        }
        var result: Boolean
        val trackedEvent = MotionEvent.obtain(event)
        val action = event.actionMasked

        if (action == MotionEvent.ACTION_DOWN) {
            nestedYOffset = 0f
            totalScrollOffset = 0f
            remainderY = 0f
            scrollOffset[0] = 0
            scrollOffset[1] = 0
            scrollConsumed[0] = 0
            scrollConsumed[1] = 0
            scroller.abortAnimation()
            stopNestedScroll(ViewCompat.TYPE_NON_TOUCH)
        }

        val y = event.y
        trackedEvent.offsetLocation(0f, nestedYOffset)

        val velocityTrackerEvent = MotionEvent.obtain(event)
        velocityTrackerEvent.offsetLocation(0f, totalScrollOffset)

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
                lastMotionY = y
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_TOUCH)
                result = super.onTouchEvent(trackedEvent)
                velocityTracker?.addMovement(velocityTrackerEvent)
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
                    totalScrollOffset += scrollOffset[1].toFloat()
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
                totalScrollOffset += scrollOffset[1].toFloat()
                trackedEvent.offsetLocation(0f, scrollOffset[1].toFloat())
                velocityTrackerEvent.offsetLocation(0f, scrollOffset[1].toFloat())
                velocityTracker?.addMovement(velocityTrackerEvent)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                velocityTracker?.addMovement(velocityTrackerEvent)
                velocityTracker?.computeCurrentVelocity(1000, maximumVelocity.toFloat())
                var yVelocity = -(velocityTracker?.yVelocity ?: 0f)

                if (abs(yVelocity) < minimumVelocity) {
                    yVelocity = 0f
                }

                val consumed = dispatchNestedPreFling(0f, yVelocity)

                if (yVelocity != 0f) {
                    if (!consumed) {
                        dispatchNestedFling(0f, yVelocity, true)
                        fling(yVelocity.toInt())
                    }
                    if (action == MotionEvent.ACTION_UP) {
                        trackedEvent.action = MotionEvent.ACTION_CANCEL
                    }
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

        velocityTrackerEvent.recycle()
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

    private fun fling(velocityY: Int) {
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH)
        scroller.fling(
            scrollX, scrollY,
            0, velocityY,
            0, 0,
            Int.MIN_VALUE, Int.MAX_VALUE,
            0, 0
        )
        lastFlingY = scrollY
        postInvalidateOnAnimation()
    }

    override fun computeScroll() {
        super.computeScroll()
        if (scroller.computeScrollOffset()) {
            val y = scroller.currY
            var unconsumed = y - lastFlingY
            lastFlingY = y

            scrollConsumed[0] = 0
            scrollConsumed[1] = 0
            if (dispatchNestedPreScroll(
                    0,
                    unconsumed,
                    scrollConsumed,
                    null,
                    ViewCompat.TYPE_NON_TOUCH
                )
            ) {
                unconsumed -= scrollConsumed[1]
            }

            val oldScrollY = scrollY
            val maxScrollY =
                (computeVerticalScrollRange() - computeVerticalScrollExtent()).coerceAtLeast(0)
            val targetScrollY = (oldScrollY + unconsumed).coerceIn(0, maxScrollY)
            val scrolledByMe = targetScrollY - oldScrollY

            scrollTo(scrollX, targetScrollY)
            unconsumed -= scrolledByMe

            scrollConsumed[0] = 0
            scrollConsumed[1] = 0
            dispatchNestedScroll(
                0, scrolledByMe,
                0, unconsumed,
                null,
                ViewCompat.TYPE_NON_TOUCH,
                scrollConsumed
            )

            val unconsumedAfterParent = unconsumed - scrollConsumed[1]
            if (unconsumedAfterParent != 0) {
                scroller.abortAnimation()
                stopNestedScroll(ViewCompat.TYPE_NON_TOUCH)
            }

            postInvalidateOnAnimation()
        } else {
            if (hasNestedScrollingParent(ViewCompat.TYPE_NON_TOUCH)) {
                stopNestedScroll(ViewCompat.TYPE_NON_TOUCH)
            }
        }
    }
}