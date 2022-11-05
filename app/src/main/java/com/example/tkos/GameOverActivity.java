package com.example.tkos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends Activity {
	//게임액티비티 클래스 객체를 받아옴
	GameActivity gActivity =(GameActivity)GameActivity.gameActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameover);
		setTitle(R.string.activity_game_over_title);

		//데이터베이스 선언부분
		final DatabaseActivity dbactivity = new DatabaseActivity(getApplicationContext(), "TKOS.db", null, 1);
		SavedID sid = (SavedID) this.getApplication();
		final String name = sid.getID();
		int tkos_score = dbactivity.getScore(name);
		int tkos_language = dbactivity.getLanguage(name);

		//최고점수 출력
		TextView textView3 = (TextView) findViewById(R.id.textView2);
		if(tkos_language == 0)
			textView3.setText("나의 최고 점수: " + tkos_score + "점");
		else if(tkos_language == 1)
			textView3.setText("My highscore: " + tkos_score + "point");
		else if(tkos_language == 2)
			textView3.setText("私の上のスコア: " + tkos_score + "スコア");

		//나가기버튼
		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		//계속하기버튼
		Button button2 = (Button) findViewById(R.id.button7);
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//데이터 전달하기
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				//액티비티(팝업) 닫기
				finish();
				gActivity.finish();
			}
		});
	}
}