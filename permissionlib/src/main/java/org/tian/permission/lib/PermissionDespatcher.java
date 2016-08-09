package org.tian.permission.lib;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import java.util.List;

/**
 * Created by ctrip on 16/8/9 下午1:54.
 */
public class PermissionDespatcher {
    private static final String TAG=PermissionDespatcher.class.getSimpleName();

    private static boolean mIsShowDialog = true;



    /**
     * request permissions to be granted
     *
     * @param act
     * @param requestCode
     * @param permissions
     */
    public static void requestPermissions(Activity act, int requestCode, String... permissions) {
        ActivityCompat.requestPermissions(act, permissions, requestCode);
    }

    /**
     * request permissions to be granted for fragment
     *
     * @param fragment
     * @param requestCode
     * @param permissions
     */
    public static void requestPermissionsByFragment(Fragment fragment, int requestCode, String... permissions) {

        fragment.requestPermissions(permissions, requestCode);
    }



    public static void checkPermissions(final Activity act, int requestCode, PermissionListener listener, boolean isShowDialog, String... permissions) {
        mIsShowDialog = isShowDialog;
        checkPermissions(act, requestCode, listener, permissions);
    }

    /**
     * check permissions are whether granted or not
     *
     * @param obj
     * @param requestCode
     * @param listener
     * @param permissions
     */
    public static void checkPermissions(final Object obj, int requestCode, PermissionListener listener, String... permissions) {

        if (PermissionUtils.isOverMarshmallow()){
            return;
        }

        if (obj == null) {
            if (listener != null) {
                listener.onPermissionsError(requestCode, null, "checkPermissions()-->param act :the obj is null", permissions);
            }
            return;
        }
        if (permissions == null || permissions.length < 1) {
            if (listener != null) {
                listener.onPermissionsError(requestCode, null, "checkPermissions()-->param permissions: is null or length is 0", permissions);
            }
            return;
        }

        if (obj instanceof Activity){
            PermissionCheck.checkGrantedAndDeniedPermissions((Activity)obj,permissions);
        }else if (obj instanceof Fragment){
            PermissionCheck.checkGrantedAndDeniedPermissions(((Fragment) obj).getActivity(),permissions);
        }else {
            throw new IllegalArgumentException(obj.getClass().getName()+"is not supported");
        }

        if (PermissionCheck.getGrantedPermissions().size() > 0) {
            List<String> grantedPermissionsList = PermissionCheck.getGrantedPermissions();
            String[] grantedPermissionsArr = grantedPermissionsList.toArray(new String[grantedPermissionsList.size()]);

            if (listener != null) {
                listener.onPermissionsGranted(requestCode, null, grantedPermissionsArr);
            }
        }

        if (PermissionCheck.getDeniedPermissions().size() > 0) {
            List<String> deniedPermissionsList = PermissionCheck.getDeniedPermissions();
            String[] deniedPermissionsArr = deniedPermissionsList.toArray(new String[deniedPermissionsList.size()]);
            if (deniedPermissionsArr.length > 0) {
                if (obj instanceof Activity){
                    PermissionCheck.checkUnshowPermission((Activity) obj, deniedPermissionsArr);
                }else if (obj instanceof Fragment){
                    PermissionCheck.checkUnshowPermissionByFragment((Fragment) obj, deniedPermissionsArr);
                }
            }
        }

        if (PermissionCheck.getUnshowedPermissions().size() > 0) {
            List<String> unShowPermissionsList = PermissionCheck.getUnshowedPermissions();
            String[] unShowPermissionsArr = unShowPermissionsList.toArray(new String[unShowPermissionsList.size()]);
            if (listener != null) {
                if(true == mIsShowDialog) {
                    //show dialog
                }

                listener.onShowRequestPermissionRationale(requestCode, false, unShowPermissionsArr);
            }
        }

        mIsShowDialog = true;

        if (PermissionCheck.getNeedRequestPermissions().size() > 0) {//true 表示允许弹申请权限框
            List<String> needRequestPermissionsList = PermissionCheck.getNeedRequestPermissions();
            String[] needRequestPermissionsArr = needRequestPermissionsList.toArray(new String[needRequestPermissionsList.size()]);
            if (listener != null) {
                listener.onShowRequestPermissionRationale(requestCode, true, needRequestPermissionsArr);
            }
        }

    }


    private static void gotoPermissionSetting(Activity act) {
        Uri packageURI = Uri.parse("package:" + act.getPackageName());
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        act.startActivity(intent);
    }

}
