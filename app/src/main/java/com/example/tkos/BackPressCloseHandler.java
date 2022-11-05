package com.example.tkos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BackPressCloseHandler extends AppCompatActivity {
    private long backKeyClickTime = 0;
    private Activity activity;
    public BackPressCloseHandler(Activity activity)
    {
        this.activity = activity;
    }

    int tkos_language;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        final DatabaseActivity dbactivity = new DatabaseActivity(getApplication(), "TKOS.db", null, 1); //DB객체 생성
        SavedID sid = (SavedID) getApplication(); //전역변수 참조
        final String name = sid.getID(); //DB ID값 받아오기
        tkos_language = dbactivity.getLanguage(name);

    }
    public void onBackPressed()
    {
        if (System.currentTimeMillis() > backKeyClickTime + 2000) //뒤로가기 버튼을 한번 누를 경우
        {
            backKeyClickTime = System.currentTimeMillis();
            showToast();
            return;
        }
        if (System.currentTimeMillis() <= backKeyClickTime + 2000) //뒤로가기 버튼을 2초이내에 두번 누를 경우
        {
            Intent intent = new Intent(getApplicationContext(), MusicService.class); // 이동할 컴포넌트
            stopService(intent); // 서비스 종료
            activity.finish(); //액티비티 종료
        }
    }

    public void showToast()
    {
        if(tkos_language==0)
            Toast.makeText(activity, "뒤로 가기 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        else if(tkos_language==1)
            Toast.makeText(activity, "Press the back button again to exit.", Toast.LENGTH_SHORT).show();
        else if(tkos_language==2)
            Toast.makeText(activity, "戻るボタンを押すと終了します.", Toast.LENGTH_SHORT).show();
    }
}
