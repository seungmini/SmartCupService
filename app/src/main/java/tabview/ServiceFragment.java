package tabview;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lageder.main.MapsActivity;
import com.example.lageder.main.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import com.example.lageder.main.MainActivity;

import org.w3c.dom.Text;

public class ServiceFragment extends Fragment {
    private ImageView drink_imageview,recommend_imageview,tracker_imageview;
    private TextView textview_recommend, textview_realtime,textview_tracker;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_service, container, false);


        String name_path=getActivity().getFilesDir().getAbsolutePath()+"/name.txt";
        File name_file = new File(name_path);
        String temp_name = "사용자";

            try {
                FileInputStream fis = new FileInputStream(name_file);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                temp_name = new String(buffer);

                fis.close();
            } catch (IOException e) {
                Log.e("File", "에러=" + e);
            }


        final String name = temp_name;
        drink_imageview = (ImageView)v.findViewById(R.id.drink_image);
        drink_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent drink_popup_intent = new Intent(getContext(), DrinkPopupActivity.class);
                drink_popup_intent.putExtra("name",name);
                startActivity(drink_popup_intent);
            }
        });
        recommend_imageview = (ImageView) v.findViewById(R.id.imageButton);
        recommend_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity myActivity = (MainActivity) getActivity();
                myActivity.checkPermission();
            }
        });
        tracker_imageview = (ImageView) v.findViewById(R.id.gps_tracker);
        tracker_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity myActivity = (MainActivity) getActivity();
                Intent intent = new Intent(myActivity, MapsActivity.class);
                startActivity(intent);
            }
        });

        Typeface font_gabia = Typeface.createFromAsset(getActivity().getAssets(), "gabia_solmee.ttf");

        textview_recommend = (TextView)v.findViewById(R.id.recommend);
        textview_realtime = (TextView)v.findViewById(R.id.real_time);
        textview_tracker = (TextView)v.findViewById(R.id.tracker);
        textview_realtime.setTypeface(font_gabia);
        textview_recommend.setTypeface(font_gabia);
        textview_tracker.setTypeface(font_gabia);

        return v;
    }
}
