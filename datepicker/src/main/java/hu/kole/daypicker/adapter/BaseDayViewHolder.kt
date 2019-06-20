package hu.kole.daypicker.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import hu.kole.daypicker.data.DayCell

abstract class BaseDayViewHolder<T: DayCell>(v: View): RecyclerView.ViewHolder(v) {
    abstract fun setup(cellData: T)
}