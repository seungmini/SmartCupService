package datas;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;


/**
 * Created by LeeSeungMin on 2016-09-13.
 */
public class YValueFormatter implements ValueFormatter
{

    private DecimalFormat mFormat;

    public YValueFormatter() {
        mFormat = new DecimalFormat("0.#");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        if(value != 0) {
            return mFormat.format(value) + "병";

        }
        else{
            return "";
        }
    }
}

