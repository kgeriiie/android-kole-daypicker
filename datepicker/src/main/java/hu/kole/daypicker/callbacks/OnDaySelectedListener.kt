package hu.kole.daypicker.callbacks

import hu.kole.daypicker.data.DayCell

interface OnDaySelectedListener {
    fun onDaySelected(selected: DayCell)
}