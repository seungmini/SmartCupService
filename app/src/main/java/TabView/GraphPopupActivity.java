package tabview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.example.lageder.main.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;

import java.util.ArrayList;

import datas.YlabelValueFormatter;
import datas.SCSDBManager;
import datas.YValueFormatter;

/**
 * Created by LeeSeungMin
 */

public class GraphPopupActivity extends Activity {
    private BarChart chart_popup;
    Typeface font_gabia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;

        getWindow().setAttributes(layoutParams);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_graph_popup);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);

        font_gabia = Typeface.createFromAsset(getAssets(), "gabia_solmee.ttf");

        //크기 조절
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int) (display.getWidth() * 0.9); //Display 사이즈의 80%
        int height = (int) (display.getHeight() * 0.5);  //Display 사이즈의 50%
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        Intent intent = getIntent();
        int chart_number = intent.getIntExtra("chart_number",-1);
        //   Log.e("?? : ", Integer.toString(chart_number));
        //this.setTitle(Integer.toString(chart_number) + "주 전");
        //this.setTitleColor(Color.rgb(255,255,255));//4EBBD1

        ////// 차트 추가
        chart_popup = (BarChart)findViewById(R.id.popup_chart);
        chart_popup.setDescription("");
        makeGraphPopup(chart_number);

    }

    public void makeGraphPopup(int chart_number){
        SCSDBManager db = new SCSDBManager(this, "Sample.db", null, 1);

        BarData data = new BarData(getXLabel_week(), db.getDataSet_beforeweek(chart_number));
        data.setValueFormatter(new YValueFormatter());
        data.setValueTypeface(font_gabia);
        chart_popup.setData(data);

        //시작값 0 설정
        YAxis yAxis_left = chart_popup.getAxisLeft();
        YAxis yAxis_right = chart_popup.getAxisRight();
        yAxis_left.setAxisMinValue(0f);
        //yAxis_left.setDrawGridLines(false);

        yAxis_right.setEnabled(false);
        yAxis_right.setTypeface(font_gabia);

        yAxis_left.setValueFormatter(new YlabelValueFormatter());

        XAxis xais = chart_popup.getXAxis();
        xais.setTypeface(font_gabia);
        //xais.setDrawGridLines(false);
        xais.setTextSize(14);
        xais.setPosition(XAxis.XAxisPosition.BOTTOM);


        chart_popup.setTouchEnabled(false);
        chart_popup.animateY(1500);
    }


    public ArrayList<String> getXLabel_week(){
        ArrayList<String> x_labels = new ArrayList<String>();

        x_labels.add("일");
        x_labels.add("월");
        x_labels.add("화");
        x_labels.add("수");
        x_labels.add("목");
        x_labels.add("금");
        x_labels.add("토");
        return x_labels;
    }

}