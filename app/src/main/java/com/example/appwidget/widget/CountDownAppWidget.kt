package com.example.appwidget.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import androidx.core.os.BuildCompat
import com.example.appwidget.R


class CountDownAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d(
            "Widget",
            "CountDownAppWidget#onUpdate() appWidgetIds:${appWidgetIds.contentToString()}"
        )

        for (appWidgetId in appWidgetIds) {
            updateCountDownList(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateCountDownList(context: Context,
                                    appWidgetManager: AppWidgetManager,
                                    appWidgetId: Int) {
        if (!BuildCompat.isAtLeastS()) {
            return
        }

        val remoteViews = RemoteViews(context.packageName, R.layout.widget_count_down_list)
        val builder = RemoteViews.RemoteCollectionItems.Builder()

        // val items = context.resources.getStringArray(R.array.count_down_list_titles)
        // items.forEachIndexed { index, id ->
        //     builder.addItem(index.toLong(), constructRemoteViews(context, id))
        // }
        // val collectionItems = builder.setHasStableIds(true).setViewTypeCount(items.count()).build()

        val menuResources = context.resources.obtainTypedArray(R.array.count_down_list_titles)
        for (index in 0 until menuResources.length()) {
            val resId = menuResources.getResourceId(index, -1)
            if (resId < 0) {
                continue
            }
            builder.addItem(index.toLong(), constructRemoteViews(context, resId))
        }

        // val collectionItems = builder.setHasStableIds(true).setViewTypeCount(menuResources.length()).build()
        val collectionItems = builder.setHasStableIds(true).build()
        remoteViews.setRemoteAdapter(R.id.count_down_list, collectionItems)
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
    }

    private fun constructRemoteViews(
        context: Context,
        stringArrayId: Int
        // value: String
    ): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.item_count_down)
        val itemData = context.resources.getStringArray(stringArrayId)

        itemData.forEachIndexed { index, value ->
            val viewId = when (index) {
                0 -> R.id.item_title
                1 -> R.id.item_time
                2 -> R.id.item_time_unit
                else -> 0
            }
            remoteViews.setTextViewText(viewId, value)
        }
        return remoteViews
    }
}