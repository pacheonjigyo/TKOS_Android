package com.example.tkos;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class Fragment2 extends Fragment {
    TextView mTvBluetoothStatus;
    Button mBtnBluetoothOn;
    Button mBtnBluetoothOff;
    BluetoothAdapter mBluetoothAdapter; // 블루투스 어댑터
    BluetoothChatFragment fragment;
    int tkos_effect; //Effect 필드 선언
    int tkos_language; //Effect 필드 선언

    final static int BT_REQUEST_ENABLE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            fragment = new BluetoothChatFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);

        final DatabaseActivity dbactivity = new DatabaseActivity(getActivity().getApplication(), "TKOS.db", null, 1); //DB객체 생성
        SavedID sid = (SavedID) getActivity().getApplication(); //전역변수 참조
        final String name = sid.getID(); //DB ID값 받아오기
        tkos_effect = dbactivity.getEffect(name); //DB의 Effect값 받아오기
        tkos_language = dbactivity.getLanguage(name);

        //SoundPool  생성
        final SoundPool sound = new SoundPool(1, AudioManager.STREAM_ALARM, 0);// maxStreams, streamType, srcQuality
        final int soundId = sound.load(getActivity(), R.raw.coin, 0); //사운드 로드

        mTvBluetoothStatus = (TextView) view.findViewById(R.id.tvBluetoothStatus);

        // 블루투스 활성화하기
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // 블루투스 어댑터를 디폴트 어댑터로 설정

        //페어링된 블루투스 장치 목록을 띄워줍니다. 선택 시 연결을 시도합니다.
        mBtnBluetoothOn = (Button) view.findViewById(R.id.btnBluetoothOn);
        mBtnBluetoothOn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tkos_effect == 1)
                    sound.play(soundId, 0.1F, 0.1F,  0,  0,  1.0F);
                bluetoothOn();
            }
        });

        //연결된 블루투스 장치를 연결해제합니다.
        mBtnBluetoothOff = (Button) view.findViewById(R.id.btnBluetoothOff);
        mBtnBluetoothOff.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tkos_effect == 1)
                    sound.play(soundId, 0.1F, 0.1F,  0,  0,  1.0F);
                bluetoothOff();
            }
        });
        return view;
    }

    void bluetoothOn() {
        if(mBluetoothAdapter == null) { // 디바이스가 블루투스를 지원하지 않을
            if(tkos_language==0)
                Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않는 기기입니다.", Toast.LENGTH_LONG).show();
            else if(tkos_language==1)
                Toast.makeText(getApplicationContext(), "This device does not support Bluetooth.", Toast.LENGTH_LONG).show();
            else if(tkos_language==2)
                Toast.makeText(getApplicationContext(), "Bluetoothをサポートしていない機器です。", Toast.LENGTH_LONG).show();
        }
        else { // 디바이스가 블루투스를 지원 할 때
            if(mBluetoothAdapter.isEnabled()) { // 블루투스가 활성화 상태 (기기에 블루투스가 켜져있음)
                if(tkos_language==0)
                    mTvBluetoothStatus.setText("블루투스 활성화 상태");
                else if(tkos_language==1)
                    mTvBluetoothStatus.setText("Bluetooth activation status");
                else if(tkos_language==2)
                    mTvBluetoothStatus.setText("Bluetooth対応の状態");
            }
            else { // 블루투스가 비 활성화 상태 (기기에 블루투스가 꺼져있음)
                // 블루투스를 활성화 하기 위한 다이얼로그 출력
                Intent intentBluetoothEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                // 선택한 값이 onActivityResult 함수에서 콜백된다.
                startActivityForResult(intentBluetoothEnable, BT_REQUEST_ENABLE);
            }
        }
    }

    void bluetoothOff() {
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
            if(tkos_language==0)
                mTvBluetoothStatus.setText("블루투스 비활성화 상태");
            else if(tkos_language==1)
                mTvBluetoothStatus.setText("Bluetooth disabled state");
            else if(tkos_language==2)
                mTvBluetoothStatus.setText("Bluetoothの無効状態");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BT_REQUEST_ENABLE:
                if (resultCode == RESULT_OK) { // 블루투스 활성화를 확인을 클릭하였다면
                    if(tkos_language==0)
                        mTvBluetoothStatus.setText("블루투스 활성화 상태");
                    else if(tkos_language==1)
                        mTvBluetoothStatus.setText("Bluetooth activation status");
                    else if(tkos_language==2)
                        mTvBluetoothStatus.setText("Bluetooth対応の状態");
                } else if (resultCode == RESULT_CANCELED) { // 블루투스 활성화를 취소를 클릭하였다면
                    if(tkos_language==0)
                        mTvBluetoothStatus.setText("블루투스 비활성화 상태");
                    else if(tkos_language==1)
                        mTvBluetoothStatus.setText("Bluetooth disabled state");
                    else if(tkos_language==2)
                        mTvBluetoothStatus.setText("Bluetoothの無効状態");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}