package com.example.appwidget.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.os.BuildCompat
import com.androidplot.Plot
import com.androidplot.xy.*
import com.example.appwidget.R


class PedometerAppWidget : AppWidgetProvider() {
    companion object {
        const val REQUEST_CODE_FROM_BUTTON_WIDGET = 2
        const val EXTRA_VIEW_ID = "extra_view_id"
        const val REQUEST_CODE = "request_code"
        const val SP_CHART_SIZE = "widget_state"
        const val SP_CHART_SIZE_KEY = "widget_state_scale"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(
            "Widget",
            "PedometerAppWidget#onReceive() intent:$intent"
        )
        super.onReceive(context, intent)
        if (!BuildCompat.isAtLeastS() ||
            intent?.extras?.getInt(REQUEST_CODE) != REQUEST_CODE_FROM_BUTTON_WIDGET
        ) {
            return
        }

        val viewId = intent.extras?.getInt(EXTRA_VIEW_ID) ?: -1
        if (viewId == -1) {
            return
        }

        val sharedPreferences = context?.getSharedPreferences(SP_CHART_SIZE, Context.MODE_PRIVATE)
        val scaleOutStatus = sharedPreferences?.getBoolean(SP_CHART_SIZE_KEY, false) ?: false

        // Get target widget.
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisAppWidget =
            ComponentName(context!!.packageName, PedometerAppWidget::class.java.name)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget)
        Log.d(
            "Widget",
            "PedometerAppWidget#onReceive() viewId:$viewId"
                    + "appWidgetIds:${appWidgetIds.contentToString()}"
                    + "scaleOutStatus:$scaleOutStatus"
        )

        val widthScaleSize = if (scaleOutStatus) 200f else 260f
        val heightScaleSize = if (scaleOutStatus) 130f else 160f

        // Update widget layout parameters dynamically.
        for (appWidgetId in appWidgetIds) {
            val remoteViews = RemoteViews(context.packageName, R.layout.widget_pedometer)
            remoteViews.setViewLayoutWidth(viewId, widthScaleSize, TypedValue.COMPLEX_UNIT_DIP)
            // remoteViews.setViewLayoutWidth(
//                remoteViews.setViewLayoutWidthDimen(
//                    viewId,
//                    context.resources.getDimension(R.dimen.widget_pedo_chart_width_large).toInt()
//                    // TypedValue.COMPLEX_UNIT_DIP
//                )
            remoteViews.setViewLayoutHeight(viewId, heightScaleSize, TypedValue.COMPLEX_UNIT_DIP)
            // remoteViews.setViewLayoutHeight(viewId, context.resources.getDimension(R.dimen.widget_pedo_chart_height_large), TypedValue.COMPLEX_UNIT_DIP)
            // remoteViews.setViewLayoutHeightDimen(viewId, context.resources.getDimension(R.dimen.widget_pedo_chart_height_large).toInt())
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }
        sharedPreferences?.edit()?.putBoolean(SP_CHART_SIZE_KEY, !scaleOutStatus)?.apply()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d("Widget", "PedometerAppWidget#onUpdate() appWidgetIds:${appWidgetIds.contentToString()}")
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            showBarChartToWidget(context, appWidgetManager, appWidgetId)
            // showLineChartToWidget(context, appWidgetManager, appWidgetId)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun showBarChartToWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        // Create plot view.
        val plot = XYPlot(context, "Pedometers chart")
        // plot.getLayoutParams().height = 750;
        // plot.getLayoutParams().width = 250;
        plot.measure(750, 250)
        plot.layout(0, 0, 750, 250)
        plot.isDrawingCacheEnabled = true

        // Create x y number array
        val seriesXNumbers = arrayOf<Number>(6, 8, 10, 12, 14, 16, 18, 20, 22, 24)
        val seriesYNumbers = arrayOf<Number>(0, 1000, 500, 2000, 1000, 1800, 2500, 500, 300, 0)
        // Turn the above arrays into XYSeries':
        // val series1: XYSeries = SimpleXYSeries(
        val series1: XYSeries = MyXYSeries(
            listOf(*seriesXNumbers),
            listOf(*seriesYNumbers),
            // "Time & meters" // Set the display title of the series
            null // Not show label
        )

//        // Turn the above arrays into XYSeries':
//        val series1: XYSeries = SimpleXYSeries(
//            listOf(*series1Numbers),  // SimpleXYSeries takes a List so turn our array into a List
//            SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,  // Y_VALS_ONLY means use the element index as the x value
//            "Series1" // Set the display title of the series
//        )

//        // Create a formatter to use for drawing a series using LineAndPointRenderer:
//        val series1Format = LineAndPointFormatter(
//            Color.rgb(0, 200, 0),
//            Color.rgb(0, 200, 0),
//            Color.rgb(0, 100, 0),
//            null
//        )
        // add a new series' to the x y plot:
        // plot.addSeries(series1, series1Format)

        val attribute = intArrayOf(android.R.attr.colorAccent)
        val array: TypedArray = context.obtainStyledAttributes(attribute)
        val color = array.getColor(0, Color.TRANSPARENT)
        array.recycle()

