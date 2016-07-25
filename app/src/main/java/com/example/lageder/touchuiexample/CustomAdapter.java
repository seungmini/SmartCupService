package com.example.lageder.touchuiexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LeeSeungMin on 2016-07-25.
 */

public class CustomAdapter extends BaseAdapter {

    private ArrayList<ListData> listview_data = new ArrayList<ListData>();
    Map<Integer, Integer> name_map = new HashMap<Integer, Integer>();
    Map<Integer, Integer> cc_map = new HashMap<Integer, Integer>();

    // 생성자
    public CustomAdapter() {
    }

    // 현재 아이템의 수를 리턴
    @Override
    public int getCount() {
        return listview_data.size();
    }

    // 현재 아이템의 오브젝트를 리턴, Object를 상황에 맞게 변경하거나 리턴받은 오브젝트를 캐스팅해서 사용
    @Override
    public Object getItem(int position) {
        return listview_data.get(position);
    }

    // 아이템 position의 ID 값 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 출력 될 아이템 관리
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();

        CustomHolder holder = null;

        // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
        if ( convertView == null ) {
            holder = new CustomHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.feedback_list_layout, parent, false);

            holder.drink_textview = (TextView) convertView.findViewById(R.id.name_textview);
            holder.drink_name_spinner = (Spinner)convertView.findViewById(R.id.drink_spinner);
            holder.drink_cc_spinner = (Spinner)convertView.findViewById(R.id.cc_spinner);

            convertView.setTag(holder);
        }
        else{
            holder = (CustomHolder) convertView.getTag();
        }

        holder.drink_textview.setText(listview_data.get(position).drink_type);
        holder.drink_name_spinner.setSelection(listview_data.get(position).drink_name);
        holder.drink_cc_spinner.setSelection(listview_data.get(position).drink_cc);


        ////////////////////////////////////////////////////////////////////////////
        //spinner 값 저장

        if ( name_map.get( position ) != null ) {
            holder.drink_name_spinner.setSelection( name_map.get( position ) );
        }
        holder.drink_name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                name_map.put( position, arg2 );
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        if ( cc_map.get( position ) != null ) {
            holder.drink_cc_spinner.setSelection( cc_map.get( position ) );
        }
        holder.drink_cc_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                cc_map.put( position, arg2 );
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        ////////////////////////////////////////////////////////////////////////////
        return convertView;
    }


    // 외부에서 아이템 추가 요청 시 사용
    public void add() {
        ListData new_data = new ListData();
        new_data.drink_type = "소주";
        listview_data.add(new_data);
    }

    // 외부에서 아이템 삭제 요청 시 사용
    public void remove(int _position) {
        listview_data.remove(_position);
    }

    private class CustomHolder{
        TextView drink_textview;
        Spinner drink_name_spinner, drink_cc_spinner;
    }
}
