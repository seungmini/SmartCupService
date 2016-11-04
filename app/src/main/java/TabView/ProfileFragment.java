package tabview;


import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lageder.main.MainActivity;
import com.example.lageder.main.R;
import datas.SCSDBManager;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class ProfileFragment extends Fragment {
    public static TextView name_textview,soju_textview,makg_textview,beer_textview, time_textview, time_result_textview, day_textview, day_result_textview, brand_textview,recommend_textview;
    public static ImageView profile_imagview;
    public static CircularImageView circular_imageview;
    public static ImageButton image_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        final SCSDBManager db = new SCSDBManager(getActivity(), "s2.db", null, 1);

        name_textview = (TextView) v.findViewById(R.id.name_textview);
        soju_textview = (TextView)v.findViewById(R.id.most_soju);
        beer_textview = (TextView)v.findViewById(R.id.most_beer);
        makg_textview = (TextView)v.findViewById(R.id.most_makg);
        recommend_textview = (TextView)v.findViewById(R.id.recommend_tv);


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

        if(!db.checkDC()){
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
            View mView = layoutInflaterAndroid.inflate(R.layout.insert_dc_dialog, null);

            AlertDialog.Builder sayWindows = new AlertDialog.Builder(getContext());
            sayWindows.setView(mView);
            sayWindows.setCancelable(false);

            final EditText edittext_input = (EditText) mView.findViewById(R.id.userInputDialog);
            final Button button_ok = (Button)mView.findViewById(R.id.dialog_ok);
            final TextView textview_title = (TextView)mView.findViewById(R.id.dialogTitle);

            Typeface font_gabia = Typeface.createFromAsset(getActivity().getAssets(), "gabia_solmee.ttf");
            edittext_input.setTypeface(font_gabia);
            button_ok.setTypeface(font_gabia);
            textview_title.setTypeface(font_gabia);
            recommend_textview.setTypeface(font_gabia);

            edittext_input.setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);

            final AlertDialog mAlertDialog = sayWindows.create();
            mAlertDialog.setCanceledOnTouchOutside(false);

            mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface dialog) {



                    button_ok.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            String value = edittext_input.getText().toString();
                            float float_dc = Float.parseFloat(value);
                            int int_dc = (int)(float_dc * 60);
                            db.executeQuery("insert into DC values("+int_dc+");");

                            mAlertDialog.dismiss();
                        }
                    });
                }
            });
           mAlertDialog.show();


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

        image_btn = (ImageButton)v.findViewById(R.id.imageButton);
        image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = "";
                MainActivity myActivity = (MainActivity) getActivity();
                myActivity.checkPermission();
            }
        });

        return v;
    }


}
