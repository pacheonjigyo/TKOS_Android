package com.example.tkos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class GameActivity extends Activity {
    // 데이터베이스에서 게임옵션을 받아올 변수 선언
    int skin;
    int control;
    int bgopt;
    int soundopt;
    int vibeopt;
    int option;
    int mode;
    int language;

    //게임 종료를 위한 액티비티 선언
    public static Activity gameActivity;

    //그림을 그릴 캔버스 선언
    Canvas canvas;

    // 클래스 선언
    SnakeView snakeView;
    ReadText1 readtext;

    //진동을 위한 Vibrator 객체 선언
    private Vibrator vibrator;

    //이미지를 받아올 비트맵 선언
    Bitmap headBitmap;
    Bitmap bodyBitmap;
    Bitmap tailBitmap;
    Bitmap headLeftBitmap;
    Bitmap bodyLeftBitmap;
    Bitmap tailLeftBitmap;
    Bitmap headUpBitmap;
    Bitmap bodyUpBitmap;
    Bitmap tailUpBitmap;
    Bitmap headDownBitmap;
    Bitmap bodyDownBitmap;
    Bitmap tailDownBitmap;
    Bitmap head1Bitmap;
    Bitmap body1Bitmap;
    Bitmap tail1Bitmap;
    Bitmap head1LeftBitmap;
    Bitmap tail1LeftBitmap;
    Bitmap head1UpBitmap;
    Bitmap tail1UpBitmap;
    Bitmap head1DownBitmap;
    Bitmap tail1DownBitmap;

    Bitmap appleBitmap;
    Bitmap aBitmap;
    Bitmap bBitmap;
    Bitmap cBitmap;
    Bitmap dBitmap;
    Bitmap eBitmap;
    Bitmap fBitmap;
    Bitmap gBitmap;
    Bitmap hBitmap;
    Bitmap iBitmap;
    Bitmap jBitmap;
    Bitmap kBitmap;
    Bitmap lBitmap;
    Bitmap mBitmap;
    Bitmap nBitmap;
    Bitmap oBitmap;
    Bitmap pBitmap;
    Bitmap qBitmap;
    Bitmap rBitmap;
    Bitmap sBitmap;
    Bitmap tBitmap;
    Bitmap uBitmap;
    Bitmap vBitmap;
    Bitmap wBitmap;
    Bitmap xBitmap;
    Bitmap yBitmap;
    Bitmap zBitmap;

    //사운드 출력을 위한 변수 선언
    SoundPool sound;
    int soundId;


    //뱀이 가는 방향을 담는 변수 선언 0 = up, 1 = right, 2 = down, 3= left
    static int directionOfTravel=0;

    //게임의 화면 구성을 위한 변수 선언
    int screenWidth;
    int screenHeight;
    int topGap;

    //게임 진행속도를 위한 변수 선언
    long lastFrameTime;
    int fps;

    //뱀 좌표
    int [] snakeX;
    int [] snakeY;

    //뱀 길이
    int snakeLength;

    //점수
    int score;

    //현재 먹은 알파벳 인덱스 받아올 변수 선언
    int count=0;

    //현재 먹은 알파벳 담는 배열
    String[] array_ate;
    String ate="";

    //알파벳의 좌표를 담는 배열
    int[][] XLocation;
    int[][] YLocation;

    //현재 단어의 알파벳 개수를 담는 변수 선언
    int a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z;

    //현재 단어의 알파벳이 출력되었는지를 담고있는 변수 선언
    boolean[][] created;

    //제스처 관련 옵션 설정
    private static final int SWIPE_MIN_DISTANCE = 80;
    private static final int SWIPE_MAX_OFF_PATH = 600;
    private static final int SWIPE_THRESHOLD_VELOCITY = 80;

    //게임의 전체적인 사이즈를 담기위한 변수 선언
    int blockSize;
    int numBlocksWide;
    int numBlocksHigh;

    //제스처를 위한 변수 선언
    GestureDetector detector;
    private SensorManager mSensorManager = null;

    //기울기 센서를 위한 변수 선언
    GyroscopeListener gyroscopeListener;
    private SensorEventListener mGyroLis;
    private Sensor mGgyroSensor = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //데이터베이스를 사용하기위한 객체 생성
        final DatabaseActivity dbactivity = new DatabaseActivity(getApplicationContext(), "TKOS.db", null, 1);
        SavedID sid = (SavedID) getApplication();
        final String name = sid.getID();

        //액티비티 객체에 현재 클래스를 담는다
        gameActivity=GameActivity.this;

        //데이터베이스에서 필요한 옵션을 받아옴
        skin=dbactivity.getSkin(name); //뱀 스킨
        control=dbactivity.getControl(name);//최고 스코어
        soundopt=dbactivity.getEffect(name);//게임 효과음
        vibeopt=dbactivity.getVibrate(name);//진동
        mode=dbactivity.getMode(name);//시나리오,무작위 모드 설정
        bgopt=dbactivity.getMusic(name); //배경 음악
        language=dbactivity.getLanguage(name);// 언어
        option=dbactivity.getOption(name); //조작 방법

        created= new boolean[6][26];     //해당 알파벳이 출력될지를 담고 있는 변수
        for(int i=0;i<6;i++){
            for(int j=0;j<26;j++){
                created[i][j]=false;
            }
        }

        //알파벳의 좌표를 받아올 변수
        XLocation=new int[6][26];
        YLocation=new int[6][26];

        //기울기 조작 방법일 때
        if (option == 1) {
            gyroscopeListener = new GyroscopeListener();
            //Using the Gyroscope & Accelometer
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

            //Using the Accelometer
            mGgyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            mGyroLis = new GyroscopeListener();
            mSensorManager.registerListener(mGyroLis, mGgyroSensor, SensorManager.SENSOR_DELAY_GAME);
        }

        configureDisplay();

        //알파벳을 받아오기위한 객체 선언
        readtext=new ReadText1();
        readtext.getText(getApplicationContext(), dbactivity, name);

        //뱀 객체를 생성
        snakeView = new SnakeView(this);
        setContentView(snakeView);

        //소리 객체
        sound = new SoundPool(1, AudioManager.STREAM_ALARM, 0);// maxStreams, streamType, srcQuality
        soundId = sound.load(this, R.raw.coin, 0);

        //진동 객체
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        //드래그를 위한 제스처 리스너
        detector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {}

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {}

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(option==0) {
                    try {
                        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                            return false;
                        // 오른쪽에서 왼쪽으로 스와이프
                        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            directionOfTravel = 3;// left
                        }
                        // 왼쪽에서 오른쪽으로 스와이프
                        else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            directionOfTravel = 1;//right

                        }
                        // 아래에서 위로 스와이프
                        else if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                            directionOfTravel = 0;//  up
                        }
                        // 위에서 아래로 스와이프
                        else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                            directionOfTravel = 2;// down
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });

        snakeView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                detector.onTouchEvent(motionEvent  );
                return true;
            }
        });

    }

    class SnakeView extends SurfaceView implements Runnable {
        Thread ourThread = null;
        SurfaceHolder ourHolder;
        volatile boolean playingSnake;
        Paint paint;

        int len; //받아온 알파벳의 길이
        String temp; //영어 단어를 받아오는 변수
        String[] array_eng; //영어 단어를 한글자씩 배열에 받기위한 배열

        //데이터베이스를 사용하기 위한 객체 생성
        final DatabaseActivity dbactivity = new DatabaseActivity(getApplicationContext(), "TKOS.db", null, 1);
        SavedID sid = (SavedID)getApplication();
        final String name = sid.getID();

        //생성자
        public SnakeView(Context context) {
            super(context);
            ourHolder = getHolder();
            paint = new Paint();

            temp = readtext.getEng(); //영어단어를 받아옴
            array_eng = temp.split(""); //받아온 영어단어를 한글자씩 배열에 저장
            len = temp.length(); //영어단어의 길이

            //뱀의 길이
            snakeX = new int[200];
            snakeY = new int[200];

            //뱀의 정보를 할당하는 메서드
            getSnake();

            //영어 단어의 한 알파벳씩 좌표 할당
            getAlphabet();
        }

        public void getSnake() {
            snakeLength = 3;
            //뱀의 위치를 중간에서 시작
            snakeX[0] = numBlocksWide / 2;
            snakeY[0] = numBlocksHigh / 2;

            //Then the body
            snakeX[1] = snakeX[0] - 1;
            snakeY[1] = snakeY[0];

            //And the tail
            snakeX[1] = snakeX[1] - 1;
            snakeY[1] = snakeY[0];
        }

        public void checkEat(int x, int y) { //먹은지 안먹은지 체크
            //알파벳을 먹었을 때
            if (snakeX[0] == XLocation[x][y] && snakeY[0] == YLocation[x][y]){
                if(y==0) ate = ate+"a"; //a일때 먹은 String값에 a추가
                else if(y==1) ate = ate+"b";
                else if(y==2) ate = ate+"c";
                else if(y==3) ate = ate+"d";
                else if(y==4) ate = ate+"e";
                else if(y==5) ate = ate+"f";
                else if(y==6) ate = ate+"g";
                else if(y==7) ate = ate+"h";
                else if(y==8) ate = ate+"i";
                else if(y==9) ate = ate+"j";
                else if(y==10) ate = ate+"k";
                else if(y==11) ate = ate+"l";
                else if(y==12) ate = ate+"m";
                else if(y==13) ate = ate+"n";
                else if(y==14) ate = ate+"o";
                else if(y==15) ate = ate+"p";
                else if(y==16) ate = ate+"q";
                else if(y==17) ate = ate+"r";
                else if(y==18) ate = ate+"s";
                else if(y==19) ate = ate+"t";
                else if(y==20) ate = ate+"u";
                else if(y==21) ate = ate+"v";
                else if(y==22) ate = ate+"w";
                else if(y==23) ate = ate+"x";
                else if(y==24) ate = ate+"y";
                else if(y==25) ate = ate+"z";

                //먹은 알파벳을 토스트메시지로 출력하기위한 부분
                GameActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(GameActivity.this,ate, Toast.LENGTH_LONG).show();
                    }
                });

                //소리옵션이 1일때 소리 출력
                if(soundopt==1){
                    sound.play(soundId, 1.0F, 1.0F, 1, 0, 1.0F);
                }

                //진동옵션이 1일때 진동 출력
                if(vibeopt==1){
                    vibrator.vibrate(500);
                }

                //해당 알파벳 좌표값
                created[x][y] = false;
                //먹은 알파벳을 맞게 먹었는지 체크하는 함수 호출
                int check =eatFailed();
                //배열을 비교할 인덱스++
                if(check==0) //잘먹었을때만 count++
                count++;
            }
        }


        //각 알파벳의 좌표 설정
        public void setAlphabet(int x, int y){
            Random random = new Random();
            XLocation[x][y] = random.nextInt(numBlocksWide-1)+1;
            YLocation[x][y] = random.nextInt(numBlocksHigh-1)+1;
        }

        //게임의 실행부분
        @Override
        public void run() {
            while (playingSnake) {
                updateGame();
                drawGame();
                controlFPS();
                eatSucceed();
            }
        }

        public void eatSucceed() // 단어를 다 먹으면 새롭게 생성
        {
            if(len==count){ //먹은 단어의 길이와 저장된 단어의 길이가 같을 때
                dbactivity.setEngword(name,temp); //데이터베이스에 영어단어 저장
                dbactivity.setKorword(name,readtext.getKor()); //데이터베이스에 한글단어 저장

                //게임에 필요한 변수 초기화
                temp="";
                ate="";
                count=0;
                a=0;b=0;c=0;d=0;e=0;f=0;g=0;h=0;i=0;j=0;k=0;l=0;m=0;n=0;o=0;p=0;q=0;r=0;s=0;t=0;u=0;v=0;w=0;x=0;y=0;z=0;
                for(int i=0;i<6;i++){
                    for(int j=0;j<26;j++){
                        created[i][j]=false;
                    }
                }

                //게임을 이겼을 때 다이얼로그 출력
                Intent intent = new Intent(getApplicationContext(), GameWinActivity.class);
                startActivity(intent);

                //게임 정보 업데이트(점수, 뱀길이)
                updateGame();

                //현재 점수가 최고점수보다 높을 때 갱신
                if(dbactivity.getScore(name) < score)
                    dbactivity.setScore(name, score);
                dbactivity.setControl(name,readtext.getNum());

                //다시 메모장에서 단어를 받아오는 부분
                readtext.getText(getApplicationContext(), dbactivity ,name);
                temp = readtext.getEng();
                array_eng = temp.split("");
                len = temp.length();
                getAlphabet();
            }
        }

        public int eatFailed(){ //잘못된 알파벳을 먹었는지 확인하는 메서드
            array_ate=ate.split(""); //먹은 알파벳을 배열로 저장
            if(!array_ate[count+1].equals(array_eng[count+1])){ //잘못된 알파벳을 먹었을 경우

                //다시 게임을 실행하기위한 변수 초기화
                ate="";
                count=0;
                a=0;b=0;c=0;d=0;e=0;f=0;g=0;h=0;i=0;j=0;k=0;l=0;m=0;n=0;o=0;p=0;q=0;r=0;s=0;t=0;u=0;v=0;w=0;x=0;y=0;z=0;
                for(int i=0;i<6;i++){
                    for(int j=0;j<26;j++){
                        created[i][j]=false;
                    }
                }

                //알파벳과 뱀의 좌표 재설정
                getSnake();
                getAlphabet();
                //게임을 졌을 때 출력되는 다이얼로그
                Intent intent = new Intent(getApplicationContext(), GameOverActivity.class);
                startActivity(intent);

                //점수가 최고치일때 점수 갱신
                if(dbactivity.getScore(name) < score)
                    dbactivity.setScore(name, score);
                return 1;
            }
          return 0;
        }

        //뱀의 길이, 점수를 업데이트하는 메서드
        public void updateGame() {
            //Did the player get the apple
            for(int i=0;i<6;i++){
                for(int j=0;j<26;j++){
                    if((snakeX[0] == XLocation[i][j] && snakeY[0] == YLocation[i][j]))
                    {
                        //grow the snake
                        snakeLength++;
                        //add to the score
                        score = score + snakeLength;
                    }
                }
            }

            //move the body - starting at the back
            for(int i=snakeLength; i >0 ; i--){
                snakeX[i] = snakeX[i-1];
                snakeY[i] = snakeY[i-1];
            }

            //뱀의 방향 설정
            switch (directionOfTravel){
                case 0://up
                    snakeY[0]  --;
                    break;
                case 1://right
                    snakeX[0] ++;
                    break;
                case 2://down
                    snakeY[0] ++;
                    break;
                case 3://left
                    snakeX[0] --;
                    break;
            }

            //뱀이 죽었는지 판단하는 dead변수
            boolean dead = false;
            //뱀이 벽에 부딪힐 때
            if(snakeX[0] == -1)dead=true;
            if(snakeX[0] >= numBlocksWide)dead=true;
            if(snakeY[0] == -1)dead=true;
            if(snakeY[0] == numBlocksHigh)dead=true;
            //뱀이 자기몸에 부딪힐 때
            for (int i = snakeLength-1; i > 0; i--) {
                if ((i > 4) && (snakeX[0] == snakeX[i]) && (snakeY[0] == snakeY[i])) {
                    dead = true;
                }
            }

            if(dead){
                //게임 다시 시작
                //게임을 다시시작하기위한 변수 초기화
                score = 0;
                getSnake();
                array_ate=ate.split("");
                ate="";
                count=0;
                a=0;b=0;c=0;d=0;e=0;f=0;g=0;h=0;i=0;j=0;k=0;l=0;m=0;n=0;o=0;p=0;q=0;r=0;s=0;t=0;u=0;v=0;w=0;x=0;y=0;z=0;
                for(int i=0;i<6;i++){
                    for(int j=0;j<26;j++){
                        created[i][j]=false;
                    }
                }

                //알파벳 좌표 재설정
                getAlphabet();

                //게임에서 졌을 때 다이얼로그 출력
                Intent intent = new Intent(getApplicationContext(), GameOverActivity.class);
                startActivity(intent);
            }
        }

        //캔버스에 게임정보(뱀, 알파벳) 출력
        public void drawGame()
        {
            if (ourHolder.getSurface().isValid()) {
                canvas = ourHolder.lockCanvas();
                //Paint paint = new Paint();
                canvas.drawColor(Color.argb(255, 92, 181, 91));//the background
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(topGap/2);
                if(language==0)//한국어
                    canvas.drawText("점수: " + score +"  단어: "+ readtext.getKor(), 10, topGap-6, paint);
                else if(language==1)//영어
                    canvas.drawText("Score: " + score +"  word: "+ readtext.getKor(), 10, topGap-6, paint);
                else if(language==2)//일본어
                    canvas.drawText("スコア: " + score +"  単語: "+ readtext.getKor(), 10, topGap-6, paint);
                else
                    canvas.drawText("점수: " + score +"  단어: "+ readtext.getKor(), 10, topGap-6, paint);

                //경계를 그린다
                paint.setStrokeWidth(3);//4 pixel border
                canvas.drawLine(1,topGap,screenWidth-1,topGap,paint);
                canvas.drawLine(screenWidth-1,topGap,screenWidth-1,topGap+(numBlocksHigh*blockSize),paint);
                canvas.drawLine(screenWidth-1,topGap+(numBlocksHigh*blockSize),1,topGap+(numBlocksHigh*blockSize),paint);
                canvas.drawLine(1,topGap, 1,topGap+(numBlocksHigh*blockSize), paint);

                //뱀을 그린다
                if(skin==0){ //0번 스킨일때
                    if(directionOfTravel==0)
                    {
                        canvas.drawBitmap(headUpBitmap, snakeX[0]*blockSize, (snakeY[0]*blockSize)+topGap, paint);
                    }
                    else if(directionOfTravel==3)
                    {
                        canvas.drawBitmap(headLeftBitmap, snakeX[0]*blockSize, (snakeY[0]*blockSize)+topGap, paint);
                    }
                    else if(directionOfTravel==2)
                    {
                        canvas.drawBitmap(headDownBitmap, snakeX[0]*blockSize, (snakeY[0]*blockSize)+topGap, paint);
                    }
                    else if(directionOfTravel==1)
                    {
                        canvas.drawBitmap(headBitmap, snakeX[0]*blockSize, (snakeY[0]*blockSize)+topGap, paint);
                    }
                }
                else if(skin==1){
                    if(directionOfTravel==0)
                    {
                        canvas.drawBitmap(head1UpBitmap, snakeX[0]*blockSize, (snakeY[0]*blockSize)+topGap, paint);
                    }
                    else if(directionOfTravel==3)
                    {
                        canvas.drawBitmap(head1LeftBitmap, snakeX[0]*blockSize, (snakeY[0]*blockSize)+topGap, paint);
                    }
                    else if(directionOfTravel==2)
                    {
                        canvas.drawBitmap(head1DownBitmap, snakeX[0]*blockSize, (snakeY[0]*blockSize)+topGap, paint);
                    }
                    else if(directionOfTravel==1)
                    {
                        canvas.drawBitmap(head1Bitmap, snakeX[0]*blockSize, (snakeY[0]*blockSize)+topGap, paint);
                    }
                }

                //몸을 그린다
                for(int i = 1; i < snakeLength-1;i++){
                    if(skin==0){
                        if(directionOfTravel==0)
                        {
                            canvas.drawBitmap(bodyUpBitmap, snakeX[i]*blockSize, (snakeY[i]*blockSize)+topGap, paint);
                        }
                        else if(directionOfTravel==3)
                        {
                            canvas.drawBitmap(bodyLeftBitmap, snakeX[i]*blockSize, (snakeY[i]*blockSize)+topGap, paint);
                        }
                        else if(directionOfTravel==2)
                        {
                            canvas.drawBitmap(bodyDownBitmap, snakeX[i]*blockSize, (snakeY[i]*blockSize)+topGap, paint);
                        }
                        else if(directionOfTravel==1)
                        {
                            canvas.drawBitmap(bodyBitmap, snakeX[i]*blockSize, (snakeY[i]*blockSize)+topGap, paint);
                        }
                    }
                    else if(skin==1){
                        if(directionOfTravel==0)
                        {
                            canvas.drawBitmap(body1Bitmap, snakeX[i]*blockSize, (snakeY[i]*blockSize)+topGap, paint);
                        }
                        else if(directionOfTravel==3)
                        {
                            canvas.drawBitmap(body1Bitmap, snakeX[i]*blockSize, (snakeY[i]*blockSize)+topGap, paint);
                        }
                        else if(directionOfTravel==2)
                        {
                            canvas.drawBitmap(body1Bitmap, snakeX[i]*blockSize, (snakeY[i]*blockSize)+topGap, paint);
                        }
                        else if(directionOfTravel==1)
                        {
                            canvas.drawBitmap(body1Bitmap, snakeX[i]*blockSize, (snakeY[i]*blockSize)+topGap, paint);
                        }
                    }
                }
                //꼬리를 그린다
                if(skin==0){
                    if(directionOfTravel==0)
                    {
                        canvas.drawBitmap(tailUpBitmap, snakeX[snakeLength-1]*blockSize, (snakeY[snakeLength-1]*blockSize)+topGap, paint);
                    }
                    else if(directionOfTravel==3)
                    {
                        canvas.drawBitmap(tailLeftBitmap, snakeX[snakeLength-1]*blockSize, (snakeY[snakeLength-1]*blockSize)+topGap, paint);
                    }
                    else if(directionOfTravel==2)
                    {
                        canvas.drawBitmap(tailDownBitmap, snakeX[snakeLength-1]*blockSize, (snakeY[snakeLength-1]*blockSize)+topGap, paint);
                    }
                    else if(directionOfTravel==1)
                    {
                        canvas.drawBitmap(tailBitmap, snakeX[snakeLength-1]*blockSize, (snakeY[snakeLength-1]*blockSize)+topGap, paint);
                    }
                }
                else if(skin==1){
                    if(directionOfTravel==0)
                    {
                        canvas.drawBitmap(tail1UpBitmap, snakeX[snakeLength-1]*blockSize, (snakeY[snakeLength-1]*blockSize)+topGap, paint);
                    }
                    else if(directionOfTravel==3)
                    {
                        canvas.drawBitmap(tail1LeftBitmap, snakeX[snakeLength-1]*blockSize, (snakeY[snakeLength-1]*blockSize)+topGap, paint);
                    }
                    else if(directionOfTravel==2)
                    {
                        canvas.drawBitmap(tail1DownBitmap, snakeX[snakeLength-1]*blockSize, (snakeY[snakeLength-1]*blockSize)+topGap, paint);
                    }
                    else if(directionOfTravel==1)
                    {
                        canvas.drawBitmap(tail1Bitmap, snakeX[snakeLength-1]*blockSize, (snakeY[snakeLength-1]*blockSize)+topGap, paint);
                    }
                }
                // canvas.drawBitmap(appleBitmap, appleX*blockSize, (appleY*blockSize)+topGap, paint);

                //알파벳 출력
                for(int i=0;i<6;i++){
                    for(int j=0;j<26;j++)
                        print(i,j);
                }
                ourHolder.unlockCanvasAndPost(canvas);
            }
        }

        //게임의 진행속도를 설정하는 메서드
        public void controlFPS() {
            long timeThisFrame = (System.currentTimeMillis() - lastFrameTime);
            long timeToSleep = 200 - timeThisFrame; //앞의 숫자가 클수록 시간이 느리게 감
            if (timeThisFrame > 0) {
                fps = (int) (1000 / timeThisFrame);
            }
            if (timeToSleep > 0) {
                try {
                    ourThread.sleep(timeToSleep);
                } catch (InterruptedException e) {
                    //Print an error message to the console
                    Log.e("error", "failed to load sound files");
                }
            }
            lastFrameTime = System.currentTimeMillis();
        }

        public void pause()
        {
            playingSnake = false;
            try {
                ourThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void resume()
        {
            playingSnake = true;
            ourThread = new Thread(this);
            ourThread.start();
        }

        //받아온 단어를 비교하여 필요한 알파벳만큼 좌표설정
        public void getAlphabet()
        {
            for (int number = 0; number <= len; number++)
            {
                if (array_eng[number].equals("a")) {
                    if(a<6){
                        setAlphabet(a,0);
                        created[a][0]=true;
                    }
                    a++;
                }
                else if (array_eng[number].equals("b"))
                {
                    if(b<6){
                        setAlphabet(b,1);
                        created[b][1]=true;
                    }
                    b++;
                }
                else if (array_eng[number].equals("c"))
                {
                    if(c<6){
                        setAlphabet(c,2);
                        created[c][2]=true;
                    }
                    c++;
                }
                else if (array_eng[number].equals("d"))
                {
                    if(d<6){
                        setAlphabet(d,3);
                        created[d][3]=true;
                    }
                    d++;
                }
                else if (array_eng[number].equals("e"))
                {
                    if(e<6){
                        setAlphabet(e,4);
                        created[e][4]=true;
                    }
                    e++;
                }
                else if (array_eng[number].equals("f"))
                {
                    if(f<6){
                        setAlphabet(f,5);
                        created[f][5]=true;
                    }
                    f++;
                }
                else if (array_eng[number].equals("g"))
                {
                    if(g<6){
                        setAlphabet(g,6);
                        created[g][6]=true;
                    }
                    g++;
                }
                else if (array_eng[number].equals("h"))
                {
                    if(h<6){
                        setAlphabet(h,7);
                        created[h][7]=true;
                    }
                    h++;
                }
                else if (array_eng[number].equals("i"))
                {
                    if(i<6){
                        setAlphabet(i,8);
                        created[i][8]=true;
                    }
                    i++;
                }
                else if (array_eng[number].equals("j"))
                {
                    if(j<6){
                        setAlphabet(j,9);
                        created[j][9]=true;
                    }
                    j++;
                }
                else if (array_eng[number].equals("k"))
                {
                    if(k<6){
                        setAlphabet(k,10);
                        created[k][10]=true;
                    }
                    k++;
                }
                else if (array_eng[number].equals("l"))
                {
                    if(l<6){
                        setAlphabet(l,11);
                        created[l][11]=true;
                    }
                    l++;
                }
                else if (array_eng[number].equals("m"))
                {
                    if(m<6){
                        setAlphabet(m,12);
                        created[m][12]=true;
                    }
                    m++;
                }
                else if (array_eng[number].equals("n"))
                {
                    if(n<6){
                        setAlphabet(n,13);
                        created[n][13]=true;
                    }
                    n++;
                }
                else if (array_eng[number].equals("o"))
                {
                    if(o<6){
                        setAlphabet(o,14);
                        created[o][14]=true;
                    }
                    o++;
                }
                else if (array_eng[number].equals("p"))
                {
                    if(p<6){
                        setAlphabet(p,15);
                        created[p][15]=true;
                    }
                    p++;
                }
                else if (array_eng[number].equals("q"))
                {
                    if(q<6){
                        setAlphabet(q,16);
                        created[q][16]=true;
                    }
                    q++;
                }
                else if (array_eng[number].equals("r"))
                {
                    if(r<6){
                        setAlphabet(r,17);
                        created[r][17]=true;
                    }
                    r++;
                }
                else if (array_eng[number].equals("s"))
                {
                    if(s<6){
                        setAlphabet(s,18);
                        created[s][18]=true;
                    }
                    s++;
                }
                else if (array_eng[number].equals("t"))
                {
                    if(t<6){
                        setAlphabet(t,19);
                        created[t][19]=true;
                    }
                    t++;
                }
                else if (array_eng[number].equals("u"))
                {
                    if(u<6){
                        setAlphabet(u,20);
                        created[u][20]=true;
                    }
                    u++;
                }
                else if (array_eng[number].equals("v"))
                {
                    if(v<6){
                        setAlphabet(v,21);
                        created[v][21]=true;
                    }
                    v++;
                }
                else if (array_eng[number].equals("w"))
                {
                    if(w<6){
                        setAlphabet(w,22);
                        created[w][22]=true;
                    }
                    w++;
                }
                else if (array_eng[number].equals("x"))
                {
                    if(x<6){
                        setAlphabet(x,23);
                        created[x][23]=true;
                    }
                    x++;
                }
                else if (array_eng[number].equals("y"))
                {
                    if(y<6){
                        setAlphabet(y,24);
                        created[z][24]=true;
                    }
                    y++;
                }
                else if (array_eng[number].equals("z"))
                {
                    if(z<6){
                        setAlphabet(z,25);
                        created[z][25]=true;
                    }
                    z++;
                }
            }
        }

        //현재 단어의 알파벳 출력
        private void print(int x, int y) {
            if (y == 0) {//알파벳 a일때
                if (created[x][y]) {  //현재 알파벳이 true이면(출력해야하면)
                    checkEat(x,y); //현재 알파벳을 먹었는지 체크
                    canvas.drawBitmap(aBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint); //a 알파벳 화면에 출력
                }
            }
            else if (y == 1) { //알파벳 b일때
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(bBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
            else if (y == 2) { //c
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(cBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
            else if (y == 3) {
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(dBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
            else if (y == 4) {
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(eBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
            else if (y == 5) {
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(fBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
            else if (y == 6) {
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(gBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
            else if (y == 7) {
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(hBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
            else if (y == 8) {
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(iBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
            else if (y == 9) {
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(jBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
            else if (y == 10) {
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(kBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
            else if (y == 11) {
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(lBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
            else if (y == 12) {
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(mBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
            else if (y == 13) {
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(nBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
            else if (y == 14) {
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(oBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
            else if (y == 15) {
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(pBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
            else if (y == 16) {
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(qBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
            else if (y == 17) {
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(rBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
            else if (y == 18) {
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(sBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
            else if (y == 19) {
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(tBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
            else if (y == 20) {
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(uBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
            else if (y == 21) {
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(vBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
            else if (y == 22) {
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(wBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
            else if (y == 23) {
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(xBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
            else if (y == 24) {
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(yBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
            else if (y == 25) {
                if (created[x][y]) {
                    checkEat(x,y);
                    canvas.drawBitmap(zBitmap, XLocation[x][y] * blockSize, (YLocation[x][y] * blockSize) + topGap, paint);
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        while (true) {
            snakeView.pause();
            break;
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(getApplicationContext(), MusicService.class);
        startService(intent);
        snakeView.resume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent(getApplicationContext(), MusicService.class);
        stopService(intent);
        snakeView.pause();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            snakeView.pause();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return false;
    }

    public void configureDisplay(){
        //find out the width and height of the screen
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        topGap = screenHeight/14;

        //Determine the size of each block/place on the game board
        blockSize = screenWidth/15;

        //Determine how many game blocks will fit into the height and width
        //Leave one block for the score at the top
        numBlocksWide = 15;
        numBlocksHigh = ((screenHeight - topGap ))/blockSize;

        //Load and scale bitmaps
        //skin 0
        //right
        headBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.head);
        bodyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.body);
        tailBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tail);
        appleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.apple);
        //left
        headLeftBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.headleft);
        bodyLeftBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bodyleft);
        tailLeftBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tailleft);
        //up
        headUpBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.headup);
        bodyUpBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bodyup);
        tailUpBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tailup);
        //down
        headDownBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.headdown);
        bodyDownBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bodydown);
        tailDownBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.taildown);

        //skin 1
        //right
        head1Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.head1right);
        body1Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.body1);
        tail1Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tail1right);
        //left
        head1LeftBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.head1left);
        tail1LeftBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tail1left);
        //up
        head1UpBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.head1up);
        tail1UpBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tail1up);
        //down
        head1DownBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.head1down);
        tail1DownBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tail1down);

        aBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.a);
        bBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.b);
        cBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.c);
        dBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.d);
        eBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.e);
        fBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.f);
        gBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.g);
        hBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.h);
        iBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.i);
        jBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.j);
        kBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.k);
        lBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.l);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.m);
        nBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.n);
        oBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.o);
        pBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.p);
        qBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.q);
        rBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.r);
        sBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.s);
        tBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.t);
        uBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.u);
        vBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.v);
        wBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.w);
        xBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.x);
        yBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.y);
        zBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.z);

        //scale the bitmaps to match the block size
        headBitmap = Bitmap.createScaledBitmap(headBitmap, blockSize, blockSize, false);
        bodyBitmap = Bitmap.createScaledBitmap(bodyBitmap, blockSize, blockSize, false);
        tailBitmap = Bitmap.createScaledBitmap(tailBitmap, blockSize, blockSize, false);

        headLeftBitmap = Bitmap.createScaledBitmap(headLeftBitmap, blockSize, blockSize, false);
        bodyLeftBitmap = Bitmap.createScaledBitmap(bodyLeftBitmap, blockSize, blockSize, false);
        tailLeftBitmap = Bitmap.createScaledBitmap(tailLeftBitmap, blockSize, blockSize, false);

        headUpBitmap = Bitmap.createScaledBitmap(headUpBitmap, blockSize, blockSize, false);
        bodyUpBitmap = Bitmap.createScaledBitmap(bodyUpBitmap, blockSize, blockSize, false);
        tailUpBitmap = Bitmap.createScaledBitmap(tailUpBitmap, blockSize, blockSize, false);

        headDownBitmap = Bitmap.createScaledBitmap(headDownBitmap, blockSize, blockSize, false);
        bodyDownBitmap = Bitmap.createScaledBitmap(bodyDownBitmap, blockSize, blockSize, false);
        tailDownBitmap = Bitmap.createScaledBitmap(tailDownBitmap, blockSize, blockSize, false);

        head1Bitmap = Bitmap.createScaledBitmap(head1Bitmap, blockSize, blockSize, false);
        body1Bitmap = Bitmap.createScaledBitmap(body1Bitmap, blockSize, blockSize, false);
        tail1Bitmap = Bitmap.createScaledBitmap(tail1Bitmap, blockSize, blockSize, false);

        head1LeftBitmap = Bitmap.createScaledBitmap(head1LeftBitmap, blockSize, blockSize, false);
        tail1LeftBitmap = Bitmap.createScaledBitmap(tail1LeftBitmap, blockSize, blockSize, false);

        head1UpBitmap = Bitmap.createScaledBitmap(head1UpBitmap, blockSize, blockSize, false);
        tail1UpBitmap = Bitmap.createScaledBitmap(tail1UpBitmap, blockSize, blockSize, false);

        head1DownBitmap = Bitmap.createScaledBitmap(head1DownBitmap, blockSize, blockSize, false);
        tail1DownBitmap = Bitmap.createScaledBitmap(tail1DownBitmap, blockSize, blockSize, false);

        appleBitmap = Bitmap.createScaledBitmap(appleBitmap, blockSize, blockSize, false);
        aBitmap=Bitmap.createScaledBitmap(aBitmap, blockSize, blockSize, false);
        bBitmap=Bitmap.createScaledBitmap(bBitmap, blockSize, blockSize, false);
        cBitmap=Bitmap.createScaledBitmap(cBitmap, blockSize, blockSize, false);
        dBitmap=Bitmap.createScaledBitmap(dBitmap, blockSize, blockSize, false);
        eBitmap=Bitmap.createScaledBitmap(eBitmap, blockSize, blockSize, false);
        fBitmap=Bitmap.createScaledBitmap(fBitmap, blockSize, blockSize, false);
        gBitmap=Bitmap.createScaledBitmap(gBitmap, blockSize, blockSize, false);
        hBitmap=Bitmap.createScaledBitmap(hBitmap, blockSize, blockSize, false);
        iBitmap=Bitmap.createScaledBitmap(iBitmap, blockSize, blockSize, false);
        jBitmap=Bitmap.createScaledBitmap(jBitmap, blockSize, blockSize, false);
        kBitmap=Bitmap.createScaledBitmap(kBitmap, blockSize, blockSize, false);
        lBitmap=Bitmap.createScaledBitmap(lBitmap, blockSize, blockSize, false);
        mBitmap=Bitmap.createScaledBitmap(mBitmap, blockSize, blockSize, false);
        nBitmap=Bitmap.createScaledBitmap(nBitmap, blockSize, blockSize, false);
        oBitmap=Bitmap.createScaledBitmap(oBitmap, blockSize, blockSize, false);
        pBitmap=Bitmap.createScaledBitmap(pBitmap, blockSize, blockSize, false);
        qBitmap=Bitmap.createScaledBitmap(qBitmap, blockSize, blockSize, false);
        rBitmap=Bitmap.createScaledBitmap(rBitmap, blockSize, blockSize, false);
        sBitmap=Bitmap.createScaledBitmap(sBitmap, blockSize, blockSize, false);
        tBitmap=Bitmap.createScaledBitmap(tBitmap, blockSize, blockSize, false);
        uBitmap=Bitmap.createScaledBitmap(uBitmap, blockSize, blockSize, false);
        vBitmap=Bitmap.createScaledBitmap(vBitmap, blockSize, blockSize, false);
        wBitmap=Bitmap.createScaledBitmap(wBitmap, blockSize, blockSize, false);
        xBitmap=Bitmap.createScaledBitmap(xBitmap, blockSize, blockSize, false);
        yBitmap=Bitmap.createScaledBitmap(yBitmap, blockSize, blockSize, false);
        zBitmap=Bitmap.createScaledBitmap(zBitmap, blockSize, blockSize, false);
    }
}

//영어단어를 받아오는 메서드
class ReadText1 {
    int num;
    Random random;
    String word[],entire;
    String split_entire[];
    String temp;
    private String eng;
    private String kor;
    String readData;

    public void getText(Context context, DatabaseActivity dbactivity, String name){
        try {
            random= new Random();
            InputStream fis = context.getResources().openRawResource(R.raw.word); //단어장 참조
            //단어장의 모든 단어를 받아옴
            byte[] data = new byte[fis.available()];
            while(fis.read(data)!=-1){;}
            readData = new String(data);
            entire=readData;
            split_entire=entire.split("\\n"); //줄바뀔때마다(한단어 단위로) 배열에 넣기

            if(dbactivity.getMode(name) == 0) //시나리오모드
            {
                num=dbactivity.getControl(name);
                num++;
            }
            else //무작위 모드
                num = random.nextInt(3000) + 1;

            //해당단어 찾기
            for(int i=0; i<num;i++){
                temp= split_entire[i];
            }
            //영어와 한글단어를 분리
            for(int i=0; i<2;i++){
                word=temp.split("/");
            }
            //배열에 저장
            this.eng=word[1];
            this.kor=word[2];

        } catch (IOException e) {
            readData = "failed read";
            e.printStackTrace();
        }
    }
    public String getEng() {
        return this.eng;
    }

    public String getKor() {
        return this.kor;
    }

    public int getNum() {
        return this.num;
    }
}

//기울기 센서를 사용하는 메서드
class GyroscopeListener implements SensorEventListener {
    double gyroX;
    double gyroY;
    GameActivity g;

    @Override
    public void onSensorChanged(SensorEvent event) {
        g=new GameActivity();
        /* 각 축의 각속도 성분을 받는다. */
        gyroX = event.values[0];
        gyroY = event.values[1];
        double gyroZ = event.values[2];

        if((int) gyroX<0){//&&(int) gyroX<(int) gyroY
            g.directionOfTravel=0;//up
        }
        else if((int) gyroX>0){//&&(int) gyroX>(int) gyroY
            g.directionOfTravel=2;//down
        }
        else if((int) gyroY>0){//&&(int) gyroX<(int) gyroY
            g.directionOfTravel=1;//left
        }
        else if((int) gyroY<0){//&&(int) gyroX>(int) gyroY
            g.directionOfTravel=3;//right
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
