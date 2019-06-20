package hu.kole.daypicker.default

import android.view.View
import hu.kole.daypicker.R
import hu.kole.daypicker.adapter.BaseDayViewHolder
import hu.kole.daypicker.adapter.DayAdapter
import hu.kole.daypicker.data.DayCell
import hu.kole.daypicker.databinding.ViewDayPickerItemBinding
import hu.kole.daypicker.extensions.isSameDayWith
import hu.kole.daypicker.extensions.setStyle
import java.util.*

class DefaultViewHolder(private val binding: ViewDayPickerItemBinding, private val adapter: DayAdapter<DayCell, BaseDayViewHolder<DayCell>>?): BaseDayViewHolder<DayCell>(binding.root) {
    override fun setup(cellData: DayCell) {
        binding.cell = cellData

        when {
            adapterPosition == adapter?.indicatorPosition -> {
                setSelectedStyle()
            }
            cellData.date.isSameDayWith(Date()) -> {
                setCurrentDateStyle()
            }
            else -> {
                setNormalStyle()
            }
        }

        binding.executePendingBindings()

        binding.root.setOnClickListener {
            adapter?.scrollToPosition(adapterPosition, true)
        }
    }

    private fun setSelectedStyle() {
        binding.calendarDayIndicator.visibility = View.VISIBLE
        binding.calendarCurrentDayIndicator.visibility = View.GONE

        binding.calendarDayNumberTv.setStyle(R.style.DateNumberSelectedStyle)
        binding.calendarDayNameTv.setStyle(R.style.DateNameSelectedStyle)
    }

    private fun setNormalStyle() {
        binding.calendarDayIndicator.visibility = View.GONE
        binding.calendarCurrentDayIndicator.visibility = View.GONE
        binding.calendarDayNumberTv.setStyle(R.style.DateNumberNormalTextStyle)
        binding.calendarDayNameTv.setStyle(R.style.DateNameNormalTextStyle)
    }

    private fun setCurrentDateStyle() {
        binding.calendarDayIndicator.visibility = View.GONE
        binding.calendarCurrentDayIndicator.visibility = View.VISIBLE
        binding.calendarDayNumberTv.setStyle(R.style.DateNumberSelectedTextStyle)
        binding.calendarDayNameTv.setStyle(R.style.DateNameSelectedTextStyle)
    }
}