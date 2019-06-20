package hu.kole.daypicker.default

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import hu.kole.daypicker.view.PlaceholderView
import hu.kole.daypicker.R
import hu.kole.daypicker.adapter.BaseDayViewHolder
import hu.kole.daypicker.data.DayCell
import hu.kole.daypicker.extensions.setStyle
import java.util.*

class DefaultPaddingViewHolder(private val view: DefaultPlaceholderView): BaseDayViewHolder<DayCell>(view) {
    override fun setup(cellData: DayCell) {
        view.setData(cellData)
    }
}

class DefaultPlaceholderView(context: Context, res: Int, itemWidth: Int) : PlaceholderView(context, res, itemWidth) {
    override fun onAttachCell(day: DayCell, view: View) {
        view.findViewById<TextView>(R.id.calendar_day_nameTv).apply {
            text = day.getDefaultNameOfDay(Locale.ENGLISH)
            setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark))
            setStyle(R.style.DateNamePastTextStyle)
        }
        view.findViewById<TextView>(R.id.calendar_day_numberTv).apply {
            text = day.getDefaultNumberOfDay(Locale.ENGLISH)
            setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark))
            setStyle(R.style.DateNumberPastTextStyle)
        }
    }
}