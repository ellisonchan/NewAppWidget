package com.example.appwidget.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log

class StateListAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d(
            "Widget",
            "StateListAppWidget#onUpdate() appWidgetIds:${appWidgetIds.contentToString()}"
        )
    }
}