        // Create bar formatter
        val seriesFormatter = BarFormatter(
            // color, // Bar's fill color
            context.resources.getColor(R.color.widget_main_color), // Bar's fill color
            // Color.WHITE // Bar's border color
            // Color.rgb(100, 150, 100),
            Color.LTGRAY
        )
        // seriesFormatter.fillPaint.

        // Bind numbers and formatter to plot view.
        plot.addSeries(series1, seriesFormatter)

        // Todo reduce the number of range labels
        // plot.setTicksPerRangeLabel(3);

        // Disable grid range and border layout
        // plot.disableAllMarkup()
        // plot.removeMarkers()
        // plot.backgroundPaint.color = Color.argb(0, 100, 150, 100)
        plot.graph.domainGridLinePaint.color = Color.TRANSPARENT
        plot.graph.domainOriginLinePaint.color = Color.TRANSPARENT
        plot.graph.domainSubGridLinePaint.color = Color.TRANSPARENT
        plot.graph.domainCursorPaint.color = Color.TRANSPARENT

        plot.graph.rangeGridLinePaint.color = Color.TRANSPARENT
        plot.graph.rangeSubGridLinePaint.color = Color.TRANSPARENT
        plot.graph.rangeCursorPaint.color = Color.TRANSPARENT
        plot.backgroundPaint.strokeWidth = 0f
        plot.borderPaint.color = Color.TRANSPARENT

        // Disable or set background
        // plot.backgroundPaint.alpha = 0
        // plot.backgroundPaint.color = context.resources.getColor(R.color.widget_chart_bg_color)
        plot.backgroundPaint.color = Color.TRANSPARENT
        plot.graph.backgroundPaint.alpha = 0
        plot.graph.gridBackgroundPaint.alpha = 0

        // Set title color
        plot.title.labelPaint.color = context.resources.getColor(R.color.widget_main_color)

        // Set bar width
        //        seriesFormatter.marginLeft = PixelUtils.dpToPix(70f)
        //        seriesFormatter.marginRight = PixelUtils.dpToPix(70f)
        val renderer = plot.getRenderer(BarRenderer::class.java) as BarRenderer<*>
        renderer.setBarGroupWidth(BarRenderer.BarGroupWidthMode.FIXED_WIDTH, 20f)

        // Todo set x values text size
        // plot.setRangeLabel()
        // plot.graph.setdom
        // plot.graph.marginBottom = 15f
        // Customize xy formatter

        // Todo show x values
        // https://stackoverflow.com/questions/10770220/androidplot-setting-the-labels-on-the-x-axis

        // Set graph shape
        plot.setBorderStyle(Plot.BorderStyle.ROUNDED, 12f, 12f)

        // Reflect chart's bitmap to widget.
        val bmp = plot.drawingCache
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_pedometer)
        remoteViews.setBitmap(R.id.bar_chart, "setImageBitmap", bmp)

        val buttonId = R.id.bar_chart
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_FROM_BUTTON_WIDGET,
            Intent(context, PedometerAppWidget::class.java).apply {
                putExtra(EXTRA_VIEW_ID, buttonId)
                putExtra(
                    REQUEST_CODE,
                    REQUEST_CODE_FROM_BUTTON_WIDGET
                )
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        remoteViews.setOnClickResponse(
            buttonId,
            RemoteViews.RemoteResponse.fromPendingIntent(pendingIntent)
        )

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun showLineChartToWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val plot = XYPlot(context, "Widget Example")
        //plot.getLayoutParams().height = 100;
        //plot.getLayoutParams().width = 100;
        plot.measure(220, 180)
        plot.layout(0, 0, 220, 180)
        plot.isDrawingCacheEnabled = true

        // Create a couple arrays of y-values to plot:
        val series1Numbers = arrayOf<Number>(1, 8, 5, 2, 7, 4)
        val series2Numbers = arrayOf<Number>(4, 6, 3, 8, 2, 10)

        // Turn the above arrays into XYSeries':
        val series1: XYSeries = SimpleXYSeries(
            listOf(*series1Numbers),  // SimpleXYSeries takes a List so turn our array into a List
            SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,  // Y_VALS_ONLY means use the element index as the x value
            "Series1"
        ) // Set the display title of the series

        // same as above
        val series2: XYSeries = SimpleXYSeries(
            listOf(*series2Numbers),
            SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
            "Series2"
        )

        // Create a formatter to use for drawing a series using LineAndPointRenderer:
        val series1Format = LineAndPointFormatter(
            Color.rgb(0, 200, 0),
            Color.rgb(0, 200, 0),  // line color
            Color.rgb(0, 100, 0),
            null
        ) // fill color (none)

        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format)

        // same as above:
        plot.addSeries(
            series2,
            LineAndPointFormatter(
                Color.rgb(0, 0, 200),
                Color.rgb(0, 0, 200),
                Color.rgb(0, 0, 100),
                null
            )
        )

        // Todo reduce the number of range labels
        // plot.setTicksPerRangeLabel(3);

        // by default, AndroidPlot displays developer guides to aid in laying out your plot.
        // To get rid of them call disableAllMarkup():
        //plot.disableAllMarkup();

        val bmp = plot.drawingCache
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_pedometer)
        remoteViews.setBitmap(R.id.bar_chart, "setImageBitmap", bmp)
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
    }
}