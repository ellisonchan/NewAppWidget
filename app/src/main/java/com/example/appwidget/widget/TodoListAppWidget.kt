package com.example.appwidget.widget

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.util.SizeF
import android.view.View
import android.widget.RemoteViews
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.BuildCompat
import com.example.appwidget.R
import com.example.appwidget.activity.WidgetConfigureActivity

class TodoListAppWidget : AppWidgetProvider() {
    companion object {
        const val REQUEST_CODE_FROM_CHECKBOX_WIDGET = 2
        const val EXTRA_VIEW_ID = "extra_view_id"
        const val REQUEST_CODE = "request_code"
    }

    @RequiresApi(31)
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d(
            "Widget",
            "TodoListAppWidget#onUpdate() appWidgetIds:${appWidgetIds.contentToString()}"
        )
        for (appWidgetId in appWidgetIds) {
            // updateAppWidget(context, appWidgetManager, appWidgetId)
            updateAppWidgetWithResponsiveLayouts(context, appWidgetManager, appWidgetId)
        }
    }

    @SuppressLint("RemoteViewLayout")
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.d(
            "Widget",
            "TodoListAppWidget#onReceive() intent:$intent"
        )

        // Ensure sdk version above S cause checkbox supported from this version.
        if (!BuildCompat.isAtLeastS() ||
            intent?.extras?.getInt(REQUEST_CODE) != REQUEST_CODE_FROM_CHECKBOX_WIDGET
        ) {
            return
        }

        val checked = intent.extras?.getBoolean(
            RemoteViews.EXTRA_CHECKED,
            false
        ) ?: false
        val viewId = intent.extras?.getInt(EXTRA_VIEW_ID) ?: -1

        Log.d(
            "Widget",
            "TodoListAppWidget#onReceive() checked:$checked"
        )

        // Notify user checkbox checked status.
        Toast.makeText(
            context,
            "ViewId : $viewId's checked status is now : $checked",
            Toast.LENGTH_SHORT
        ).show()

        updateCheckedColor(context, viewId, checked)
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        Log.d(
            "Widget",
            "TodoListAppWidget#onDeleted() appWidgetIds:${appWidgetIds.contentToString()}"
        )
    }

    @RequiresApi(31)
    @SuppressLint("RemoteViewLayout")
    private fun updateCheckedColor(context: Context?, viewId: Int, checked: Boolean) {
        // Get target widget.
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisAppWidget = ComponentName(context!!.packageName, TodoListAppWidget::class.java.name)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget)
        Log.d(
            "Widget",
            "TodoListAppWidget#onReceive() viewId:$viewId"
                    + "appWidgetIds:${appWidgetIds.contentToString()}"
        )

        // Update widget color parameters dynamically.
        for (appWidgetId in appWidgetIds) {
            val remoteViews = RemoteViews(context.packageName, R.layout.widget_todo_list)
            remoteViews.setColorStateList(
                viewId,
                "setTextColor",
                getColorStateList(context, checked)
            )
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }
    }

//    val attribute = intArrayOf(android.R.attr.textColorPrimary)
//    val array: TypedArray = context.obtainStyledAttributes(attribute)
//    val color = array.getColor(0, Color.TRANSPARENT)
//    array.recycle()

    private fun getColorStateList(context: Context, checkStatus: Boolean): ColorStateList =
        if (checkStatus)
            ColorStateList.valueOf(context.getColor(R.color.widget_checked_text_color))
        else
            ColorStateList.valueOf(context.getColor(R.color.widget_unchecked_text_color))

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        Log.d(
            "Widget",
            "PedometerAppWidget#onAppWidgetOptionsChanged() appWidgetId:$appWidgetId"
        )
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)

        // Get the new sizes.
        val sizes = newOptions?.getParcelableArrayList<SizeF>(
            AppWidgetManager.OPTION_APPWIDGET_SIZES
        )

        // Do nothing if sizes is not provided by the launcher.
        if (sizes.isNullOrEmpty()) {
            return
        }
        Log.d("Widget", "PedometerAppWidget#onAppWidgetOptionsChanged() size:${sizes}")

