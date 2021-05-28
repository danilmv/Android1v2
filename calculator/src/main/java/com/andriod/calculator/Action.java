package com.andriod.calculator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum Action implements Serializable {
    ZERO("0"), ONE("1"), TWO("2"), THREE("3"), FOUR("4"),
    FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"),
    DECIMAL("."),
    CANCEL("C", true),
    SIGN("+/-", true),
    PERCENT("%", true), DIVIDE("/", true),
    MULTIPLY("*", true), SUBTRACT("-", true),
    ADD("+", true), EQUAL("=", true);

    private final String value;
    private final boolean isOperator;

    private static final Map<String, Action> actions = new HashMap<String, Action>();

    static {
        for (Action a : values()) {
            actions.put(a.getValue(), a);
        }
    }

    Action(String value, boolean isOperator) {
        this.value = value;
        this.isOperator = isOperator;
    }

    Action(String value) {
        this(value, false);
    }

    public String getValue() {
        return value;
    }

    public boolean isOperator() {
        return isOperator;
    }

    public static Action getAction(char value) {
        return actions.get(String.valueOf(value));
    }

    @Override
    public String toString() {
        return value;
    }
}
