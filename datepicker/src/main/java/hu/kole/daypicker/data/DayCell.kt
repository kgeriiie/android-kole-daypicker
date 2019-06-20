package hu.kole.daypicker.data

import java.text.SimpleDateFormat
import java.util.*

open class DayCell(var date: Date) {
    var type: CellType = CellType.NORMAL_DAY
    var direction: CellDirection = CellDirection.NONE

    open fun getDefaultNameOfDay(locale: Locale = Locale.ENGLISH): String {
        return SimpleDateFormat("EEEE", locale).format(date)
    }

    open fun getDefaultNameOfMonth(locale: Locale = Locale.ENGLISH): String {
        return SimpleDateFormat("MMM", locale).format(date)
    }

    open fun getDefaultNumberOfDay(locale: Locale = Locale.ENGLISH): String {
        val cal = Calendar.getInstance()
        cal.time = date

        val day = String.format(locale, "%d", cal.get(Calendar.DAY_OF_MONTH))

        return if (day.length > 1) day else String.format("0%s", day)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DayCell

        if (date != other.date) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = date.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }

    companion object {

        fun firstPlaceholder(date: Date = Date()): DayCell {
            val label = DayCell(date)
            label.type = CellType.PADDING
            label.direction = CellDirection.BEFORE

            return label
        }

        fun lastPlaceholder(date: Date = Date()): DayCell {
            val label = DayCell(date)
            label.type = CellType.PADDING
            label.direction = CellDirection.AFTER

            return label
        }
    }
}