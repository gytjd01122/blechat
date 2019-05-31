package com.hardcopy.blechat;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExerciseActivity extends AppCompatActivity {

   // @Override
    ArrayList<Event> eventlist = new ArrayList<>();

    CompactCalendarView compactCalendar;
    SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM-yyyy", Locale.getDefault());
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.KOREA);
    TextView myDate;
    TextView Month;

    Integer a = 2;  //if문 실행위해 임의로 만든 변수
    String mydate = "2019-05-24 09:00:00";  //날짜가 string값으로 들어있으면 date형으로 바꾸기 위한 string 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Month = (TextView)findViewById(R.id.yyyy_mm);
        myDate = (TextView)findViewById(R.id.myDate);
        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        /*
        DB에 입력된 거리 값이 0인 것들을 다 읽어오는 쿼리 작성
        while(받아온 값들을 하나씩 차례로 끝까지 다 읽어올 때 까지){
            날짜 값이 string형으로 되어있으면"yyyy-mm-dd"형식에서 "09:00:00"를 붙인 "yyyy-mm-dd HH:mm:ss"형식으로 변환
            "yyyy-MM-dd HH:mm:ss"형식의 string값을 date형으로 변환 후 mills로 변환
            event 생성 & ㅣist에 이벤트 넣기 }
        */

        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     //"yyyy-MM-dd HH:mm:ss"형식의 string값을 date형으로 변환
            Date date = sdf.parse(mydate);
            long millisecond = date.getTime();  //date형을 mills로 변환

            String str = String.valueOf(millisecond);   //mills로 변환된 값 화인 위한 변수
            myDate.setText(str);    //화면에 띄워서 mills값을 확인하기 위해 작성.

            while(eventlist.size()>=0){
                if(a!=0){
                    eventlist.add(new Event(Color.GREEN,millisecond,"exercise day"));
                    millisecond = millisecond + 259200000;      //3일 후 값 더하기(임의로 작성)
                    a--;
                }
                else if(a==0) break;
            }
        } catch (ParseException e){
            e.printStackTrace();;
        }


        for (Event e : eventlist)   //list에 들어가있는 이벤트들을 캘린더에 붙이기
        {
            compactCalendar.addEvent(e);
        }

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();
                myDate.setText(simpleDateFormat.format(dateClicked));
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Month.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });
    }
}