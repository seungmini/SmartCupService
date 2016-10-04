package tabview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lageder.main.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import datas.ListData;

/**
 * Created by LeeSeungMin
 */

public class CustomAdapter extends BaseAdapter {

    public ArrayList<ListData> listview_data = new ArrayList<ListData>();
    private LayoutInflater inflater;
    public Map<Integer, Integer> drink_name_hashmap = new HashMap<Integer, Integer>();
    public Map<Integer, Integer> drink_number_hashmap = new HashMap<Integer, Integer>();

    // 생성자
    public CustomAdapter(Activity input){
       inflater = (LayoutInflater) input.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public int getDrinkName(int position){
        if(drink_name_hashmap.get(position) == null){
            return 0;
        }
        return drink_name_hashmap.get(position);
    }
    public int getDrinkNumber(int position){
        if(drink_number_hashmap.get(position) == null){
            return 0;
        }
        return drink_number_hashmap.get(position);
    }
    // 현재 아이템의 수를 리턴
    @Override
    public int getCount(){
        return listview_data.size();
    }

    // 현재 아이템의 오브젝트를 리턴, Object를 상황에 맞게 변경하거나 리턴받은 오브젝트를 캐스팅해서 사용
    @Override
    public Object getItem(int position){
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
            holder.drink_number_spinner = (Spinner)vi.findViewById(R.id.cc_spinner);

            vi.setTag(holder);
        }
        else{
            holder = (CustomHolder) convertView.getTag();
        }

        holder.drink_textview.setText(listview_data.get(position).drink_type);
        holder.drink_name_spinner.setSelection(listview_data.get(position).drink_name);
        holder.drink_number_spinner.setSelection(listview_data.get(position).drink_number);

       if(listview_data.get(position).drink_type_int== 0){
            String arr[] = context.getResources().getStringArray(R.array.soju_list);
            ArrayAdapter<String> list = new ArrayAdapter<String>(context,R.layout.feedback_spinner_item,arr);
            holder.drink_name_spinner.setAdapter(list);
        }
        else if(listview_data.get(position).drink_type_int == 1){
            String arr[] = context.getResources().getStringArray(R.array.beer_list);
            ArrayAdapter<String> list = new ArrayAdapter<String>(context,R.layout.feedback_spinner_item,arr);
            holder.drink_name_spinner.setAdapter(list);
        }
        else{
            String arr[] = context.getResources().getStringArray(R.array.makgeolli_list);
            ArrayAdapter<String> list = new ArrayAdapter<String>(context,R.layout.feedback_spinner_item,arr);
            holder.drink_name_spinner.setAdapter(list);
        }

        String arr[] = context.getResources().getStringArray(R.array.number_of_drink);
        ArrayAdapter<String> list = new ArrayAdapter<String>(context,R.layout.feedback_spinner_item,arr);
        holder.drink_number_spinner.setAdapter(list);

        ImageView buttonLA = (ImageView) vi.findViewById(R.id.image_button);
        buttonLA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.e("what select", " "+position );
                remove(position);
                notifyDataSetChanged();
            }
        });
        //spinner 값 저장
        if ( drink_name_hashmap.get( position ) != null ) {
            holder.drink_name_spinner.setSelection( drink_name_hashmap.get( position ) );
        }
        holder.drink_name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                drink_name_hashmap.put( position, arg2 );
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        if ( drink_number_hashmap.get( position ) != null ) {
            holder.drink_number_spinner.setSelection( drink_number_hashmap.get( position ) );
        }
        holder.drink_number_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                drink_number_hashmap.put( position, arg2 );
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });




        return vi;
    }

    // 외부에서 아이템 추가 요청 시 사용
    public void add(int type) {
        ListData new_data = new ListData();
        if(type == 0){
            new_data.drink_type = "소";
        }
        else if (type == 1){
            new_data.drink_type = "맥";
        }
        else if(type == 2){
            new_data.drink_type = "막";
        }
        new_data.drink_type_int = type;

        listview_data.add(new_data);
    }

    // 외부에서 아이템 삭제 요청 시 사용
    public void remove(int _position) {

        for(int i = _position ; i < drink_number_hashmap.size()-1;i++){
            drink_name_hashmap.put(i, drink_name_hashmap.get(i+1));
            drink_number_hashmap.put(i, drink_number_hashmap.get(i+1));
        }

        drink_number_hashmap.put(drink_number_hashmap.size()-1,0);
        drink_name_hashmap.put(drink_number_hashmap.size()-1,0);

        listview_data.remove(_position);
    }

    private class CustomHolder{
        TextView drink_textview;
        Spinner drink_name_spinner, drink_number_spinner;
    }
}
