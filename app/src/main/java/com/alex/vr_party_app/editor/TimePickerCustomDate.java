package com.alex.vr_party_app.editor;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TimePickerCustomDate extends TimePickerDialog {

    private TimePicker mTimePicker;
    private final OnTimeSetListener mTimeSetListener;
    private final static int TIME_PICKER_INTERVAL = 60;

    public TimePickerCustomDate(Context context, OnTimeSetListener listener,
                                int hourOfDay, int minute, boolean is24HourView) {
        super(context, TimePickerDialog.THEME_HOLO_DARK, null, hourOfDay,
                minute / TIME_PICKER_INTERVAL, is24HourView);
        mTimeSetListener = listener;
    }
//TimePickerDialog.THEME_HOLO_LIGHT
//R.style.DialogTheme
    @Override
    public void updateTime(int hourOfDay, int minuteOfHour) {
        mTimePicker.setCurrentHour(hourOfDay);
        mTimePicker.setCurrentMinute(minuteOfHour / TIME_PICKER_INTERVAL);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                if (mTimeSetListener != null) {
                    mTimeSetListener.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
                            mTimePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
                }
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");
            Field timePickerField = classForid.getField("timePicker");
            mTimePicker = (TimePicker) findViewById(timePickerField.getInt(null));
            Field field = classForid.getField("minute");

            NumberPicker minuteSpinner = (NumberPicker) mTimePicker
                    .findViewById(field.getInt(null));
            minuteSpinner.setMinValue(0);
            minuteSpinner.setMaxValue(2);

            List<String> displayedValues = new ArrayList<>();

//
//            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
//                displayedValues.add(String.format("%02d", i));
//            }
            displayedValues.add(String.format("%02d", 00));
            displayedValues.add(String.format("%02d", 00));
            displayedValues.add(String.format("%02d", 00));

            minuteSpinner.setDisplayedValues(displayedValues
                    .toArray(new String[displayedValues.size()]));

            ////////////////////////////

            Class<?> classForidHours = Class.forName("com.android.internal.R$id");
            Field timePickerFieldHours = classForidHours.getField("timePicker");
            mTimePicker = (TimePicker) findViewById(timePickerFieldHours.getInt(null));
            Field fieldHours = classForidHours.getField("hour");
            NumberPicker minuteSpinnerHour = (NumberPicker) mTimePicker
                    .findViewById(fieldHours.getInt(null));
            minuteSpinnerHour.setMinValue(0);
            minuteSpinnerHour.setMaxValue(31);

            List<String> displayedValuesHours = new ArrayList<>();

            for (int i = 0; i < 32; i += 1) {
                displayedValuesHours.add(String.format("%02d", i));
            }

            minuteSpinnerHour.setDisplayedValues(displayedValuesHours
                    .toArray(new String[displayedValuesHours.size()]));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
