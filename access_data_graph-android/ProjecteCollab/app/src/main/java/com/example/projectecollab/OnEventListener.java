package com.example.projectecollab;

import java.util.List;

public interface OnEventListener {
    public void onSuccess(List<Double> x, List<Double> y,
                          String firstDate, String lastDate);
    public void onFailure(String error);
}