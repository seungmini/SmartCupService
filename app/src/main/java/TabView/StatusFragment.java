package tabview;

/**
 * Created by Lageder on 2016-07-23.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lageder.main.MainActivity;
import com.example.lageder.main.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import bluetoothLe.DevicePopupActivity;
import datas.SCSDBManager;

public class StatusFragment extends Fragment {
    Activity activity;
    private Switch swc;
    private Button gps_btn;
    private Button phone_btn;

    private final int REQUEST_BLE = 1;
    private final int REQUEST_CONTACTS = 2;

    private TextView svText;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_status, container, false);

        activity = getActivity();

        swc = (Switch)v.findViewById(R.id.ble_switch);
        swc.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton cb, boolean isChecking) {
                String str = String.valueOf(isChecking);
                if(isChecking) {
                    Intent intent = new Intent(activity, DevicePopupActivity.class);
                    activity.startActivityForResult(intent,REQUEST_BLE);
                    Toast.makeText(activity.getApplication(), "Try to connect..", Toast.LENGTH_SHORT).show();
                }
                else {
                    MainActivity myActivity = (MainActivity) getActivity();
                    myActivity.disconnect_ble();
                }
            }
        });

        phone_btn = (Button)v.findViewById(R.id.phone_btn);
        phone_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PhonePopupActivity.class);
                activity.startActivity(intent);
            }
        });

/*        gps_btn = (Button)v.findViewById(R.id.gps_btn);
        gps_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        return v;
    }

}
