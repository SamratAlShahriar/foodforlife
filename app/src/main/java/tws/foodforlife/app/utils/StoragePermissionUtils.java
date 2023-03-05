package tws.foodforlife.app.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

public class StoragePermissionUtils {
    public static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    public static final int RC_STORAGE = 201;

    public static boolean isPermitted(Context context) {
        if ((ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        }
        return false;
    }

    public static void requestPermissions(FragmentActivity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE}, RC_STORAGE);
    }
}
