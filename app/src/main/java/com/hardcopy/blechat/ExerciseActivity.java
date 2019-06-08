package com.hardcopy.blechat;


import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.hardcopy.blechat.exerciseLogic.ExerciseLogic;

import android.content.Context;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class ExerciseActivity extends AppCompatActivity {


        /*주석뒤에 (..)표시된거는 DB가 있으면 없어도 되는 코드일거에요!*/

        ExerciseLogic logic;

        ArrayList<Event> eventlist = new ArrayList<>(); //점찍는 이벤트가 들어갈 list
        ArrayList<String> ExerciseDate = new ArrayList<>();  //DB에서 받아온 "yyyy-MM-dd"형식의 string에 " 09:00:00"을 더해서 "yyyy-MM-dd HH:mm:ss"형식으로 바뀐 값을 넣는 list
        ArrayList<Long> millisecond = new ArrayList<>();    //"yyyy-MM-dd HH;mm:ss"형식을 mills로 바꾼 값을 넣는 list

        List<String> exerciseDays;

        CompactCalendarView compactCalendar;
        SimpleDateFormat dateFormatMonth = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        TextView myDate;
        TextView Month;

        TextView distanceView;
        TextView timeView;
        TextView kcalView;
/*
        int a = 1;//if문 실행위해 임의로 만든 변수(..)
        String Edate = "2019-05-24";  //"Edate= DB에서 받아온 날짜 형식"
*/
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_exercise);

            Date today = new Date();
            logic = new ExerciseLogic(this, MainActivity.db, simpleDateFormat.format(today));

            Month = (TextView) findViewById(R.id.yyyy_mm);
            myDate = (TextView) findViewById(R.id.myDate);

            distanceView = (TextView) findViewById(R.id.Distance);
            timeView = (TextView) findViewById(R.id.Time);
            kcalView = (TextView) findViewById(R.id.Kcal);

            compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
            compactCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
            compactCalendar.setUseThreeLetterAbbreviation(true);





        /*
        DB에 입력된 거리 값이 0이 아닌 것들을 다 읽어오는 쿼리 필요
        읽어온 값의 날짜(string형 : "yyyy-MM-dd")에 " 09:00:00"를 더하고 "yyyy-mm-dd HH:mm:ss"형식으로 변환
        "yyyy-MM-dd HH:mm:ss"형식의 string값을 date형으로 변환 후 mills로 변환
        event 생성 & ㅣist에 이벤트 넣기
        calendar에 이벤트 붙이기
        */

        //DB에서 받아온 "yyyy-MM-dd"형식의 string에 " 09:00:00"을 더해서 "yyyy-MM-dd HH:mm:ss"형식으로 바꾸어서 list에 넣음.
        exerciseDays = MainActivity.db.gpsDao().getAllExerciseDay();
        Iterator<String> it =  exerciseDays.iterator();
        while (it.hasNext()) {
            String _str = it.next();
            ExerciseDate.add( _str +" 09:00:00");
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     //"yyyy-MM-dd HH:mm:ss"형식의 string값을 date형으로 변환
            for (String s : ExerciseDate) {
                Date date = sdf.parse(s);
                millisecond.add(date.getTime());    //date형을 mills로 변환해서 list에 넣기
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

/*
            //DB에서 받아온 "yyyy-MM-dd"형식의 string에 " 09:00:00"을 더해서 "yyyy-MM-dd HH:mm:ss"형식으로 바꾸어서 list에 넣음.
            while (ExerciseDate.size() >= 0) {//while(DB에 입력된 거리값이 0이 아닌 것들을 다 읽어올때까지)

                if (a != 0)    //DB값 대신 임의로 넣은 값을 하나씩 확인해보기위해 if문 사용.(..)
                {
                    ExerciseDate.add(Edate + " 09:00:00");
                    a--;
                } else if (a == 0) break;
            }


            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     //"yyyy-MM-dd HH:mm:ss"형식의 string값을 date형으로 변환
                for (String s : ExerciseDate) {
                    Date date = sdf.parse(s);
                    millisecond.add(date.getTime());    //date형을 mills로 변환해서 list에 넣기
                }
            } catch (ParseException e) {
                e.printStackTrace();
                ;
            }
*/
            //점찍는 이벤트 생성하고 list에 insert
            for (Long l : millisecond) {
                eventlist.add(new Event(Color.GREEN, l, "exercise day"));
            }

            //list에 들어가있는 이벤트들을 캘린더에 붙이기
            for (Event e : eventlist) {
                compactCalendar.addEvent(e);
            }

            compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
                @Override
                public void onDayClick(Date dateClicked) {
                    Context context = getApplicationContext();
                    myDate.setText(simpleDateFormat.format(dateClicked));

                    String _date = new SimpleDateFormat("yyyy-MM-dd").format(dateClicked);

                    if (!exerciseDays.contains(_date)) {
                        distanceView.setText("0");
                        timeView.setText("0");
                        kcalView.setText("0");

                        return;
                    }

                    distanceView.setText( String.format( "%.2f" , MainActivity.db.gpsDao().getSumDistanceByDate(_date)) );
                    timeView.setText(MainActivity.db.gpsDao().getSumTimeByDate(_date).toString());
                    kcalView.setText(logic.getKcal(_date).toString());
                }

                @Override
                public void onMonthScroll(Date firstDayOfNewMonth) {
                    Month.setText(dateFormatMonth.format(firstDayOfNewMonth));
                }
            });
        }
    }
