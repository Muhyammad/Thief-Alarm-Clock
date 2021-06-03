package gold.experience.myapplication;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import static gold.experience.myapplication.AlarmReceiver.mediaPlayer;

public class MainActivity extends AppCompatActivity {

    Button alarm_On;
    Button alarm_Off;
    TextView tvShowTime;
    TimePicker timepicker;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    Calendar calendar;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarm_On = findViewById(R.id.btnStart);
        alarm_Off = findViewById(R.id.btnStop);

        tvShowTime = findViewById(R.id.tvShowTime);

        timepicker = findViewById(R.id.editTextTime);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        timepicker.setIs24HourView(true);

        calendar = Calendar.getInstance();


        alarm_On.setOnClickListener(onClickListener);
        alarm_Off.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnStart:
                    Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                    int hour = timepicker.getHour();
                    int minute = timepicker.getMinute();

                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);

                    if(minute < 10)
                        tvShowTime.setText("Будильник поставлен на " + hour + ":0" + minute);
                    else
                        tvShowTime.setText("Будильник поставлен на " + hour + ":" + minute);

                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    break;
                case R.id.btnStop:
                    tvShowTime.setText("Будильник выключен");
                    mediaPlayer.stop();
                    break;
            }
        }
    };
}