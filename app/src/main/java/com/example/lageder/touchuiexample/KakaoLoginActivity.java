package com.example.lageder.touchuiexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Display;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.KakaoParameterException;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class KakaoLoginActivity extends Activity {
    SessionCallback callback;
    private KakaoLink kakaoLink;
    private KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;

    Bitmap mBmp = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_login);


        try {
            kakaoLink = KakaoLink.getKakaoLink(this);
            kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
        } catch (KakaoParameterException e) {
            e.getMessage();
        }
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAffinity(this);
        System.runFinalizersOnExit(true);
        System.exit(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //간편로그인시 호출 ,없으면 간편로그인시 로그인 성공화면으로 넘어가지 않음
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {

            UserManagement.requestMe(new MeResponseCallback() {

                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;
                    Logger.d(message);

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        finish();
                    } else {
                        //redirectMainActivity();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }
                @Override
                public void onNotSignedUp() {
                }

                @Override
                public void onSuccess(UserProfile userProfile) {
                    //로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다
                    ProfileFragment.name_textview.setText("이름 : " + userProfile.getNickname());

                    try{

                        FileOutputStream fos = openFileOutput("name.txt",
                                Context.MODE_PRIVATE);
                        String str = userProfile.getNickname();
                        fos.write(str.getBytes()); // String을 byte배열로 변환후 저장
                        fos.close();

                    }catch(IOException e){

                    }
                    String url1 = userProfile.getProfileImagePath();
                    String profile = "profile.jpg";
                    new HttpReqTask().execute(url1, profile);

                    finish();
                }
            });

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            // 세션 연결이 실패했을때
        }
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class HttpReqTask extends AsyncTask<String,String,String> {
        @Override // 쓰레드 주업무를 수행하는 함수
        protected String doInBackground(String... arg) {
            boolean result = false;
            if (arg.length == 1)
                // 서버에서 전달 받은 데이터를 Bitmap 이미지에 저장
                result = loadWebImage(arg[0]);
            else {
                // 서버에서 다운로드 한 데이터를 파일로 저장
                result = downloadFile(arg[0], arg[1]);
                if (result) {
                    String image_path = getFilesDir(). getAbsolutePath() + "/profile.jpg";
                    File image_file = new File(image_path);
                    // 파일을 로딩해서 Bitmap 객체로 생성
                    mBmp = BitmapFactory.decodeFile(image_file.getAbsolutePath());
                }
            }

            if (result)
                return "True";
            return "";
        }
        // 쓰레드의 업무가 끝났을 때 결과를 처리하는 함수
        protected void onPostExecute(String result) {
            if( result.length() > 0 )
                // 서버에서 다운받은 Bitmap 이미지를 ImageView 에 표시
                ProfileFragment.profile_imagview.setImageBitmap(mBmp);
        }
    }

    public boolean loadWebImage(String strUrl) {
        try {
            // 스트림 데이터를 Bitmap 에 저장
            InputStream is = new URL(strUrl).openStream();
            mBmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            Log.d("tag", "Image Stream error.");
            return false;
        }
        return true;
    }

    // 서버에서 다운로드 한 데이터를 파일로 저장
    boolean downloadFile(String strUrl, String fileName) {
        try {
            URL url = new URL(strUrl);
            // 서버와 접속하는 클라이언트 객체 생성
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 입력 스트림을 구한다
            InputStream is = conn.getInputStream();
            // 파일 저장 스트림을 생성
            FileOutputStream fos = openFileOutput(fileName, 0);
            // 입력 스트림을 파일로 저장
            byte[] buf = new byte[1024];
            int count;
            while ((count = is.read(buf)) > 0) {
                fos.write(buf, 0, count);
            }
            // 접속 해제
            conn.disconnect();
            // 파일을 닫는다

            fos.close();
        } catch (Exception e) {
            Log.d("tag", "Image download error.");
            return false;
        }
        return true;
    }


}
