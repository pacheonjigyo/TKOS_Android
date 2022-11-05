package com.example.tkos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ToggleButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

final public class SettingActivity extends AppCompatActivity {
    private Vibrator vibrator; // Vibrator 필드 선언

    int tkos_music; //Music 필드 선언
    int tkos_effect; //Effect 필드 선언
    int tkos_vibrate; //Vibrate 필드 선언

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting); //setting.xml 참조

        final DatabaseActivity dbactivity = new DatabaseActivity(getApplicationContext(), "TKOS.db", null, 1); //DB객체 생성
        SavedID sid = (SavedID) getApplicationContext(); //전역변수 참조
        final String name = sid.getID(); //DB ID값 받아오기

        //SoundPool  생성
        final SoundPool sound = new SoundPool(1, AudioManager.STREAM_ALARM, 0);// maxStreams, streamType, srcQuality
        final int soundId = sound.load(this, R.raw.coin, 0); //사운드 로드
        final int streamid = sound.play(soundId, 0.1F, 0.1F,  0,  0,  1.0F); //사운드 플레이(재생)

        tkos_music = dbactivity.getMusic(name); //DB의 Music값 받아오기
        tkos_effect = dbactivity.getEffect(name); //DB의 Effect값 받아오기
        tkos_vibrate = dbactivity.getVibrate(name); //DB의 Vibrate값 받아오기

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE); // Vibrator 객체생성

        final ToggleButton toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton);
        if(tkos_music == 1){ //앱 실행 시 배경음악이 켜져있을 때
            toggleButton1.setBackgroundColor(Color.GREEN); //버튼색 변경
            toggleButton1.setText("ON"); //버튼 글자 변경
        }
        else { //앱 실행 시 배경음악이 꺼져있을 때
            toggleButton1.setBackgroundColor(Color.RED); //버튼색 변경
            toggleButton1.setText("OFF"); //버튼 글자 변경
        }
        toggleButton1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tkos_music == 1){ //배경음악이 켜져있을 때
                    toggleButton1.setBackgroundColor(Color.RED); //버튼색 변경
                    toggleButton1.setText("OFF"); //버튼 글자 변경
                    dbactivity.setMusic(name, 0); //배경음악 꺼짐으로 변경(Database)
                    tkos_music = 0; //배경음악 꺼짐으로 변경
                    Intent intent = new Intent(getApplicationContext(), MusicService.class); // 인텐트 참조
                    stopService(intent); //서비스 중지
                    if(tkos_effect == 1) //효과음이 켜져있을 때
                        sound.play(soundId, 0.1F, 0.1F,  0,  0,  1.0F); //사운드 플레이(재생)
                }
                else if(tkos_music == 0){ //배경음악이 꺼져있을 때
                    toggleButton1.setBackgroundColor(Color.GREEN); //버튼색 변경
                    toggleButton1.setText("ON"); //버튼 글자 변경
                    dbactivity.setMusic(name, 1); //배경음악 켜짐으로 변경(Database)
                    tkos_music = 1; //배경음악 켜짐으로 변경
                    Intent intent = new Intent(getApplicationContext(), MusicService.class); // 인텐트 참조
                    startService(intent); //서비스 시작
                    if(tkos_effect == 1) //효과음이 켜져있을 때
                        sound.play(soundId, 0.1F, 0.1F,  0,  0,  1.0F); //사운드 플레이(재생)
                }
            }
        });

        final ToggleButton toggleButton2 = (ToggleButton) findViewById(R.id.toggleButton2);
        if(tkos_effect == 1){ //앱 실행 시 효과음이 켜져있을 때
            toggleButton2.setBackgroundColor(Color.GREEN); //버튼색 변경
            toggleButton2.setText("ON"); //버튼 글자 변경
        }
        else { //앱 실행 시 효과음이 꺼져있을 때
            toggleButton2.setBackgroundColor(Color.RED); //버튼색 변경
            toggleButton2.setText("OFF"); //버튼 글자 변경
        }
        toggleButton2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(tkos_effect == 1){ //효과음이 켜져있을 때
                    toggleButton2.setBackgroundColor(Color.RED); //버튼색 변경
                    toggleButton2.setText("OFF"); //버튼 글자 변경
                    dbactivity.setEffect(name, 0); //효과음 꺼짐으로 변경(Database)
                    tkos_effect = 0; //효과음 꺼짐으로 변경
                    sound.stop(streamid); //사운드 중지(stop)
                }
                else if(tkos_effect == 0){ //효과음이 꺼져있을 때
                    toggleButton2.setBackgroundColor(Color.GREEN); //버튼색 변경
                    toggleButton2.setText("ON"); //버튼 글자 변경
                    dbactivity.setEffect(name, 1); //효과음 켜짐으로 변경(Database)
                    tkos_effect = 1; //효과음 켜짐으로 변경
                    sound.play(soundId, 0.1F, 0.1F,  0,  0,  1.0F); //사운드 플레이(재생)
                }
            }
        });

        final ToggleButton toggleButton3 = (ToggleButton) findViewById(R.id.toggleButton3);
        if(tkos_vibrate == 1){ //앱 실행 시 진동이 켜져있을 때
            toggleButton3.setBackgroundColor(Color.GREEN); //버튼색 변경
            toggleButton3.setText("ON"); //버튼 글자 변경
        }
        else { //앱 실행 시 진동이 꺼져있을 때
            toggleButton3.setBackgroundColor(Color.RED); //버튼색 변경
            toggleButton3.setText("OFF"); //버튼 글자 변경
        }
        toggleButton3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(tkos_vibrate == 1){ //진동이 켜져있을 때
                    toggleButton3.setBackgroundColor(Color.RED); //버튼색 변경
                    toggleButton3.setText("OFF"); //버튼 글자 변경
                    dbactivity.setVibrate(name, 0); //진동 꺼짐으로 변경(Database)
                    tkos_vibrate = 0; //진동 꺼짐으로 변경
                    if(tkos_effect == 1) //효과음이 켜져있을 때
                        sound.play(soundId, 0.1F, 0.1F,  0,  0,  1.0F); //사운드 플레이(재생)
                }
                else if(tkos_vibrate == 0){ //진동이 꺼져있을 때
                    toggleButton3.setBackgroundColor(Color.GREEN); //버튼색 변경
                    toggleButton3.setText("ON"); //버튼 글자 변경
                    dbactivity.setVibrate(name, 1); //진동 켜짐으로 변경(Database)
                    tkos_vibrate = 1; //진동 켜짐으로 변경
                    vibrator.vibrate(500); // 0.5초간 진동
                    if(tkos_effect == 1) //효과음이 켜져있을 때
                        sound.play(soundId, 0.1F, 0.1F,  0,  0,  1.0F); //사운드 플레이(재생)
                }
            }
        });

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), PopupActivity.class); //PopupActivity로 가는 인텐트를 생성
                startActivityForResult(intent, 1); //액티비티 시작
                if(tkos_effect == 1) //효과음이 켜져있을 때
                    sound.play(soundId, 0.1F, 0.1F,  0,  0,  1.0F); //사운드 플레이(재생)
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), OperationActivity.class); //OperationActivity로 가는 인텐트를 생성
                startActivityForResult(intent, 2); //액티비티 시작
                if(tkos_effect == 1) //효과음이 켜져있을 때
                    sound.play(soundId, 0.1F, 0.1F,  0,  0,  1.0F); //사운드 플레이(재생)
            }
        });

        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), CreditActivity.class); //CreditActivity로 가는 인텐트를 생성
                startActivityForResult(intent, 3); //액티비티 시작
                if(tkos_effect == 1) //효과음이 켜져있을 때
                    sound.play(soundId, 0.1F, 0.1F,  0,  0,  1.0F); //사운드 플레이(재생)
            }
        });

        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton2);
        imageButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        }); //뒤로가기 버튼
    }

    public void onBackPressed() {
        super.onBackPressed();
    }//뒤로가기
}