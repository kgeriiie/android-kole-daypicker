# KoleDayPicker

Simple library for picking a date between a given intervall. You can select day by swiping, tapping on selector. You can customize all essential view on picker. Basically the picker use a default implementation for display dates.

![How does it look like](https://github.com/kgeriiie/KoleDayPicker/blob/master/promo-res/day_demo.gif)

# Usage

##### Put into the layout resource file
Include the day cells container in your layout. You can customize (size, color, font) the title of container.

```xml
        <hu.kole.daypicker.view.KoleDayPickerView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"/>
```
##### Implement DayAdapter
After implementation, setup data source and add it to *KoleDayPickerView*. You can use default impelemntation or implement your own solution.

###### DefaultDayAdapter
Its a ListAdapter based class. Waiting for *DayCell* inherited data sources as input. You can create this input list by yourself or with the method below.
 - *****KoleDayPickerView.getInitDayCells(start_date, end_date)*****
It will return an intervall of *DayCell* list between **start_date** and **end_date**.
###### DefaultViewHolder
Its a simple setup of cells and positions.
###### DefaultPaddingViewHolder
This view will fill the space at the begining and end of the list. It will display the same cell as we use in the list.

##### Setup callbacks
You can set Day selection chaned listener. ***setOnDaySelectedListener***