package hu.kole.daypicker.callbacks

import hu.kole.daypicker.data.DayCell

interface OnMonthChangeListener {
    fun onMonthChanged(month: DayCell)
}