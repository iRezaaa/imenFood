package ir.bvar.imenfood.managers;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;

import ir.bvar.imenfood.App;

/**
 * Created by hp on 1/23/2017.
 */

public abstract class BasePermissionManager {

    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1010;
    static final int PERMISSIONS_REQUEST_WRITE_EX_STORAGE = 1020;
    static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1030;
    public static final int PERMISSIONS_REQUEST_CAMERA = 1040;
    public static final int PERMISSIONS_REQUEST_READ_EX_STORAGE = 1050;

    private Activity mActivity;
    private Fragment mFragment;

    BasePermissionManager(Activity activity) {
        this.mActivity = activity;
    }

    BasePermissionManager(Fragment fragment) {
        this.mFragment = fragment;
    }

    /**
     * Check the SDK version and whether the permission is already granted or not.
     */
    boolean hasPermission(String permissionName) {
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && App.getInstance().checkSelfPermission(permissionName) != PackageManager.PERMISSION_GRANTED);
    }

    void showPermissionDescriptionDialog(String title, String message, final String permission, final int permissionRequestCode) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mActivity != null) {
                mActivity.requestPermissions(new String[]{permission}, permissionRequestCode);
            } else if (mFragment != null) {
                mFragment.requestPermissions(new String[]{permission}, permissionRequestCode);
            }
        }

    }

}
