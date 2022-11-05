package com.example.tkos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

public class CreditActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀바 없애기
        setContentView(R.layout.activity_credits); //activity_credits.xml 참조
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
