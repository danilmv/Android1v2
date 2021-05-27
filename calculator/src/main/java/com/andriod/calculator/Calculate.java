package com.andriod.calculator;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Locale;

public class Calculate implements Serializable, Parcelable {

    private static final double MIN_FLOOR = 0.00001;

    private int length;
    private boolean hasDecimal = false;
    private boolean operatorPressed = false;
    private Action lastOperator;
    private double firstValue = 0;
    private double secondValue = 0;
    private double prevValue = 0;

    private boolean newNumber = true;

    private final StringBuilder history = new StringBuilder();

    public interface ShowValuesListener {
        void setMainText(String value);

        void appendMainText(String value);

        String getMainText();

        void setOperationText(String value);

        void setHistoryText(String value);

        void appendHistoryText(String value);
    }

    private ShowValuesListener listener;

    public Calculate(ShowValuesListener listener) {
        this.listener = listener;
    }

    public void setListener(ShowValuesListener listener) {
        this.listener = listener;
    }

    public void process(Action action) {
        int MAX_LENGTH = 30;

        switch (action) {
            case CANCEL:
                listener.setMainText("");
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
                if (!newNumber) showCalculation();

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
                getPercent();
                break;

            case SIGN:
                setNegative();
                break;

            case ZERO:
                if (newNumber) break;

            case DECIMAL:
                if (action == Action.DECIMAL) {
                    if (hasDecimal && !newNumber) break;
                    hasDecimal = true;
                }

            default:
                if (newNumber) {
                    length = 1;
                    listener.setMainText(action.getValue());
                    newNumber = false;
                    if (!operatorPressed)
                        lastOperator = null;
                } else {
                    if (length >= MAX_LENGTH) return;
                    length++;
                    listener.appendMainText(action.getValue());
                }
        }

        showOperation();
    }

    private double getInputValue() {
        String string = listener.getMainText();
        if (string.isEmpty()) return 0;
        return Double.parseDouble(string);
    }

    private void showCalculation() {
        if (lastOperator == null) {
            showOperation();
            return;
        }
        if (secondValue == 0) secondValue = getInputValue();

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
        listener.appendHistoryText(operation);

    }

    private void setNegative() {
        showNumber(getInputValue() * -1);
    }

    private void getPercent() {
        if (lastOperator == null) return;

        if (secondValue == 0) secondValue = getInputValue();

        secondValue = firstValue * secondValue / 100;

        showNumber(secondValue);
    }

    private String formatNumber(double number) {

        if (number - Math.floor(number) < MIN_FLOOR) {
            return String.format(Locale.getDefault(), "%.0f", number);
        } else {
            return String.format(Locale.getDefault(), "%s", number);
        }
    }

    private void showNumber(double number) {
        listener.setMainText(formatNumber(number));
    }

    private String showOperation() {
        String operation;
        if (lastOperator != null) {
            if (operatorPressed) {
                operation = String.format(Locale.getDefault(), "%s %s",
                        formatNumber(prevValue), lastOperator.getValue());
            } else {
                operation = String.format(Locale.getDefault(), "%s %s %s = ",
                        formatNumber(prevValue), lastOperator.getValue(), formatNumber(secondValue));
            }
        } else operation = "";

        listener.setOperationText(operation);
        return operation;
    }

    public void show() {
        showNumber(firstValue);
        listener.setHistoryText(history.toString());
        showOperation();
    }

    protected Calculate(Parcel in) {

        length = in.readInt();
        hasDecimal = in.readByte() != 0;
        operatorPressed = in.readByte() != 0;
        firstValue = in.readDouble();
        secondValue = in.readDouble();
        prevValue = in.readDouble();
        newNumber = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(length);
        dest.writeByte((byte) (hasDecimal ? 1 : 0));
        dest.writeByte((byte) (operatorPressed ? 1 : 0));
        dest.writeDouble(firstValue);
        dest.writeDouble(secondValue);
        dest.writeDouble(prevValue);
        dest.writeByte((byte) (newNumber ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Calculate> CREATOR = new Creator<Calculate>() {
        @Override
        public Calculate createFromParcel(Parcel in) {
            return new Calculate(in);
        }

        @Override
        public Calculate[] newArray(int size) {
            return new Calculate[size];
        }
    };
}
