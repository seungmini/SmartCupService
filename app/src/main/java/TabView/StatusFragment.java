package tabview;

/**
 * Created by Lageder on 2016-07-23.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lageder.main.R;
import com.lylc.widget.circularprogressbar.CircularProgressBar;

/**
 * Created by Ferdousur Rahman Shajib on 27-10-2015.
 */
public class StatusFragment extends Fragment {
    Activity activity;
    Button button1;
    CircularProgressBar c1;
    int current_value;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_status, container, false);

        activity = getActivity();
        c1 = (CircularProgressBar) v.findViewById(R.id.circularprogressbar1);
        current_value = 95;
        c1.animateProgressTo(0, current_value, new CircularProgressBar.ProgressAnimationListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationFinish() {
                c1.setTitle("Concentration");
            }

            @Override
            public void onAnimationProgress(int progress) {
                c1.setTitle(progress + "%");
            }
        });

        button1 = (Button) v.findViewById(R.id.buttonTab1);
        button1.setText("RESET");

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c1.animateProgressTo(current_value, 0, new CircularProgressBar.ProgressAnimationListener() {
                    @Override
                    public void onAnimationStart() {
                    }

                    @Override
                    public void onAnimationFinish() {
                        c1.setTitle("초기화");
                        current_value = 0;
                    }

                    @Override
                    public void onAnimationProgress(int progress) {
                        c1.setTitle(progress + "%");
                    }
                });
            }
        });

        return v;
    }


}