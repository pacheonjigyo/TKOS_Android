package com.example.tkos;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class OperationActivity extends Activity {
    int tkos_effect; //Effect 필드 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀바 없애기
        setContentView(R.layout.operation_activity); //operation_activity.xml 참조

        //SoundPool  생성
        final SoundPool sound = new SoundPool(1, AudioManager.STREAM_RING, 0);// maxStreams, streamType, srcQuality
        final int soundId = sound.load(this, R.raw.coin, 1); //사운드 로드

        final DatabaseActivity dbactivity = new DatabaseActivity(getApplicationContext(), "TKOS.db", null, 1); //DB객체 생성
        SavedID sid = (SavedID)getApplication(); //전역변수 참조
        final String name = sid.getID(); //DB ID값 받아오기
        tkos_effect = dbactivity.getEffect(name); //DB의 Effect값 받아오기

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(tkos_effect == 1)
                    sound.play(soundId, 0.1F, 0.1F,  0,  0,  1.0F);
                dbactivity.setOption(name,0); //드래그
                finish(); //액티비티(팝업) 닫기
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(tkos_effect == 1)
                    sound.play(soundId, 0.1F, 0.1F,  0,  0,  1.0F);
                dbactivity.setOption(name,1); //기울기
                finish(); //액티비티(팝업) 닫기
            }
        });
    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        Intent intent = new Intent(); //데이터 전달하기
        setResult(RESULT_OK, intent);
        this.finish(); //액티비티(팝업) 닫기
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){ //바깥레이어 클릭시 안닫히게
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() { //안드로이드 백버튼 막기
        return;
    }
}
