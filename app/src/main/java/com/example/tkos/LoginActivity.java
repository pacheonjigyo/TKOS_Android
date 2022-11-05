package com.example.tkos;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
    int tkos_effect;
    int tkos_language;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);

        final SoundPool sound = new SoundPool(1, AudioManager.STREAM_ALARM, 0);// maxStreams, streamType, srcQuality
        final int soundId = sound.load(getApplicationContext(), R.raw.coin, 0);

        final Button button1 = (Button) findViewById(R.id.button1);
        final TextView textView = (TextView) findViewById(R.id.textView1);

        final DatabaseActivity dbactivity = new DatabaseActivity(getApplicationContext(), "TKOS.db", null, 1);
        SavedID sid = (SavedID) this.getApplication();
        final String name = sid.getID();
        tkos_language = dbactivity.getLanguage(name);

        button1.setOnClickListener(null);

        EditText editText = (EditText) findViewById(R.id.editText1);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) { //입력 상자 내용이 바뀔때
                if(charSequence.length() == 0) { //공백일 경우
                    if(tkos_language==0)
                        textView.setText("공백 형태의 ID는 허용되지 않습니다!");
                    else if(tkos_language==1)
                        textView.setText("Blank IDs are not allowed!");
                    else if(tkos_language==2)
                        textView.setText("空白形態のIDは許可されません！");

                    button1.setOnClickListener(null);
                }
                else
                {
                    final String name = charSequence.toString().toUpperCase(); //대문자로 통일해서 처리함

                    if (!dbactivity.getID(name)) { //아이디가 이미 존재하는 경우
                        if(tkos_language==0)
                            textView.setText("등록된 ID를 찾았습니다!");
                        else if(tkos_language==1)
                            textView.setText("We found your registered ID!");
                        else if(tkos_language==2)
                            textView.setText("登録されたIDが見つかりました！");
                    } else { //아이디가 데이터베이스에 없는 경우
                        if(tkos_language==0)
                            textView.setText("새 ID를 생성하시겠습니까?");
                        else if(tkos_language==1)
                            textView.setText("Do you want to generate a new ID?");
                        else if(tkos_language==2)
                            textView.setText("新しいIDを作成しますか？");
                    }

                    button1.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (dbactivity.getID(name))
                                dbactivity.insert(name, "unnamed", 0, null, 0, 0, 1, 1, 1, 0, 0,0," "," ");
                            if(tkos_effect == 1)
                                sound.play(soundId, 0.5F, 0.5F,  0,  0,  1.0F);
                            SavedID sid = (SavedID) getApplication();
                            sid.setID(name);

                            finish();
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE)
            return false;
        return true;
    }

    @Override
    public void onBackPressed() {
        return;
    }
}