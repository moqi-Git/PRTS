package com.moqi.prts.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import com.moqi.prts.R

class TreeWidgetProvider: AppWidgetProvider() {

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.e("asdfg", "onReceive")
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.e("asdfg", "onUpdate")
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        Log.e("asdfg", "onAppWidgetOptionsChanged")
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        Log.e("asdfg", "onDeleted")
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        Log.e("asdfg", "onEnabled")
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        Log.e("asdfg", "onDisabled")
    }

    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)
        Log.e("asdfg", "onRestored")
    }
}