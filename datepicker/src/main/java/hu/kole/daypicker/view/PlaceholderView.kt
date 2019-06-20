package hu.kole.daypicker.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import hu.kole.daypicker.Utils
import hu.kole.daypicker.data.CellDirection
import hu.kole.daypicker.data.DayCell
import hu.kole.daypicker.extensions.getNextDay
import hu.kole.daypicker.extensions.getPreviousDay

abstract class PlaceholderView: LinearLayout {
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, -1)
    constructor(context: Context) : this(context, null, -1)
    constructor(context: Context, res: Int, itemWidth: Int): this(context, null, -1) {
        itemResourceLayout = res
        orientation = HORIZONTAL

        this.itemWidth = itemWidth
        layoutParams = LayoutParams(Utils.getScreenWidth(context).minus(this.itemWidth).div(2), LayoutParams.WRAP_CONTENT)
    }

    private var itemWidth: Int = 0
    private var itemResourceLayout: Int = -1

    fun setData(data: DayCell) {
        if (tag != data.date) {
            tag = data.date
            removeAllViews()
        }

        layoutDirection = data.direction.direction

        if (childCount == 0 || childCount.times(itemWidth) <= Utils.getScreenWidth(context).div(2)) {
            attachDayCell(data)
        }
    }

    @Synchronized
    private fun attachDayCell(day: DayCell): View {
        val view = LayoutInflater.from(context).inflate(itemResourceLayout, this, false)

        val date = if (day.direction == CellDirection.BEFORE) {
            DayCell(day.date.getPreviousDay(childCount))
        } else {
            DayCell(day.date.getNextDay(childCount))
        }

        onAttachCell(date, view)

        addView(view)

        view.post {
            if (childCount.times(itemWidth) <= Utils.getScreenWidth(context).div(2)) {
                attachDayCell(day)
            }
        }

        return view
    }

    abstract fun onAttachCell(day: DayCell, view: View)
}