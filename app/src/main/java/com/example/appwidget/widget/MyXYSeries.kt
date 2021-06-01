package com.example.appwidget.widget

import com.androidplot.xy.SimpleXYSeries

class MyXYSeries(xVals: List<Number?>?, yVals: List<Number?>?, title: String?) :
    SimpleXYSeries(xVals, yVals, title) {
    override fun getTitle(): String {
//        Log.d("Widget", "MyXYSeries# getTitle() WITH:", Throwable())
        return super.getTitle()
    }
}