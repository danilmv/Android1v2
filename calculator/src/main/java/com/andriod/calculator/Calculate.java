package com.andriod.calculator;

import android.widget.TextView;

import java.io.Serializable;
import java.util.Locale;

public class Calculate implements Serializable {

    private static final double MIN_FLOOR = 0.00001;

    private transient TextView textView;
    private transient TextView textViewOperation;
    private transient TextView textViewHistory;

    private int length;
    private boolean hasDecimal = false;
    private boolean operatorPressed = false;
    private Action lastOperator;
    private double firstValue = 0;
    private double secondValue = 0;
    private double prevValue = 0;

    private boolean newNumber = true;

    private final StringBuilder history;

    public Calculate(TextView textView, TextView textViewOperation, TextView textViewHistory) {
        this.textView = textView;
        this.textViewOperation = textViewOperation;
        this.textViewHistory = textViewHistory;
        history = new StringBuilder();
    }

    public void setTextView(TextView textView, TextView textViewOperation, TextView textViewHistory) {
        this.textView = textView;
        this.textViewOperation = textViewOperation;
        this.textViewHistory = textViewHistory;
        if (textViewHistory != null)
            textViewHistory.setText(history.toString());
        showOperation();
    }

    public void process(Action action) {
        int MAX_LENGTH = 15;
        if (length >= MAX_LENGTH) return;


        switch (action) {
            case CANCEL:
                textView.setText("");
                hasDecimal = false;
                length = 0;
                if (operatorPressed) {
                    if (newNumber) {
                        lastOperator = null;
                    } else {
                        newNumber = true;
                    }
                } else {
                    newNumber = true;
                    lastOperator = null;
                }
                showOperation();

                break;

            case ADD:
            case SUBTRACT:
            case MULTIPLY:
            case DIVIDE:
                if (!newNumber)
                    showCalculation();

                operatorPressed = true;
                lastOperator = action;
                prevValue = firstValue = getInputValue();
                secondValue = 0;
                newNumber = true;

                break;

            case EQUAL:
                newNumber = true;

                showCalculation();
                break;

            case PERCENT:
                break;

            case SIGN:
                setNegative();
                break;

            case ZERO:
                if (newNumber)
                    break;

            case DECIMAL:
                if (action == Action.DECIMAL) {
                    if (hasDecimal && !newNumber) break;
                    hasDecimal = true;
                }

            default:
                if (newNumber) {
                    length = 1;
                    textView.setText(action.getValue());
                    newNumber = false;
                    if (!operatorPressed)
                        lastOperator = null;
                } else {
                    length++;
                    textView.append(action.getValue());
                }
        }

        showOperation();
    }

    private double getInputValue() {
        String string = textView.getText().toString();
        if (string.isEmpty()) return 0;
        return Double.parseDouble(string);
    }

    private void showCalculation() {
        if (lastOperator == null) {
            showOperation();
            return;
        }
        if (secondValue == 0)
            secondValue = getInputValue();

        operatorPressed = false;

        prevValue = firstValue;
        switch (lastOperator) {
            case ADD:
                firstValue += secondValue;
                break;
            case SUBTRACT:
                firstValue -= secondValue;
                break;
            case MULTIPLY:
                firstValue *= secondValue;
                break;
            case DIVIDE:
                firstValue /= secondValue;
                break;
        }
        showNumber(firstValue);


        String operation = String.format("%s\n%s\n", showOperation(), formatNumber(firstValue));
        history.append(operation);
        if (textViewHistory != null)
            textViewHistory.append(operation);

    }

    private void setNegative() {
        showNumber(getInputValue() * -1);
    }

    private String formatNumber(double number) {

        if (number - Math.floor(number) < MIN_FLOOR)
            return String.format(Locale.getDefault(), "%.0f", number);
        else
            return String.format(Locale.getDefault(), "%s", number);
    }

    private void showNumber(double number) {
        textView.setText(formatNumber(number));
    }

    private String showOperation() {
        String operation;
        if (lastOperator != null)
            if (operatorPressed)
                operation = String.format(Locale.getDefault(), "%s %s",
                        formatNumber(prevValue), lastOperator.getValue());
            else
                operation = String.format(Locale.getDefault(), "%s %s %s = ",
                        formatNumber(prevValue), lastOperator.getValue(), formatNumber(secondValue));
        else
            operation = "";

        textViewOperation.setText(operation);
        return operation;
    }

    public void show() {
        showNumber(firstValue);
    }
}
