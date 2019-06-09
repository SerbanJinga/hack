package com.roh44x.medlinker.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.roh44x.medlinker.R;

import java.util.Random;

import static android.graphics.Color.BLUE;
import static com.google.common.collect.ComparisonChain.start;

public class ProgressFragment extends Fragment {
    private final Handler hndlr = new Handler();
    private Runnable runn;

    Button addBtn;
    Button stop;

    GraphView graph;


    private static final Random rnd = new Random();
    public LineGraphSeries<DataPoint> input;

    private double lastX = 5d;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.progress, null);

        final EditText et_name = (EditText) view.findViewById(R.id.editText_name);
        final TextView tv_name = (TextView) view.findViewById(R.id.textView_name);
        final EditText et_id = (EditText) view.findViewById(R.id.editText_id);
        final TextView tv_id = (TextView) view.findViewById(R.id.textView_id);
        final EditText et_age = (EditText) view.findViewById(R.id.editText_age);
        final TextView tv_age = (TextView) view.findViewById(R.id.textView_age);
        Button Enter = (Button) view.findViewById(R.id.button1);
        Button Clear = (Button) view.findViewById(R.id.button2);

        Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();
                tv_name.append("\n");
                tv_name.append(name);
                et_name.setText("");
                String id = et_id.getText().toString(); //Get txt from et when button is clicked
                tv_id.append("\n");
                tv_id.append(id);
                et_id.setText("");//String age = et_age.getText().toString(); //Get txt from et when button is clicked
                String age = et_age.getText().toString(); //Get txt from et when button is clicked
                tv_age.append("\n");
                tv_age.append(age);
                et_age.setText("");//String age = et_ag
            }
        });

        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String name = et_name.getText().toString(); //Get txt from et when button is clicked
                tv_name.setText("     NAME"); //Set text extracted from et in tv

                //String id = et_id.getText().toString(); //Get txt from et when button is clicked
                tv_id.setText("       ID"); //Set text extracted from et in tv
                et_id.setText("");//String age = et_age.getText().toString(); //Get txt from et when button is clicked
                tv_age.setText("        AGE"); //Set text extracted from et in tv

            }
        });

        addBtn = (Button)view.findViewById(R.id.AddBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        stop = (Button)view.findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });


        graph = (GraphView) view.findViewById(R.id.graph);
        //Set Graph Title and X-Axis and Y-Axis Labels
        graph.setTitle("Group 9: Health Monitoring UI");
        graph.setTitleTextSize((float) 60.0);
        graph.setTitleColor(BLUE);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Patient Data");
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(BLUE);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time/s");
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(BLUE);

        Viewport viewport = graph.getViewport();
        viewport.setScrollable(false);

        //input = new LineGraphSeries<>(generateData());
        input = new LineGraphSeries<>();
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(40);
        graph.getViewport().setXAxisBoundsManual(true);
        return view;
    }

    public void start() {
        graph.addSeries(input);
        super.onStart();
        runn = new Runnable() {
            @Override
            public void run() {
                //input.resetData(generateData());
                lastX+=1d;
                input.appendData(new DataPoint(lastX, rndGen()), true, 100);
                hndlr.postDelayed(this, 270);
            }
        };
        hndlr.postDelayed(runn, 270);
    }
    public void stop()
    {
        hndlr.removeCallbacks(runn);
        super.onPause();
        graph.removeAllSeries();
    }

    double m =2;
    private double rndGen() {
        return m += rnd.nextDouble()*0.75;
    }


}
