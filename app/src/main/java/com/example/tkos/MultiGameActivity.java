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
import java.util.Random;

public class MultiGameActivity extends Activity {
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

    //사운드 출력을 위한 변수 선언
    SoundPool sound;
    int soundId;
    //사과 좌표
    int appleX;
    int appleY;

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
    GyroscopeListener1 gyroscopeListener;
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
        gameActivity=MultiGameActivity.this;

        //데이터베이스에서 필요한 옵션을 받아옴
        skin=dbactivity.getSkin(name); //뱀 스킨
        control=dbactivity.getControl(name);//최고 스코어
        soundopt=dbactivity.getEffect(name);//게임 효과음
        vibeopt=dbactivity.getVibrate(name);//진동
        mode=dbactivity.getMode(name);//시나리오,무작위 모드 설정
        bgopt=dbactivity.getMusic(name); //배경 음악
        language=dbactivity.getLanguage(name);// 언어
        option=dbactivity.getOption(name); //조작 방법

        //기울기 조작 방법일 때
        if (option == 1) {
            gyroscopeListener = new GyroscopeListener1();
            //Using the Gyroscope & Accelometer
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

            //Using the Accelometer
            mGgyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            mGyroLis = new GyroscopeListener1();
            mSensorManager.registerListener(mGyroLis, mGgyroSensor, SensorManager.SENSOR_DELAY_GAME);
        }

        configureDisplay();

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

        //데이터베이스를 사용하기 위한 객체 생성
        final DatabaseActivity dbactivity = new DatabaseActivity(getApplicationContext(), "TKOS.db", null, 1);
        SavedID sid = (SavedID)getApplication();
        final String name = sid.getID();

        //생성자
        public SnakeView(Context context) {
            super(context);
            ourHolder = getHolder();
            paint = new Paint();

            //뱀의 길이
            snakeX = new int[200];
            snakeY = new int[200];

            //뱀의 정보를 할당하는 메서드
            getSnake();
            getApple();
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


        //각 알파벳의 좌표 설정
        public void getApple(){
            Random random = new Random();
            appleX = random.nextInt(numBlocksWide-1)+1;
            appleY = random.nextInt(numBlocksHigh-1)+1;
        }

        //게임의 실행부분
        @Override
        public void run() {
            while (playingSnake) {
                updateGame();
                drawGame();
                controlFPS();
            }
        }

        //뱀의 길이, 점수를 업데이트하는 메서드
        public void updateGame() {
            //Did the player get the apple
            if((snakeX[0] == appleX && snakeY[0] == appleY))
            {
                //grow the snake
                snakeLength++;
                //add to the score
                getApple();
                score = score + snakeLength;

                //소리옵션이 1일때 소리 출력
                if(soundopt==1){
                    sound.play(soundId, 1.0F, 1.0F, 1, 0, 1.0F);
                }
                //진동옵션이 1일때 진동 출력
                if(vibeopt==1){
                    vibrator.vibrate(500);
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
                    canvas.drawText("점수: " + score, 10, topGap-6, paint);
                else if(language==1)//영어
                    canvas.drawText("Score: " + score , 10, topGap-6, paint);
                else if(language==2)//일본어
                    canvas.drawText("スコア: " + score , 10, topGap-6, paint);
                else
                    canvas.drawText("점수: " + score , 10, topGap-6, paint);

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
                canvas.drawBitmap(appleBitmap, appleX*blockSize, (appleY*blockSize)+topGap, paint);

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

    }
}

class GyroscopeListener1 implements SensorEventListener {
    double gyroX;
    double gyroY;
    MultiGameActivity g;

    @Override
    public void onSensorChanged(SensorEvent event) {
        g=new MultiGameActivity();
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