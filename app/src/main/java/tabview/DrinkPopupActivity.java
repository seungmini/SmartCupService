package tabview;

import android.app.Activity;

import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lageder.main.R;

import datas.SCSDBManager;

public class DrinkPopupActivity extends Activity {
    Typeface font_gabia,font_kover;
    TextView textview_pec,textview_sen,textview_title;
    ImageView imageview_cup;
    String user_name;
    SCSDBManager db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;

        getWindow().setAttributes(layoutParams);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_drink_popup);

        db = new SCSDBManager(this, "abc12345.db", null, 1);


        font_gabia = Typeface.createFromAsset(getAssets(), "gabia_solmee.ttf");

        textview_pec = (TextView)findViewById(R.id.pec_textview);
        textview_pec.setTypeface(font_gabia);
        textview_title = (TextView)findViewById(R.id.pec_title_textview);
        textview_title.setTypeface(font_gabia);

        imageview_cup = (ImageView)findViewById(R.id.cup_imageview);

        textview_sen = (TextView)findViewById(R.id.sentence_textview);

        //크기 조절
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int) (display.getWidth() * 0.9); //Display 사이즈의 80%
        int height = (int) (display.getHeight() * 0.8);  //Display 사이즈의 50%
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        Intent intent = getIntent();
        user_name = intent.getStringExtra("name");

        setCupImage();
        setCupPectage();
        setSentence();
    }
    public void setCupImage(){
        int dc = db.getDC();
        int alchol = db.get_current_DC();
        int pectage = alchol * 100 / dc ;
        if(pectage <12){
            imageview_cup.setImageResource(R.drawable.water_0);
        }
        else if(pectage >= 12 && pectage < 37){
            imageview_cup.setImageResource(R.drawable.water_25);
        }
        else if(pectage >= 37 && pectage < 62){
            imageview_cup.setImageResource(R.drawable.water_50);
        }
        else if(pectage >= 62 && pectage < 87){
            imageview_cup.setImageResource(R.drawable.water_75);
        }
        else{
            imageview_cup.setImageResource(R.drawable.water_99);
        }
    }
    public void setCupPectage(){
        int dc = db.getDC();
        int alchol = db.get_current_DC();
        int pectage = alchol * 100 / dc ;
        textview_pec.setText(pectage + "%");
        if(pectage >= 80){
            textview_pec.setTextColor(Color.rgb(255,0,0));
        }
    }
    public void setSentence(){
        int dc = db.getDC();
        int alchol = db.get_current_DC();
        int pectage = alchol * 100 / dc ;
        font_kover = Typeface.createFromAsset(getAssets(), "koverwatch.ttf");

        String name = "<font color='#FF0000'>"+user_name+"</font>";
        String liver = "<font color='#FF0000'>간</font>";
        String number = "<font color='#FF0000'>(+"+pectage+")</font>";

        textview_sen.setText(Html.fromHtml(name +"의 " + liver +" 파괴"+number));
        textview_sen.setTypeface(font_kover);
    }
}
