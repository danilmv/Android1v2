package com.andriod.calculator;

import android.widget.TextView;

import java.io.Serializable;
import java.util.Locale;

public class Calculate implements Serializable {

    private transient TextView textView;

    private int length;
    private boolean hasDecimal = false;
    private boolean operatorPressed = false;
    private Action lastOperator;
    private double value = 0;
    private double prevValue = 0;

    private boolean newNumber = true;

    public Calculate(TextView textView) {
        this.textView = textView;
    }

    public void setTextView(TextView textView){
        this.textView = textView;
    }

    public void process(Action action) {
        int MAX_LENGTH = 15;
        if (length >= MAX_LENGTH) return;


        switch (action) {
            case CANCEL:
                clear();

                lastOperator = null;
                return;

            case ADD:
            case SUBTRACT:
            case MULTIPLY:
            case DIVIDE:
                if (operatorPressed)
                    showCalculation();
                value = getInputValue();
                operatorPressed = true;
                newNumber = true;
                lastOperator = action;
                prevValue = 0;
                break;

            case EQUAL:
                if (prevValue == 0)
                    prevValue = getInputValue();
                showCalculation();
                operatorPressed = false;
                newNumber = true;
                break;

            case PERCENT:
                break;
            case SIGN:
                break;

            case DECIMAL:
                if (hasDecimal) return;
                hasDecimal = true;

            default:
                if (newNumber) {
                    value = getInputValue();
                    clear();
                    textView.setText(action.getValue());
                    newNumber = false;
                    if (!operatorPressed)
                        lastOperator = null;
                } else {
                    length++;
                    textView.append(action.getValue());
                }
        }
    }

    private void clear() {
        textView.setText("");
        hasDecimal = false;
        length = 0;
    }

    private double getInputValue() {
        String string = textView.getText().toString();
        if (string.isEmpty()) return 0;
        return Double.parseDouble(string);
    }

    private void showCalculation() {
        if (lastOperator == null) return;
        switch (lastOperator) {
            case ADD:
                value += prevValue;
                break;
            case SUBTRACT:
                value -= prevValue;
                break;
            case MULTIPLY:
                value *= prevValue;
                break;
            case DIVIDE:
                value /= prevValue;
                break;
        }


        clear();
        textView.setText(String.format(Locale.getDefault(), "%.5f", value));
    }

    public void show(){
        textView.setText(String.format(Locale.getDefault(), "%.5f", value));
    }
}
