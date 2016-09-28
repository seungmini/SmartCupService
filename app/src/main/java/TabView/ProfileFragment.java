package tabview;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lageder.main.R;
import datas.SCSDBManager;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ProfileFragment extends Fragment {


    public static TextView name_textview,soju_textview,makg_textview,beer_textview, time_textview, time_result_textview, day_textview, day_result_textview, brand_textview;
    public static ImageView profile_imagview;
    public static CircularImageView circular_imageview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        SCSDBManager db = new SCSDBManager(getActivity(), "abcde.db", null, 1);


        name_textview = (TextView) v.findViewById(R.id.name_textview);
        soju_textview = (TextView)v.findViewById(R.id.most_soju);
        beer_textview = (TextView)v.findViewById(R.id.most_beer);
        makg_textview = (TextView)v.findViewById(R.id.most_makg);


        time_textview = (TextView)v.findViewById(R.id.lastest_time);
        time_result_textview = (TextView)v.findViewById(R.id.lastest_time_result);
        time_result_textview.setText(db.getLatestTime());

        day_textview = (TextView)v.findViewById(R.id.most_day);
        day_result_textview = (TextView)v.findViewById(R.id.most_day_result);
        day_result_textview.setText(db.getMostTime());

        brand_textview = (TextView)v.findViewById(R.id.brand);
        // ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Profile");

        circular_imageview = (CircularImageView)v.findViewById(R.id.profile_imageview);

        circular_imageview.setBorderColor(Color.rgb(0,0,0));
        circular_imageview.setBorderWidth(0);

        String name_path = getActivity().getApplicationContext().getFilesDir().getAbsolutePath() + "/name.txt";
        String image_path = getActivity().getApplicationContext().getFilesDir().getAbsolutePath() + "/profile.jpg";
        File name_file = new File(name_path);
        File image_file = new File(image_path);

        if (name_file.exists() == true) {

            Bitmap bmap = BitmapFactory.decodeFile(image_file.getAbsolutePath());
            circular_imageview.setImageBitmap(bmap);
            try {
                FileInputStream fis = new FileInputStream(name_file);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                String name = new String(buffer);
                name_textview.setText(name);
                fis.close();
            } catch (IOException e) {
                Log.e("File", "에러=" + e);
            }

        }

        soju_textview.setText(db.getMostSoju());
        beer_textview.setText(db.getMostBeer());
        makg_textview.setText(db.getMostMakg());

        db.getMostTime();

        Typeface font_gabia = Typeface.createFromAsset(getActivity().getAssets(), "gabia_solmee.ttf");

        name_textview.setTypeface(font_gabia);
        soju_textview.setTypeface(font_gabia);
        beer_textview.setTypeface(font_gabia);
        makg_textview.setTypeface(font_gabia);

        time_textview.setTypeface(font_gabia);
        time_result_textview.setTypeface(font_gabia);
        day_textview.setTypeface(font_gabia);
        day_result_textview.setTypeface(font_gabia);
        brand_textview.setTypeface(font_gabia);


        return v;
    }


}