//        // Get exact layout
//        if (BuildCompat.isAtLeastS()) {
//            val remoteViews = RemoteViews(sizes.associateWith(::createRemoteViews))
//            appWidgetManager?.updateAppWidget(appWidgetId, remoteViews)
//        }
    }

    @SuppressLint("RemoteViewLayout")
    @RequiresApi(31)
    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        if (!BuildCompat.isAtLeastS()) {
            return
        }

        val viewId1 = R.id.checkbox_first
        val viewId2 = R.id.checkbox_second
        val viewId3 = R.id.checkbox_third

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_FROM_CHECKBOX_WIDGET,
            Intent(context, TodoListAppWidget::class.java).apply {
                putExtra(EXTRA_VIEW_ID, viewId1)
                putExtra(REQUEST_CODE, REQUEST_CODE_FROM_CHECKBOX_WIDGET)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val rv = RemoteViews(context.packageName, R.layout.widget_todo_list)
        rv.apply {
            // Show text
            setTextViewText(viewId1, context.resources.getString(R.string.todo_list_android))
            setTextViewText(viewId2, context.resources.getString(R.string.todo_list_compose))
            setTextViewText(viewId3, context.resources.getString(R.string.todo_list_kotlin))

            // Show default checked status
            setCompoundButtonChecked(viewId1, true)

            // Listen to status change listener
            setOnCheckedChangeResponse(
                viewId1,
                RemoteViews.RemoteResponse.fromPendingIntent(pendingIntent)
            )
        }
        appWidgetManager.updateAppWidget(appWidgetId, rv)
    }

    @SuppressLint("RemoteViewLayout")
    @RequiresApi(31)
    private fun updateAppWidgetWithResponsiveLayouts(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        if (!BuildCompat.isAtLeastS()) {
            return
        }

        Log.d(
            "Widget",
            "PedometerAppWidget#updateAppWidgetWithResponsiveLayouts() appWidgetId:$appWidgetId"
        )

        val button = R.id.checkbox_list_btn
        val viewId1 = R.id.checkbox_first
        val viewId2 = R.id.checkbox_second
        val viewId3 = R.id.checkbox_third

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_FROM_CHECKBOX_WIDGET,
            Intent(context, TodoListAppWidget::class.java).apply {
                putExtra(EXTRA_VIEW_ID, viewId1)
                putExtra(REQUEST_CODE, REQUEST_CODE_FROM_CHECKBOX_WIDGET)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val rv = RemoteViews(context.packageName, R.layout.widget_todo_list)
        rv.apply {
            // Show text
            setTextViewText(viewId1, context.resources.getString(R.string.todo_list_android))
            setTextViewText(viewId2, context.resources.getString(R.string.todo_list_compose))
            setTextViewText(viewId3, context.resources.getString(R.string.todo_list_kotlin))

            // Show default checked status
            setCompoundButtonChecked(viewId1, true)

            // Update checked text color
            rv.setColorStateList(viewId1, "setTextColor", getColorStateList(context, true))

            // Listen to status change listener
            setOnCheckedChangeResponse(
                viewId1,
                RemoteViews.RemoteResponse.fromPendingIntent(pendingIntent)
            )

            // Not show button default
            rv.setViewVisibility(button, View.GONE)
        }
        // appWidgetManager.updateAppWidget(appWidgetId, rv)

        // Build app intent and bind to widget's button.
        val intent = Intent(context, WidgetConfigureActivity::class.java)
        val showAppPendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_MUTABLE
        )
        // rv.setOnClickPendingIntent(R.id.checkbox_list_btn, pendingIntent)

        // Init responsive remote views
        val wideView = RemoteViews(rv)
        wideView.setViewVisibility(button, View.VISIBLE)
        wideView.setOnClickPendingIntent(R.id.checkbox_list_btn, showAppPendingIntent)

        val viewMapping: Map<SizeF, RemoteViews> = mapOf(
            SizeF(100f, 100f) to rv,
            SizeF(200f, 100f) to wideView
        )
        val remoteViews = RemoteViews(viewMapping)
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
    }

    private fun createRemoteViews(size: SizeF): RemoteViews? {
        val smallView: RemoteViews? = null
        val tallView: RemoteViews? = null
        val wideView: RemoteViews? = null
        val bigView: RemoteViews? = null
        val xTallView: RemoteViews? = null
        val xWideView: RemoteViews? = null

        return when (size) {
            SizeF(100f, 100f) -> smallView
            SizeF(100f, 200f) -> tallView
            SizeF(200f, 100f) -> wideView
            SizeF(200f, 200f) -> bigView
            SizeF(200f, 300f) -> xTallView
            SizeF(300f, 200f) -> xWideView
            else -> smallView
        }
    }
}