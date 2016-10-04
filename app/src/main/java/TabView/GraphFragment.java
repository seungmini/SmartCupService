package tabview;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.lageder.main.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

import datas.SCSDBManager;
import datas.YValueFormatter;
import datas.YlabelValueFormatter;


/**
 * Created by LeeSeungMin
 */

public class GraphFragment extends Fragment {

    Activity activity;

    private BarChart chart;
    private Spinner spinner_date;
    private Typeface font_gabia;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_graph, container, false);

        activity = getActivity();

        font_gabia = Typeface.createFromAsset(getActivity().getAssets(), "gabia_solmee.ttf");

        spinner_date = (Spinner)v.findViewById(R.id.date_spinner);
        makeSpinnerList();
        //spinner_date.setOnItemClickListener();
        spinner_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if(arg2 == 0){
                    makeWeekGraph();
                }
                else{
                    makeMonthGraph();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        ////// 차트 추가
        chart = (BarChart)v.findViewById(R.id.chart);
        chart.setDescription("");
        makeWeekGraph();

        return v;
    }


    public void makeSpinnerList(){
        ArrayList<String> datelist = new ArrayList<>();
        datelist.add("최근 한주 GRAPH");
        datelist.add("최근 한달 GRAPH");
        ArrayAdapter<String> list = new ArrayAdapter<String>(getContext(),R.layout.spinner_item,datelist);
        spinner_date.setAdapter(list);
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

    public ArrayList<String> getXLabel_month(){
        ArrayList<String> x_labels = new ArrayList<String>();
        x_labels.add("1주전");
        x_labels.add("2주전");
        x_labels.add("3주전");
        x_labels.add("4주전");
        return x_labels;
    }

    public void makeWeekGraph(){
        SCSDBManager db = new SCSDBManager(getActivity(), "abcde.db", null, 1);

        BarData data = new BarData(getXLabel_week(), db.getDataSet_thisweek());
        data.setValueFormatter(new YValueFormatter());
        data.setValueTypeface(font_gabia);
        chart.setData(data);

        //시작값 0 설정
        YAxis yAxis_left = chart.getAxisLeft();
        YAxis yAxis_right = chart.getAxisRight();
        yAxis_left.setAxisMinValue(0f);
        yAxis_right.setAxisMinValue(0f);


        yAxis_right.setTypeface(font_gabia);
        yAxis_left.setValueFormatter(new YlabelValueFormatter());

        yAxis_right.setEnabled(false);

        XAxis xais = chart.getXAxis();
        xais.setTypeface(font_gabia);
        xais.setTextSize(14);
        xais.setPosition(XAxis.XAxisPosition.BOTTOM);


        chart.setTouchEnabled(false);

        chart.animateY(1500);
    }
    public void makeMonthGraph(){

        SCSDBManager db = new SCSDBManager(getActivity(), "Sample.db", null, 1);
        Typeface font_gabia = Typeface.createFromAsset(getActivity().getAssets(), "gabia_solmee.ttf");

        BarData data = new BarData(getXLabel_month(), db.getDataSet_month());
        data.setValueFormatter(new YValueFormatter());
        data.setValueTypeface(font_gabia);
        data.setValueTextSize(11);
        chart.setData(data);
        chart.setTouchEnabled(true);


        //touch 빼고 다 끔
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        chart.setDragDecelerationEnabled(false);
        chart.setHighlightPerDragEnabled(false);
        chart.setHighlightPerDragEnabled(false);
        //////////////////


        //시작값 0 설정
        YAxis yAxis_left = chart.getAxisLeft();
        yAxis_left.setAxisMinValue(0f);
        //yAxis_left.setDrawGridLines(false);
        yAxis_left.setValueFormatter(new YlabelValueFormatter());

        YAxis yAxis_right = chart.getAxisRight();
        yAxis_right.setTypeface(font_gabia);

        yAxis_right.setEnabled(false);


        XAxis xais = chart.getXAxis();
        //xais.setDrawGridLines(false);
        xais.setTypeface(font_gabia);
        xais.setTextSize(14);
        xais.setPosition(XAxis.XAxisPosition.BOTTOM);



        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                Intent graph_popup_intent;

                switch(e.getXIndex()){
                    case 0:
                        graph_popup_intent = new Intent(getContext(), GraphPopupActivity.class);
                        graph_popup_intent.putExtra("chart_number",1);
                        startActivity(graph_popup_intent);
                        break;
                    case 1:
                        graph_popup_intent = new Intent(getContext(), GraphPopupActivity.class);
                        graph_popup_intent.putExtra("chart_number",2);
                        startActivity(graph_popup_intent);
                        break;
                    case 2:
                        graph_popup_intent = new Intent(getContext(), GraphPopupActivity.class);
                        graph_popup_intent.putExtra("chart_number",3);
                        startActivity(graph_popup_intent);
                        break;
                    case 3:
                        //graph_popup_intent = new Intent(getContext(), GraphPopupActivity.class);
                        graph_popup_intent = new Intent(getContext(), FeedBackActivity.class);
                        //graph_popup_intent.putExtra("chart_number",4);
                        startActivity(graph_popup_intent);
                        break;

                }

            }

            @Override
            public void onNothingSelected() {
                // do nothing
            }
        });
        chart.animateY(1500);
    }


}
