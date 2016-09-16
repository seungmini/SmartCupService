package datas;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * Created by LeeSeungMin on 2016-09-13.
 */
public class YlabelValueFormatter implements YAxisValueFormatter
{

    private DecimalFormat mFormat;

    public YlabelValueFormatter() {
        mFormat = new DecimalFormat("0.#");
    }

    @Override
    public String getFormattedValue(float value, YAxis axis) {
        if(value != 0) {
            return mFormat.format(value) + "병";

        }
        else{
            return "";
        }
    }


}