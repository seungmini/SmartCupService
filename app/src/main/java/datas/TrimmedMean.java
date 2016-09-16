package datas;

import java.util.Arrays;

/**
 * Created by Lageder on 2016-09-01.
 */
public class TrimmedMean {
    double[] buffer;
    int n, div, sum;
    double avg;

    private TrimmedMean(double[] target, int length) {
        System.arraycopy(buffer,0,target,0,length);
        n = length;
        div = Math.round(n * 10 / 100);
    }

    private void calculate() {
        Arrays.sort(buffer);

        if(n == 0) {
            avg = -1;
            return;
        }
        else {
            for(int i = div; i < n - div; i++) {
                sum += buffer[i];
            }
            avg = sum / (n - div);
        }
    }

    private double getAvg() {
        if(n != 0) return avg;
        else return -1;
    }
}
