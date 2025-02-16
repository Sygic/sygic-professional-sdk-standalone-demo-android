package com.sygic.example.ipcdemo3d;


import android.app.Application;
import android.os.Environment;

/**
 * Used to save global state and to provide application general constants
 */
public class SdkApplication extends Application {

    public static String PATH_GPS_LOG;
    public static String PATH_ROUTE;
    public static String PATH_ITINERARY;
    public static String PATH_BTIMAP;
    public static String PATH_GF;
    public static String PATH_VOICES_2D;
    public static String PATH_VOICES_PERSON_2D;
    public static String PATH_LANGS;
    public static final int MAX = 20000;

    private static final String PACKAGE_NAME = "com.sygic";
    public static final String INTENT_ACTION_NO_SDCARD = PACKAGE_NAME + ".intent.action.noSdcard";
    public static final String INTENT_ACTION_NO_DRIVE = PACKAGE_NAME + ".intent.action.noDrive";
    public static final String INTENT_ACTION_GPS_CLOSED = PACKAGE_NAME + ".intent.action.closedGps";
    public static final String INTENT_ACTION_SERVICE_DESTROYED = PACKAGE_NAME + ".intent.action.onDestroy";
    public static final String INTENT_ACTION_APP_STARTED = PACKAGE_NAME + ".intent.action.app_started";
    public static final String INTENT_ACTION_APP_STARTED_LOCAL = PACKAGE_NAME + ".intent.action.app_started_local";
    public static final String INTENT_CHANGE_STATE = PACKAGE_NAME + ".intent.change.state";
    public static final String INTENT_CHANGE_MENU_STATE = PACKAGE_NAME + ".intent.change_menu.state";
    public static final String INTENT_ACTION_AM_WAKEUP = PACKAGE_NAME + ".intent.action.am_wakeup";

    public static final int ZOOM = 5000;

    private static boolean mService = false;

    @Override
    public void onCreate() {
        super.onCreate();
        String prefix = Environment.getExternalStorageDirectory() + "/Android/data/" + BuildConfig.NAVI_PACKAGE+"/files/SygicTruck/";
        PATH_GPS_LOG = prefix+"Res/gpslogs";
        PATH_ROUTE = prefix+"Res/routes";
        PATH_ITINERARY = prefix+"Res/itinerary";
        PATH_BTIMAP = prefix+"Res/icons/rupi";
        PATH_GF = prefix+"Res/geofiles";
        PATH_VOICES_2D = prefix+"Res/voices";
        PATH_VOICES_PERSON_2D = prefix+"LoquendoTTS/modules";
        PATH_LANGS = prefix+"Res/skin/langs";
    }

    public static boolean isService() {
        return mService;
    }

    public static void setService(boolean isService) {
        mService = isService;
    }
}
