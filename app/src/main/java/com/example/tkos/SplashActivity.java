package com.example.tkos;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

public class SplashActivity extends Activity implements AutoPermissionsListener
{
    private BackPressCloseHandler backPressCloseHandler; //backPressCloseHandler 참조
    int tkos_language;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash); //splash.xml 참조

        backPressCloseHandler = new BackPressCloseHandler(this); //뒤로가기 버튼 생성
        final DatabaseActivity dbactivity = new DatabaseActivity(this, "TKOS.db", null, 1);
        SavedID sid = (SavedID) this.getApplication();
        final String name = sid.getID();
        tkos_language = dbactivity.getLanguage(name);

        BluetoothAdapter ap = BluetoothAdapter.getDefaultAdapter(); // 블루투스 어댑터를 디폴트 어댑터로 설정
        if (ap == null) //블루투스 어댑터가 없으면
        {   if(tkos_language==0)
            Toast.makeText(this,"이 기기는 블루투스를 지원하지 않습니다.", Toast.LENGTH_SHORT).show();
        else if(tkos_language==1)
            Toast.makeText(this,"This device does not support Bluetooth.", Toast.LENGTH_SHORT).show();
        else if(tkos_language==2)
            Toast.makeText(this,"この機器は、Bluetoothをサポートしていません。", Toast.LENGTH_SHORT).show();
        }
        else { //어댑터가 있으면
            if (tkos_language == 0)
                Toast.makeText(this,"이 기기는 블루투스를 지원합니다.", Toast.LENGTH_SHORT).show();
            else if (tkos_language == 1)
                Toast.makeText(this,"This device supports Bluetooth.", Toast.LENGTH_SHORT).show();
            else if (tkos_language == 2)
                Toast.makeText(this,"この機器は、Bluetoothをサポートします。", Toast.LENGTH_SHORT).show();
        }
        Button button = (Button)findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), MainActivity.class); //인텐트 참조
                startActivity(intent); //액티비티 시작
                intent = new Intent(getApplicationContext(), LoginActivity.class); //인텐트 참조
                startActivity(intent); //액티비티 시작
                finish(); //액티비티 종료
            }
        });

        AutoPermissions.Companion.loadAllPermissions(this, 101); //권한 부여
    }

    @Override
    public void onDenied(int i, String[] permissions) { //권한이 거부되었을 때
        if(tkos_language==0)
            Toast.makeText(this,"권한이 거부되었습니다. : " + permissions.length, Toast.LENGTH_LONG).show();
        else if(tkos_language==1)
            Toast.makeText(this,"Permission denied. : " + permissions.length, Toast.LENGTH_LONG).show();
        else if(tkos_language==2)
            Toast.makeText(this,"アクセスが拒否されました。 : " + permissions.length, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGranted(int i, String[] permissions) { //권한이 허용되었을 때
        if(tkos_language==0)
            Toast.makeText(this,"권한이 허용되었습니다. : " + permissions.length, Toast.LENGTH_LONG).show();
        else if(tkos_language==1)
            Toast.makeText(this,"Permission granted. : " + permissions.length, Toast.LENGTH_LONG).show();
        else if(tkos_language==2)
            Toast.makeText(this,"権限が許可されました。: " + permissions.length, Toast.LENGTH_LONG).show();
    }

    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}