package tabview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lageder.main.R;

import java.util.ArrayList;

import datas.SCSDBManager;

/**
 * Created by Lageder on 2016-11-03.
 */

public class PhonePopupActivity extends Activity {
    private static final int REQUEST_CONTACTS = 2;
    private Display display;
    private TextView textview_title, textview_num, textview_name;
    private Button btn_add, btn_ok;
    private String receiveName;
    private String receivePhone;
    private ListView listview;
    private NumberAdapter adapter;
    private ArrayList<ListViewItem> item_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_number_set);

        adapter = new NumberAdapter();
        listview = (ListView) findViewById(R.id.number_list);
        listview.setAdapter(adapter);

        SCSDBManager db = new SCSDBManager(getApplicationContext(), "s2.db", null, 1);
        item_list = db.getPhone();

        for(ListViewItem item : item_list)
            adapter.addItem(item.getPhone_num(),item.getUser_name());

/*        adapter.addItem("01084830533","훈민");
        adapter.addItem("01057568719","승민");
        adapter.addItem("01043045775","상진");*/

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                adapter.notifyDataSetChanged();
            }
        });

        display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int) (display.getWidth() * 0.8);
        int height = (int) (display.getHeight() * 0.8);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        btn_add = (Button) findViewById(R.id.btn_add);
        //btn_type = (Button) findViewById(R.id.btn_type);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent,REQUEST_CONTACTS);
            }
        });

        btn_ok = (Button)findViewById(R.id.btn_ok);
        textview_title = (TextView)findViewById(R.id.phone_title);
        textview_name = (TextView)findViewById(R.id.phone_name);
        textview_num = (TextView)findViewById(R.id.phone_num);

        Typeface font_gabia = Typeface.createFromAsset(this.getAssets(), "gabia_solmee.ttf");

        textview_title.setTypeface(font_gabia);
        textview_name.setTypeface(font_gabia);
        textview_num.setTypeface(font_gabia);
        btn_add.setTypeface(font_gabia);
        btn_ok.setTypeface(font_gabia);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) {
            return;
        }
        if(requestCode == REQUEST_CONTACTS) {
            Cursor cursor = getContentResolver().query(data.getData(),
                    new String[] {
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                    }, null, null, null);
            cursor.moveToFirst();
            receiveName = cursor.getString(0);
            receivePhone = cursor.getString(1);

            for(int i = 0;i<adapter.getCount();i++) {
                if(adapter.getItem(i).getPhone_num().compareTo(receivePhone) == 0)
                    return;
            }

            SCSDBManager db = new SCSDBManager(getApplicationContext(), "s2.db", null, 1);
            String query_add = "INSERT INTO PHONE VALUES (\"" + receivePhone + "\",\"" + receiveName + "\");";
            db.executeQuery(query_add);

            adapter.addItem(receivePhone,receiveName);
            adapter.notifyDataSetChanged();
            cursor.close();
        }
    }
}
