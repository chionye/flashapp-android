package com.example.myapplication.Helper;

import android.util.Log;

public class Logger {
    public static void Logger(String message) {
        Log.d("Debug function", message);
    }

    public static void Logger(String key, String message) {
        Log.d(key, message);
    }
}
