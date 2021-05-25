package com.andriod.calculatorCopy;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    private FrameLayout container;
    private boolean inflated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.container);

        RadioGroup rg = findViewById(R.id.radio_group);
        rg.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_linear:
                    setStubView(R.layout.calculator_linear);
                    break;
                case R.id.radio_table:
                    setStubView(R.layout.calculator_table);
                    break;
                case R.id.radio_grid:
                    setStubView(R.layout.calculator_grid);
                    break;
                case R.id.radio_constraint:
                    setStubView(R.layout.calculator_constraint);
                    break;
            }
        });

        RadioButton rbLinear = findViewById(R.id.radio_linear);
        rbLinear.setChecked(true);
    }

    private void setStubView(@LayoutRes int layoutResource) {
        container.removeAllViews();
        container.addView(LayoutInflater.from(this).inflate(layoutResource,container, false));
    }
}