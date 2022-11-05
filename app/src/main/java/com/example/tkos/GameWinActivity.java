package com.example.tkos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GameWinActivity extends Activity {

	//이 클래스의 객체를 받아옴
	GameActivity gActivity =(GameActivity)GameActivity.gameActivity;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamewin);
		setTitle(R.string.activity_game_wins_title);

		//데이터베이스 선언부분
		final DatabaseActivity dbactivity = new DatabaseActivity(getApplicationContext(), "TKOS.db", null, 1);
		SavedID sid = (SavedID) this.getApplication();
		final String name = sid.getID();
		int tkos_score = dbactivity.getScore(name);
		int tkos_language = dbactivity.getLanguage(name);
		String tkos_engword=dbactivity.getEngword(name);
		String tkos_korword=dbactivity.getKorword(name);

		//점수와 먹은 단어 출력
		TextView textView3 = (TextView) findViewById(R.id.textView2);
		if(tkos_language == 0)
			textView3.setText("나의 최고 점수: " + tkos_score + "점");
		else if(tkos_language == 1)
			textView3.setText("My highscore: " + tkos_score + "point");
		else if(tkos_language == 2)
			textView3.setText("私の上のスコア: " + tkos_score + "スコア");
		TextView textView4 = (TextView) findViewById(R.id.textView4);
		textView4.setText(tkos_engword+ ": "+tkos_korword);

		//나가기버튼
		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//계속하기버튼
		Button button5 = (Button) findViewById(R.id.button6);
		button5.setOnClickListener(new OnClickListener() {
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