package hu.kole.daypicker.view
import android.content.Context
import android.content.res.Resources
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import hu.kole.daypicker.R
import hu.kole.daypicker.Utils
import hu.kole.daypicker.adapter.BaseDayViewHolder
import hu.kole.daypicker.adapter.DayAdapter
import hu.kole.daypicker.callbacks.OnDaySelectedListener
import hu.kole.daypicker.callbacks.OnMonthChangeListener
import hu.kole.daypicker.data.DayCell
import hu.kole.daypicker.extensions.getPreviousDay
import hu.kole.daypicker.extensions.isSameDayWith
import kotlinx.android.synthetic.main.view_day_picker.view.*
import java.text.SimpleDateFormat
import java.util.*

fun List<Date>.toDateCellList(): List<DayCell> {
    val result: MutableList<DayCell> = mutableListOf()

    this.forEach { date ->
        result.add(DayCell(date))
    }

    return result
}

class KoleDayPickerView<T: DayCell, VH: BaseDayViewHolder<T>>: FrameLayout {
    constructor(context: Context): this(context, null, -1)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, -1)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr){
        attrs?.let {
            handleAttributeSet(it)
        }
    }

    private val defaultTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14f, context.resources.displayMetrics)
    private val defaultTextColor = ContextCompat.getColor(context, android.R.color.black)

    var adapter: DayAdapter<T, VH>? = null
        set(value) {
            field = value

            field?.let {
                it.monthSelectedListener = object: OnMonthChangeListener {
                    override fun onMonthChanged(month: DayCell) {
                        day_picker_month.text = SimpleDateFormat("yyyy. MMMM",
                            Utils.getDefaultLocale(context)
                        ).format(month.date)
                    }
                }

                day_picker_Rv.adapter = it
            }
        }

    private fun handleAttributeSet(attrs: AttributeSet) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.KoleDayPickerView, 0, 0)

        day_picker_month.setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimension(R.styleable.KoleDayPickerView_titleSize, defaultTextSize))

        try {
            day_picker_month.typeface = ResourcesCompat.getFont(context, a.getResourceId(R.styleable.KoleDayPickerView_titleFont, 0))
        } catch (e: Resources.NotFoundException) {
        }

        day_picker_month.setTextColor(a.getColor(R.styleable.KoleDayPickerView_titleColor, defaultTextColor))

        a.recycle()
    }

    companion object {
        const val OFFSET_BACK = 6
        const val OFFSET_FORWARD = 6

        fun getDateListBetween(start: Date, end: Date): List<Date> {
            val dateList = mutableListOf<Date>()

            val startCalendar = Calendar.getInstance()
            startCalendar.time = start

            val endCalendar = Calendar.getInstance()
            endCalendar.time = end

            while (endCalendar.timeInMillis >= startCalendar.timeInMillis) {
                dateList.add(startCalendar.time)
                startCalendar.add(Calendar.DAY_OF_YEAR, 1)
            }

            return dateList
        }

        fun getInitDayCells(start: Date, end: Date): List<DayCell> {
            val dateList = getDateListBetween(start, end).toDateCellList().toMutableList()

            dateList.add(0, DayCell.firstPlaceholder(start.getPreviousDay()))
            dateList.add(DayCell.lastPlaceholder(end))

            return dateList
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_day_picker,this,true)
    }

    fun invalidatePicker() {
        if (adapter?.getItemAtOrNull(0)?.date?.isSameDayWith(Date()) == false) {
            adapter?.invalidate()
        } else {
            adapter?.notifyDataSetChanged()
        }
    }

    fun getSelectedDate(): Date? {
        return adapter?.selectedDate
    }

    fun setOnDaySelectedListener(listener: OnDaySelectedListener?) {
        adapter?.daySelectedListener = listener
    }
}
