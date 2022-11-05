package com.example.tkos;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.MessageDialog;
import com.facebook.share.widget.ShareDialog;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Fragment3 extends Fragment{
    ImageView imageView; //ImageView 필드 선언
    CallbackManager callbackManager; //CallbackManager 필드 선언
    ShareDialog shareDialog; //ShareDialog 필드 선언
    int tkos_effect; //Effect 필드 선언
    int tkos_language; //Language 필드 선언
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment3, container, false); //fragment3.xml 참조

        //SoundPool  생성
        final SoundPool sound = new SoundPool(1, AudioManager.STREAM_ALARM, 0);// maxStreams, streamType, srcQuality
        final int soundId = sound.load(getActivity(), R.raw.coin, 0); //사운드 로드

        final DatabaseActivity dbactivity = new DatabaseActivity(getContext(), "TKOS.db", null, 1); //DB객체 생성
        SavedID sid = (SavedID) getActivity().getApplication(); //전역변수 참조
        final String name = sid.getID(); //DB ID값 받아오기
        final String tkos_nickname = dbactivity.getNickname(name); //DB Nickname값 받아오기
        final int tkos_score = dbactivity.getScore(name); //DB Score값 받아오기
        final byte[] tkos_profile = dbactivity.getProfile(name); //DB Profile값 받아오기
        tkos_language = dbactivity.getLanguage(name); //DB Language값 받아오기
        tkos_effect = dbactivity.getEffect(name); //DB의 Effect값 받아오기

        imageView = view.findViewById(R.id.imageView2);
        Bitmap bitmap = BitmapFactory.decodeByteArray(tkos_profile, 0, tkos_profile.length);
        imageView.setImageBitmap(bitmap);

        ImageButton imageButton = (ImageButton) view.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tkos_effect == 1)
                    sound.play(soundId, 0.1F, 0.1F,  0,  0,  1.0F);
                openGallery();
            }
        });

        TextView textView = (TextView) view.findViewById((R.id.textView9));
        textView.setText(name);

        final EditText editText = (EditText) view.findViewById(R.id.editText);

        if(!tkos_nickname.equals("unnamed"))
            editText.setText(dbactivity.getNickname(name));

        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                dbactivity.setNickname(name, textView.getText().toString());
                if(tkos_language == 0)
                Toast.makeText(getActivity(), "닉네임이 저장되었습니다.", Toast.LENGTH_LONG).show();
                else if(tkos_language == 1)
                    Toast.makeText(getActivity(), "Nickname saved.", Toast.LENGTH_LONG).show();
                else if(tkos_language == 2)
                    Toast.makeText(getActivity(), "ニックネームが保存されてい.", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        TextView textView2 = (TextView) view.findViewById((R.id.textView5));
        if(tkos_language == 0)
            textView2.setText(tkos_score + "점");
        else if(tkos_language == 1)
            textView2.setText(tkos_score + "point");
        else if(tkos_language == 2)
            textView2.setText(tkos_score + "スコア");

        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(tkos_effect == 1)
                    sound.play(soundId, 0.1F, 0.1F,  0,  0,  1.0F);
                shareFacebook(tkos_nickname, tkos_score);
            }
        });

        Button button2 = (Button) view.findViewById(R.id.button2);
        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(tkos_effect == 1)
                    sound.play(soundId, 0.1F, 0.1F,  0,  0,  1.0F);
                shareKakao(tkos_nickname, tkos_score);
            }
        });

        Button button3 = (Button) view.findViewById(R.id.button3);
        button3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(tkos_effect == 1)
                    sound.play(soundId, 0.1F, 0.1F,  0,  0,  1.0F); //사운드 플레이(재생)
                getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });
        TextView textview = (TextView) view.findViewById(R.id.textView5);
        return view;
    }

    public void shareFacebook(String tkos_nickname, int tkos_score)
    {
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            if(tkos_language == 0)
            {  ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setQuote(tkos_nickname + "님의 점수는 " + tkos_score + "점이네요.")
                        .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                        .build();
            shareDialog.show(linkContent);

            MessageDialog.show(this, linkContent);}
            else if(tkos_language == 1)
            {  ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setQuote(tkos_nickname + "'s score is " + tkos_score + "point")
                        .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                        .build();
            shareDialog.show(linkContent);

            MessageDialog.show(this, linkContent);}
            else if(tkos_language == 2) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setQuote(tkos_nickname + "さんのスコアは " + tkos_score + "の点がね.")
                        .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                        .build();
                shareDialog.show(linkContent);

                MessageDialog.show(this, linkContent);
            }
        }
    }

    public void shareKakao(String tkos_nickname, int tkos_score)
    {
        if(tkos_language == 0)
        {
            TextTemplate params = TextTemplate.newBuilder(tkos_nickname + "님의 점수는 " + tkos_score + "점이네요.", LinkObject.newBuilder().setWebUrl("https://developers.kakao.com").setMobileWebUrl("https://developers.kakao.com").build()).setButtonTitle("나도할래요!").build();
            Map<String, String> serverCallbackArgs = new HashMap<String, String>();
            serverCallbackArgs.put("user_id", "${current_user_id}");
            serverCallbackArgs.put("product_id", "${shared_product_id}");
            KakaoLinkService.getInstance().sendDefault(getActivity(), params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    Logger.e(errorResult.toString());
                }

                @Override
                public void onSuccess(KakaoLinkResponse result) {}
            });
        }
        else if(tkos_language == 1)
        {
            TextTemplate params = TextTemplate.newBuilder(tkos_nickname + "'s score is " + tkos_score + "point.", LinkObject.newBuilder().setWebUrl("https://developers.kakao.com").setMobileWebUrl("https://developers.kakao.com").build()).setButtonTitle("나도할래요!").build();
            Map<String, String> serverCallbackArgs = new HashMap<String, String>();
            serverCallbackArgs.put("user_id", "${current_user_id}");
            serverCallbackArgs.put("product_id", "${shared_product_id}");
            KakaoLinkService.getInstance().sendDefault(getActivity(), params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    Logger.e(errorResult.toString());
                }

                @Override
                public void onSuccess(KakaoLinkResponse result) {}
            });
        }
        else if(tkos_language == 2)
        {
            TextTemplate params = TextTemplate.newBuilder(tkos_nickname + "さんのスコアは " + tkos_score + "の点がね.", LinkObject.newBuilder().setWebUrl("https://developers.kakao.com").setMobileWebUrl("https://developers.kakao.com").build()).setButtonTitle("나도할래요!").build();
            Map<String, String> serverCallbackArgs = new HashMap<String, String>();
            serverCallbackArgs.put("user_id", "${current_user_id}");
            serverCallbackArgs.put("product_id", "${shared_product_id}");
            KakaoLinkService.getInstance().sendDefault(getActivity(), params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    Logger.e(errorResult.toString());
                }

                @Override
                public void onSuccess(KakaoLinkResponse result) {}
            });
        }
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri fileUri = data.getData();
        ContentResolver resolver = getActivity().getContentResolver();
        final DatabaseActivity dbactivity = new DatabaseActivity(getContext(), "TKOS.db", null, 1);
        SavedID sid = (SavedID) getActivity().getApplication();
        final String name = sid.getID();

        try {
            InputStream instream = resolver.openInputStream(fileUri);
            Bitmap imgBitmap = BitmapFactory.decodeStream(instream);
            imageView.setImageBitmap(imgBitmap);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            dbactivity.setProfile(name, stream.toByteArray());
            instream.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}