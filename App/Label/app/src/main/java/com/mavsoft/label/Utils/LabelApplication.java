package com.mavsoft.label.Utils;

import android.app.Application;
import android.content.Context;

public class LabelApplication extends Application {

        private static Context context;

        public void onCreate() {
            super.onCreate();
            LabelApplication.context = getApplicationContext();
        }

        public static Context getAppContext() {
            return LabelApplication.context;
        }
}
