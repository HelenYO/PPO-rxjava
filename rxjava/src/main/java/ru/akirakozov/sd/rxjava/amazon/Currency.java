package ru.akirakozov.sd.rxjava.amazon;

import java.util.HashMap;
import java.util.Map;

public class Currency {
    CurrencyDetail state;
    public boolean isCorrect = true;

    enum CurrencyDetail {
        EURO,
        DOLLAR,
        RUBLE
    }

    Map<CurrencyDetail, Double> CurrencyDetailPrice = new HashMap<>();

    public Currency(String str) {
        CurrencyDetailPrice.put(CurrencyDetail.EURO, 79.0);
        CurrencyDetailPrice.put(CurrencyDetail.DOLLAR, 67.0);
        CurrencyDetailPrice.put(CurrencyDetail.RUBLE, 1.0);

        if (str.toLowerCase().equals("euro")) {
            state = CurrencyDetail.EURO;
        } else if (str.toLowerCase().equals("dollar")) {
            state = CurrencyDetail.DOLLAR;
        } else if (str.toLowerCase().equals("ruble")) {
            state = CurrencyDetail.RUBLE;
        } else {
            isCorrect = false;
        }
    }

    @Override
    public String toString() {
        if (state.equals(CurrencyDetail.EURO)) {
            return "euro";
        } else if (state.equals(CurrencyDetail.DOLLAR)) {
            return "dollar";
        } else {
            return "ruble";
        }
    }

    public int exchangeRate() {
        if (state.equals(CurrencyDetail.EURO)) {
            return 75;
        } else if (state.equals(CurrencyDetail.DOLLAR)) {
            return 65;
        } else {
            return 1;
        }
    }
}
