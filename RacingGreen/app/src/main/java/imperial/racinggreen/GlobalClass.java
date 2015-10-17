package imperial.racinggreen;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by aaronsheah on 01/07/15.
 */
public class GlobalClass extends Application {

    private ArrayList<String> values;

    public ArrayList<String> getValues() {

        return values;
    }

    public void setValues(ArrayList<String> aValues) {

        values = aValues;

    }


}
