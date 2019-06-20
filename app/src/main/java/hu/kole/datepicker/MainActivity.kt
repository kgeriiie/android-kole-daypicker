package hu.kole.datepicker

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import hu.kole.datepicker.databinding.ActivityMainBinding
import hu.kole.daypicker.view.DayPickerView
import hu.kole.daypicker.callbacks.OnDaySelectedListener
import hu.kole.daypicker.data.DayCell
import hu.kole.daypicker.default.DefaultDayAdapter
import hu.kole.daypicker.extensions.getNextDay
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var adapter = DefaultDayAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        adapter.submitList(DayPickerView.getInitDateCells(Date(), Date().getNextDay(DayPickerView.OFFSET_FORWARD)))

        binding.picker.adapter = adapter

        //binding.picker.adapter?.selectedItemPosition = 2

        binding.picker.setOnDateSelectedListener(object: OnDaySelectedListener {
            override fun onDaySelected(selected: DayCell) {
                //Log.d("test--","selected: ${selected.date}")
            }
        })
    }
}
