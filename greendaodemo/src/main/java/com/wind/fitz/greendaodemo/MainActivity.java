package com.wind.fitz.greendaodemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wind.fitz.greendaodemo.bean.ABusData;
import com.wind.fitz.greendaodemo.bean.BusLine;

import java.nio.Buffer;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private TextView mTextView;
    private Button mbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.tv1);
        mbutton = findViewById(R.id.button1);
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusLine bus = new BusLine(1L,"021","763","763路",5,"水清路报春路");
                ABusData.getInstance().insertBus(bus);

                List<BusLine> busLine= ABusData.getInstance().queryRawBus("021");
                for (BusLine businfo:busLine){
                    if (businfo.getLineID().equals("763")){
                        mTextView.setText(businfo.toString());
                    }
                }

            }
        });
    }



}
