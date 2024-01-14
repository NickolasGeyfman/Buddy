package com.example.buddy2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private SeekBar seekBar;
    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.mainLayout);
        seekBar = findViewById(R.id.seekBar);
        button = findViewById(R.id.button);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateBackgroundColor(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sliderPosition = seekBar.getProgress();
                openActivity2(sliderPosition);
            }
        });
    }

    private void updateBackgroundColor(int progress) {
        float[] hsv = new float[3];
        float hue = progress / 100f * 120f; // 0 (red) to 120 (green)
        hsv[0] = hue;
        hsv[1] = 1f; // Full saturation
        hsv[2] = 0.3f; // Reduced value/brightness for darker colors

        int color = Color.HSVToColor(hsv);
        layout.setBackgroundColor(color);
    }

    public void openActivity2(int sliderPosition) {
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("SliderPosition", sliderPosition);
        startActivity(intent);
    }
}
