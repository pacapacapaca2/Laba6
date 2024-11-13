package com.example.laba6;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

public class ReminderActivity extends Activity {

    private EditText titleEditText, messageEditText;
    private Button selectDateTimeButton, saveButton;
    private long reminderTimeInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        titleEditText = findViewById(R.id.titleEditText);
        messageEditText = findViewById(R.id.messageEditText);
        selectDateTimeButton = findViewById(R.id.selectDateTimeButton);
        saveButton = findViewById(R.id.saveButton);

        selectDateTimeButton.setOnClickListener(v -> showDateTimePicker());

        saveButton.setOnClickListener(v -> saveReminder());
    }

    private void showDateTimePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            TimePickerDialog timePicker = new TimePickerDialog(this, (timeView, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                reminderTimeInMillis = calendar.getTimeInMillis();

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timePicker.show();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    private void saveReminder() {
        String title = titleEditText.getText().toString();
        String message = messageEditText.getText().toString();

        ReminderDatabaseHelper dbHelper = new ReminderDatabaseHelper(this);
        dbHelper.addReminder(title, message, reminderTimeInMillis);

        // Установка будильника
        setAlarm(title, message, reminderTimeInMillis);

        finish();  // Закрыть активность после сохранения
    }

    @SuppressLint("ScheduleExactAlarm")
    private void setAlarm(String title, String message, long timeInMillis) {
        // Проверка для Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }

        Intent intent = new Intent(this, ReminderBroadcastReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }
    }


}
