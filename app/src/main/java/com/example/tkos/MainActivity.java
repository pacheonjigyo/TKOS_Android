package com.example.tkos;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BackPressCloseHandler backPressCloseHandler;
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    MenuItem prevMenuItem;
    int tkos_language;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //activity_main.xml 참조
        final DatabaseActivity dbactivity = new DatabaseActivity(getApplicationContext(), "TKOS.db", null, 1); //DB객체 생성
        SavedID sid = (SavedID) getApplication(); //전역변수 참조
        final String name = sid.getID(); //DB ID값 받아오기
        tkos_language = dbactivity.getLanguage(name); //DB Language값 받아오기
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.tkos", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Intent svc = new Intent(this, MusicService.class); // 인텐트 참조
        startService(svc); //서비스 시작

        backPressCloseHandler = new BackPressCloseHandler(this);

        viewPager = (ViewPager) findViewById(R.id.pager);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab1:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.tab2:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.tab3:
                        viewPager.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null)
                    prevMenuItem.setChecked(false);
                else
                    bottomNavigationView.getMenu().getItem(2).setChecked(false);

                bottomNavigationView.getMenu().getItem(position).setChecked(true);

                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        setupViewPager(viewPager);
        CheckTypesTask task = new CheckTypesTask();
        task.execute();
    }

    private void setupViewPager(ViewPager viewPager) {
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());

        Fragment1 fragment1 = new Fragment1();
        adapter.addItem(fragment1);

        Fragment2 fragment2 = new Fragment2();
        adapter.addItem(fragment2);

        Fragment3 fragment3 = new Fragment3();
        adapter.addItem(fragment3);

        viewPager.setAdapter(adapter);
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> items = new ArrayList<Fragment>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addItem(Fragment item) {
            items.add(item);
        }

        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch(position)
            {
                case 0: {
                    if (tkos_language == 0)
                        return ("혼자하기");
                    else if (tkos_language == 1)
                        return ("Alone");
                    else if (tkos_language == 2)
                        return ("一人で");
                }
                case 1: {
                    if (tkos_language == 0)
                        return ("같이하기");
                    else if (tkos_language == 1)
                        return ("Together");
                    else if (tkos_language == 2)
                        return ("一緒に");
                }
                case 2: {
                    if (tkos_language == 0)
                        return ("설정");
                    else if (tkos_language == 1)
                        return ("Configuration");
                    else if (tkos_language == 2)
                        return ("設定");

                }
                default: break;
            }
            return ("NULL");
        }
    }

    class CheckTypesTask extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog asyncDialog = new ProgressDialog(MainActivity.this);

        @Override
    protected void onPreExecute() {
//        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        if (tkos_language == 0)
//            asyncDialog.setMessage("로딩중입니다..");
//        else if (tkos_language == 1)
//            asyncDialog.setMessage("Loading..");
//        else if (tkos_language == 2)
//            asyncDialog.setMessage("読み込んでいます..");
//        asyncDialog.show();// show dialog
        super.onPreExecute();
    }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                for (int i = 0; i < 1; i++)
                    Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }

    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}


