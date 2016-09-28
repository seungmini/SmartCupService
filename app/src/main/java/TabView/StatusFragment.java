package tabview;

/**
 * Created by Lageder on 2016-07-23.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.lageder.main.R;

import bluetoothLe.DevicePopupActivity;

/**
 * Created by Ferdousur Rahman Shajib on 27-10-2015.
 */
public class StatusFragment extends Fragment {
    Activity activity;
    private Switch swc;

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
                    activity.startActivityForResult(intent,1);
                    Toast.makeText(activity.getApplication(), "Try to connect..", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(activity.getApplication(), "Disconnect." , Toast.LENGTH_SHORT).show();
                }

            }
        });

        return v;
    }

}