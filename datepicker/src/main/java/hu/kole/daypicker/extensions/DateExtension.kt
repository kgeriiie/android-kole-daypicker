package hu.kole.daypicker.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.getNextDay(): Date {
    return getNextDay(1)
}

fun Date.getNextDay(dayCount: Int): Date {
    val currentCalendar = Calendar.getInstance()
    currentCalendar.time = this
    currentCalendar.add(Calendar.DAY_OF_YEAR, dayCount)
    currentCalendar.set(Calendar.HOUR_OF_DAY,1)
    currentCalendar.set(Calendar.MINUTE, 0)
    currentCalendar.set(Calendar.SECOND, 0)

    return currentCalendar.time
}

fun Date.getPreviousDay(): Date {
    return getPreviousDay(1)
}

fun Date.getPreviousDay(dayCount: Int): Date {
    val currentCalendar = Calendar.getInstance()
    currentCalendar.time = this
    currentCalendar.add(Calendar.DAY_OF_YEAR, -dayCount)
    currentCalendar.set(Calendar.HOUR_OF_DAY,1)
    currentCalendar.set(Calendar.MINUTE, 0)
    currentCalendar.set(Calendar.SECOND, 0)

    return currentCalendar.time
}

fun Date.getFirstDayOfCurrentMonth(): Date {
    val currentCalendar = Calendar.getInstance()
    currentCalendar.time = this
    currentCalendar.set(Calendar.DAY_OF_MONTH, 1)
    currentCalendar.set(Calendar.HOUR_OF_DAY,1)
    currentCalendar.set(Calendar.MINUTE, 0)
    currentCalendar.set(Calendar.SECOND, 0)

    return currentCalendar.time
}

fun Date.getFirstDayOfNextMonth(offset: Int): Date {
    val currentCalendar = Calendar.getInstance()
    currentCalendar.time = this
    currentCalendar.add(Calendar.MONTH, offset)
    currentCalendar.set(Calendar.DAY_OF_MONTH, 1)
    currentCalendar.set(Calendar.HOUR_OF_DAY,1)
    currentCalendar.set(Calendar.MINUTE, 0)
    currentCalendar.set(Calendar.SECOND, 0)

    return currentCalendar.time
}

fun Date.getFirstDayOfPreviousMonth(offset: Int): Date {
    val currentCalendar = Calendar.getInstance()
    currentCalendar.time = this
    currentCalendar.add(Calendar.MONTH, -offset)
    currentCalendar.set(Calendar.DAY_OF_MONTH, 1)
    currentCalendar.set(Calendar.HOUR_OF_DAY,1)
    currentCalendar.set(Calendar.MINUTE, 0)
    currentCalendar.set(Calendar.SECOND, 0)

    return currentCalendar.time
}

fun Date.getLastDayOfNextMonth(): Date {
    return this.getLastDayOfNextMonth(1)
}

fun Date.getLastDayOfNextMonth(offset: Int): Date {
    return this.getFirstDayOfNextMonth(offset.plus(1)).getPreviousDay()
}

fun Date.getLastDayOfPreviousMonth(): Date {
    return this.getFirstDayOfCurrentMonth().getPreviousDay()
}

fun Date.getFirstDayOfNextMonth(): Date {
    return getFirstDayOfNextMonth(1)
}

fun Date.getFirstDayOfPreviousMonth(): Date {
    return getFirstDayOfPreviousMonth(1)
}

fun Date.isSameDayWith(date: Date?): Boolean {
    if (date == null) return false

    val fmt = SimpleDateFormat("yyyyMMdd")
    return fmt.format(this) == fmt.format(date)
}

fun Date.isSameMonthWith(date: Date): Boolean {
    val fmt = SimpleDateFormat("yyyyMM")
    return fmt.format(this) == fmt.format(date)
}
