package hu.kole.daypicker.adapter

import android.support.v7.util.DiffUtil
import hu.kole.daypicker.data.DayCell

class DayDiffCallback<T: DayCell>: DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldDate: T, newDate: T): Boolean {
        return oldDate == newDate
    }

    override fun areContentsTheSame(oldDate: T, newDate: T): Boolean {
        return oldDate == newDate
    }
}