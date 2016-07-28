package com.example.lageder.touchuiexample;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LeeSeungMin
 */

public class CustomAdapter extends BaseAdapter {

    private ArrayList<ListData> listview_data = new ArrayList<ListData>();
    Map<Integer, Integer> name_map = new HashMap<Integer, Integer>();
    Map<Integer, Integer> cc_map = new HashMap<Integer, Integer>();
    int list_type = -1;
    LayoutInflater inflater;
    // 생성자
    public CustomAdapter(Activity input) {
       inflater = (LayoutInflater) input.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        View vi = convertView;
        CustomHolder holder = null;


        // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
        if ( convertView == null ) {
            holder = new CustomHolder();


            vi= inflater.inflate(R.layout.feedback_list_layout, parent, false);

            holder.drink_textview = (TextView) vi.findViewById(R.id.feedback_text);
            holder.drink_name_spinner = (Spinner)vi.findViewById(R.id.drink_spinner);
            holder.drink_cc_spinner = (Spinner)vi.findViewById(R.id.cc_spinner);


            if(list_type == 1){

            }
            vi.setTag(holder);
        }
        else{
            holder = (CustomHolder) convertView.getTag();
        }

        holder.drink_textview.setText(listview_data.get(position).drink_type);
        holder.drink_name_spinner.setSelection(listview_data.get(position).drink_name);
        holder.drink_cc_spinner.setSelection(listview_data.get(position).drink_cc);

       if(listview_data.get(position).drink_type_int== 0){
            String arr[] = context.getResources().getStringArray(R.array.soju);
            ArrayAdapter<String> list = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,arr);
           holder.drink_name_spinner.setAdapter(list);
        }
        else if(listview_data.get(position).drink_type_int == 1){
            String arr[] = context.getResources().getStringArray(R.array.macju);
            ArrayAdapter<String> list = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,arr);
           holder.drink_name_spinner.setAdapter(list);
        }
        else{
            String arr[] = context.getResources().getStringArray(R.array.macguli);
            ArrayAdapter<String> list = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,arr);
           holder.drink_name_spinner.setAdapter(list);
        }
      //  ArrayAdapter<String> list = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,arr);
       // holder.drink_name_spinner.setAdapter(list);


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
        return vi;
    }


    // 외부에서 아이템 추가 요청 시 사용
    public void add(int type) {
        ListData new_data = new ListData();
        if(type == 0){
            new_data.drink_type = "소주　";
            new_data.drink_type_int = type;
        }
        else if (type == 1){
            new_data.drink_type = "맥주　";
            new_data.drink_type_int = type;
        }
        else if(type == 2){
            new_data.drink_type = "막걸리";
            new_data.drink_type_int = type;
        }

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
