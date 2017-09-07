package com.example.mohammadlaknahour.lighthouse;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mohammadlaknahour.lighthouse.R;

public class view extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        TextView nameView=(TextView)findViewById(R.id.result_txt);
        nameView.setText(getIntent().getExtras().getString("location"));

    }
}
