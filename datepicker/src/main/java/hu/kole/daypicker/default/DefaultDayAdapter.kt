package hu.kole.daypicker.default

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import hu.kole.daypicker.R
import hu.kole.daypicker.adapter.BaseDayViewHolder
import hu.kole.daypicker.adapter.DayAdapter
import hu.kole.daypicker.data.CellType
import hu.kole.daypicker.data.DayCell

class DefaultDayAdapter(private val context: Context): DayAdapter<DayCell, BaseDayViewHolder<DayCell>>() {

    override fun getItemViewType(position: Int): Int {
        getItemAtOrNull(position)?.let {
            return it.type.ordinal
        }

        return CellType.NORMAL_DAY.ordinal
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): BaseDayViewHolder<DayCell> {
        return if (viewType == CellType.PADDING.ordinal) {
            DefaultPaddingViewHolder(DefaultPlaceholderView(context, R.layout.view_day_picker_item, getItemWidth()))
        } else {
            DefaultViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_day_picker_item, parent, false), this)
        }
    }

    override fun onBindViewHolder(holder: BaseDayViewHolder<DayCell>, item: DayCell?, position: Int) {
        item?.let {
            holder.setup(it)
        }
    }

    override fun getItemWidth(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.default_day_view_width)
    }
}