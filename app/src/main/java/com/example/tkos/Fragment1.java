package com.example.tkos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class Fragment1 extends Fragment {

    Bitmap headAnimBitmap; //Bitmap 필드 선언
    int screenWidth; //screenWidth 필드 선언
    int screenHeight; //screenHeight 필드 선언
    int tkos_effect; //Effect 필드 선언
    Intent intent; //intent 필드 선언
    ImageView imageview; //ImageView 필드 선언

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false); //fragment2.xml 참조

        //SoundPool  생성
        final SoundPool sound = new SoundPool(1, AudioManager.STREAM_ALARM, 0);// maxStreams, streamType, srcQuality
        final int soundId = sound.load(getActivity(), R.raw.coin, 0); //사운드 로드

        final DatabaseActivity dbactivity = new DatabaseActivity(getActivity().getApplication(), "TKOS.db", null, 1); //DB객체 생성
        SavedID sid = (SavedID) getActivity().getApplication(); //전역변수 참조
        final String name = sid.getID(); //DB ID값 받아오기
        tkos_effect = dbactivity.getEffect(name); //DB의 Effect값 받아오기
        Button button = (Button) view.findViewById(R.id.button2);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(tkos_effect == 1)
                    sound.play(soundId, 0.1F, 0.1F,  0,  0,  1.0F);
                dbactivity.setMode(name,0);
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);

                screenWidth = size.x;
                screenHeight = size.y;
                headAnimBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.head_sprite_sheet);

                intent = new Intent(getContext(), GameActivity.class); //인텐트 참조
                startActivity(intent); //액티비티 시작
            }
        });

        Button button1 = (Button) view.findViewById(R.id.button3);
        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tkos_effect == 1)
                    sound.play(soundId, 0.1F, 0.1F,  0,  0,  1.0F); //사운드 플레이(재생)
                dbactivity.setMode(name,1);

                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);

                screenWidth = size.x;
                screenHeight = size.y;
                headAnimBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.head_sprite_sheet);

                intent = new Intent(getContext(), GameActivity.class); //인텐트 참조
                startActivity(intent); //액티비티 시작
            }
        });

        imageview = (ImageView) view.findViewById(R.id.imageView5);

        Button button2 = (Button) view.findViewById(R.id.button4);
        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tkos_skin = dbactivity.getSkin(name);

                if(tkos_skin == 0){
                    imageview.setImageResource(R.drawable.head1right);
                    dbactivity.setSkin(name,1);
                }
                if(tkos_skin == 1) {
                    imageview.setImageResource(R.drawable.head);
                    dbactivity.setSkin(name,0);
                }
                if(tkos_effect == 1)
                    sound.play(soundId, 0.1F, 0.1F,  0,  0,  1.0F);
            }
        });

        Button button3 = (Button) view.findViewById(R.id.button5);
        button3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbactivity.setControl(name,0);
                int tkos_language = dbactivity.getLanguage(name);
                if(tkos_language == 0)
                    Toast.makeText(getActivity(), "시나리오가 초기화 되었습니다.", Toast.LENGTH_LONG).show();
                else if(tkos_language == 1)
                    Toast.makeText(getActivity(), "The scenario has been initialized.", Toast.LENGTH_LONG).show();
                else if(tkos_language == 2)
                    Toast.makeText(getActivity(), "シナリオが初期化されました。", Toast.LENGTH_LONG).show();

                if(tkos_effect == 1)
                    sound.play(soundId, 0.1F, 0.1F,  0,  0,  1.0F);
            }
        });
        return view;
    }
}
