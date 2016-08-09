package org.tian.permission.lib;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ctrip on 16/8/9 下午3:05.
 */
public class PermissionCheck {

    private static List<String> mGrantedPermissions = new ArrayList<String>();
    private static List<String> mDeniedPermissions = new ArrayList<String>();
    private static List<String> mUnshowedPermissions = new ArrayList<String>();
    private static List<String> mNeedRequestPermissions = new ArrayList<String>();
    private PermissionCheck() {

    }

    /**
     * check granted and denied permission
     */
    public static void checkGrantedAndDeniedPermissions(Context context, String... permissions){
        if(context == null){
            return ;
        }
        if(permissions == null || permissions.length < 1){
            return ;
        }
        mGrantedPermissions.clear();
        mDeniedPermissions.clear();
        mUnshowedPermissions.clear();
        mNeedRequestPermissions.clear();

        try {
            for (String permission : permissions) {
                if (PermissionChecker.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                    mGrantedPermissions.add(permission);
                } else {
                    mDeniedPermissions.add(permission);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkUnshowPermission(Activity activity, String... permissions) {
        if(activity == null){
            return ;
        }
        mUnshowedPermissions.clear();
        mNeedRequestPermissions.clear();
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                mNeedRequestPermissions.add(permission);
            } else {
                mUnshowedPermissions.add(permission);
            }
        }
    }
    public static void checkUnshowPermissionByFragment(Fragment fragment, String... permissions) {
        if(fragment == null){
            return ;
        }

        mUnshowedPermissions.clear();
        mNeedRequestPermissions.clear();
        for (String permission : permissions) {
            if (fragment.shouldShowRequestPermissionRationale(permission)) {
                mNeedRequestPermissions.add(permission);
            } else {
                mUnshowedPermissions.add(permission);
            }
        }
    }

    public static List<String> getGrantedPermissions() { return mGrantedPermissions; }

    public static List<String> getDeniedPermissions() {
        return mDeniedPermissions;
    }

    public static List<String> getUnshowedPermissions() {
        return mUnshowedPermissions;
    }

    public static List<String> getNeedRequestPermissions() {
        return mNeedRequestPermissions;
    }



}
