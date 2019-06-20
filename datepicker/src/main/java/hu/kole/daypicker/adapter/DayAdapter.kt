package hu.kole.daypicker.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.ViewTreeObserver
import hu.kole.daypicker.callbacks.OnDaySelectedListener
import hu.kole.daypicker.callbacks.OnMonthChangeListener
import hu.kole.daypicker.data.DayCell
import hu.kole.daypicker.extensions.isSameDayWith
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit

private const val NONE = -1
abstract class DayAdapter<T: DayCell, VH: BaseDayViewHolder<T>>: ListAdapter<T, VH>(DayDiffCallback<T>()) {

    var parentRecyclerView: RecyclerView? = null
    private var parentViewWidth: Int = 0
    private val itemViewWidth: Int
        get() = getItemWidth()

    private var offset: Int = 0

    private var allPixels: Int = 0 // scrolled pixels
    val pixelOffset: Int
        get() {
            return allPixels
        }

    var daySelectedListener: OnDaySelectedListener? = null
    var monthSelectedListener: OnMonthChangeListener? = null

    var isScrolling = false
    var isAutoScroll = false
    private var _indicatorPosition: Int = 1
    val indicatorPosition: Int
        get() {
            return _indicatorPosition
        }
    var selectedItemPosition: Int = 1
        set(value) {
            field = value
            if (!isScrolling) {
                getItemAtOrNull(field).let {
                    selectedItem = it
                }
            }

            notifyMonthChangeIfNeeded(field)
            notifyDataSetChanged()
        }

    var selectedItem: T? = null
        set(value) {
            if (field == null || field?.date?.isSameDayWith(value?.date) == false) {
                value?.let { day ->
                    daySelectedListener?.onDaySelected(day)
                }
            }

            field = value
        }

    val selectedDate: Date?
        get() {
            return selectedItem?.date
        }

    private var lastVisibleDate: Date? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        parentRecyclerView = recyclerView

        recyclerView.itemAnimator = null
        recyclerView.viewTreeObserver.addOnPreDrawListener(object: ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                recyclerView.viewTreeObserver.removeOnPreDrawListener(this)

                parentViewWidth = recyclerView.measuredWidth
                offset = parentViewWidth.minus(itemViewWidth).div(2)

                allPixels = 0

                LinearSnapHelper().apply {
                    attachToRecyclerView(recyclerView)
                }

                createItemSelectionObservableFrom(recyclerView)
                    .debounce(200, TimeUnit.MILLISECONDS)
                    .subscribe(ItemSelectionObserver(this@DayAdapter))

                recyclerView.post {
                    if (selectedItemPosition != NONE) {
                        recyclerView.scrollToPosition(selectedItemPosition, this@DayAdapter)
                        notifyMonthChangeIfNeeded(selectedItemPosition)
                    }
                }

                return true
            }
        })
    }

    fun createItemSelectionObservableFrom(sourceView: RecyclerView): Observable<Int> {
        return Observable.create { emitter ->
            sourceView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    synchronized(this) {
                        when(newState) {
                            RecyclerView.SCROLL_STATE_IDLE -> {
                                isScrolling = false
                                isAutoScroll = false
                                emitter.onNext(_indicatorPosition)
                            }
                            else -> {
                                isScrolling = true
                            }
                        }
                    }

                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    allPixels = allPixels.plus(dx)

                    sourceView.post {
                        calculateSelectedIndex()
                    }
                }
            })
        }
    }

    private fun calculateSelectedIndex() {
        val expectedPositionDate: Int = Math.round(allPixels.toDouble().div(itemViewWidth)).toInt()

        val previousIndex = _indicatorPosition
        _indicatorPosition = expectedPositionDate.plus(1)

        notifyItemChanged(previousIndex)
        notifyItemChanged(_indicatorPosition)
    }

    fun selectDate(date: Date?, recyclerView: RecyclerView) {
        getDatePosition(date)?.let { pos ->
            //initialDate = date
            recyclerView.scrollToPosition(pos, this)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): VH {
        return onCreateViewHolder(viewGroup.context, viewGroup, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, getItem(position), position)
    }

    abstract fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH
    abstract fun onBindViewHolder(holder: VH, item: T?, position: Int)

    abstract fun getItemWidth(): Int

    fun invalidate() {
        allPixels = 0
        _indicatorPosition = 0
        selectedItemPosition = 0
    }

    fun getItemAtOrNull(position: Int): T? {
        return try {
            getItem(position)
        } catch (e: Exception) {
            null
        }
    }

    private fun getDatePosition(date: Date?): Int? {
        if (date == null) return null

        for (pos in 0 until itemCount) {
            if (getItem(pos).date.isSameDayWith(date)) {
                return pos
            }
        }

        return null
    }

    fun scrollToPosition(position: Int, smooth: Boolean) {
        parentRecyclerView?.post {
            parentRecyclerView?.scrollToPosition(position, this, smooth)
        }
    }

    private fun notifyMonthChangeIfNeeded(position: Int) {
        getItemAtOrNull(position)?.let { item ->
            val date = item.date

            val currentCalendar = Calendar.getInstance()
            currentCalendar.time = date

            val lastCalendar = Calendar.getInstance()
            if (lastVisibleDate != null) {
                lastCalendar.time = lastVisibleDate
            }

            if (monthSelectedListener != null && (lastVisibleDate == null || lastCalendar.get(Calendar.MONTH) != currentCalendar.get(Calendar.MONTH))) {
                monthSelectedListener?.onMonthChanged(item)
                lastVisibleDate = date
            }
        }
    }
}

fun <T: DayCell, VH: BaseDayViewHolder<T>>RecyclerView.scrollToPosition(position: Int, adapter: DayAdapter<T, VH>, isSmooth: Boolean = false) {
    var targetPosition = 0

    if (position > 0) {
        targetPosition = position.minus(1)
    }

    val targetScrollPosDate: Int = targetPosition.times(adapter.getItemWidth())
    val missingPxDate: Int = (targetScrollPosDate - adapter.pixelOffset)

    if (missingPxDate != 0) {
        if (isSmooth) {
            smoothScrollBy(missingPxDate, 0)
        } else {
            scrollBy(missingPxDate, 0)
        }
    }
}

class ItemSelectionObserver<T: DayCell, VH: BaseDayViewHolder<T>>(private var adapter: DayAdapter<T, VH>): Observer<Int> {
    override fun onComplete() {
    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onNext(selectedIndex: Int) {
        Handler(Looper.getMainLooper()).post {
            adapter.selectedItemPosition = selectedIndex
        }
    }

    override fun onError(e: Throwable) {
    }
}