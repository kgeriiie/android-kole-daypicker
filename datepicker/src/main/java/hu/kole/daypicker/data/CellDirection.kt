package hu.kole.daypicker.data

import android.widget.LinearLayout

enum class CellDirection(val direction: Int) {
    BEFORE(LinearLayout.LAYOUT_DIRECTION_RTL),
    AFTER(LinearLayout.LAYOUT_DIRECTION_LTR),
    NONE(0)
}