package com.example.lageder.touchuiexample;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lylc.widget.circularprogressbar.CircularProgressBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ProfileFragment extends Fragment {

    CircularProgressBar c1;
    public static TextView name_textview,soju_textview,makg_textview,beer_textview;
    public static ImageView profile_imagview;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        SojuDBManager db = new SojuDBManager(getActivity(), "Sample.db", null, 1);

        c1 = (CircularProgressBar) v.findViewById(R.id.circularprogressbar1);
        int current_value = 95;
        c1.animateProgressTo(0, current_value, new CircularProgressBar.ProgressAnimationListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationFinish() {
                c1.setTitle("286ml");
            }

            @Override
            public void onAnimationProgress(int progress) {
                c1.setTitle(progress + "%");
            }
        });

        name_textview = (TextView) v.findViewById(R.id.name_textview);
        soju_textview = (TextView)v.findViewById(R.id.most_soju);
        beer_textview = (TextView)v.findViewById(R.id.most_beer);
        makg_textview = (TextView)v.findViewById(R.id.most_makg);

        profile_imagview = (ImageView) v.findViewById(R.id.profile_imageview);



        String name_path = getActivity().getApplicationContext().getFilesDir().getAbsolutePath() + "/name.txt";
        String image_path = getActivity().getApplicationContext().getFilesDir().getAbsolutePath() + "/profile.jpg";
        File name_file = new File(name_path);
        File image_file = new File(image_path);

        if (name_file.exists() == true) {

            Bitmap bmap = BitmapFactory.decodeFile(image_file.getAbsolutePath());
            profile_imagview.setImageBitmap(bmap);
            try {
                FileInputStream fis = new FileInputStream(name_file);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                String name = new String(buffer);
                name_textview.setText("이름 : " + name);
                fis.close();
            } catch (IOException e) {
                Log.e("File", "에러=" + e);
            }

        }

        soju_textview.setText("소주 : " +db.getMostSoju());
        beer_textview.setText("맥주 : " +db.getMostBeer());
        makg_textview.setText("막걸리 : " +db.getMostMakg());

        db.getMostTime();
        return v;
    }


}
