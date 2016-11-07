package tabview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lageder.main.R;

import java.util.ArrayList;
import java.util.Iterator;

import datas.SCSDBManager;

/**
 * Created by Lageder on 2016-11-03.
 */

public class NumberAdapter extends BaseAdapter {
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public ListViewItem getItem(int i) {
        return listViewItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.number_item, parent, false);
        }

        TextView phoneNum = (TextView)convertView.findViewById(R.id.phone_num);
        TextView user_name = (TextView)convertView.findViewById(R.id.user_name);

        ListViewItem listViewItem = listViewItemList.get(position);

        phoneNum.setText(listViewItem.getPhone_num());
        user_name.setText(listViewItem.getUser_name());

        final String text = listViewItemList.get(position).getPhone_num();
        ImageView btn_delete = (ImageView) convertView.findViewById(R.id.btn_delete);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SCSDBManager db = new SCSDBManager(context, "abc12345.db", null, 1);
                String query_delete = "DELETE FROM PHONE WHERE phone_num=\""+listViewItemList.get(pos).getPhone_num() + "\" and user_name=\""+listViewItemList.get(pos).getUser_name()+"\"";
                db.executeQuery(query_delete);
                deleteItem(listViewItemList.get(pos).getPhone_num(),listViewItemList.get(pos).getUser_name());
                Toast.makeText(context, "Delete Complete", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public void addItem(String num, String name) {
        ListViewItem item = new ListViewItem();

        item.setPhone_num(num);
        item.setUser_name(name);
        listViewItemList.add(item);
    }

    public void deleteItem(String num, String name) {
        for(Iterator<ListViewItem> iterator = listViewItemList.iterator() ; iterator.hasNext() ;) {
            ListViewItem value = iterator.next();
            if(value.getUser_name().compareTo(name) == 0 &&
                    value.getPhone_num().compareTo(num) == 0)
                iterator.remove();
        }
    }
}
