package boeren.com.appsuline.app.bmedical.appsuline.utils;

import android.util.Log;

public class Logger {
    private static final boolean IS_DEBUG=false;

    public static int i (String tag, String msg){
        if(IS_DEBUG){
            return Log.i(tag,msg);
        }
       return 0;
    }

    public static int d(String tag, String msg){
        if(IS_DEBUG){
            return Log.d(tag,msg);
        }
        return 0;
    }

    public static int v(String tag, String msg){
        if(IS_DEBUG){
            return Log.v(tag,msg);
        }
        return 0;
    }

}
