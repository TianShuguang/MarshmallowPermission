package org.tian.permission.lib;

/**
 * Created by ctrip on 16/8/9 下午2:02.
 */
public interface PermissionListener {
    /**
     * call when permissions are granted
     * 已授权
     * @param requestCode
     * @param grantResults
     * @param permissions
     */

    public void onPermissionsGranted(int requestCode, int[] grantResults, String... permissions);

    /**
     * call when one or some permissions are denied
     * 未授权
     * @param requestCode
     * @param grantResults
     * @param permissions
     */
    public void onPermissionsDenied(int requestCode, int[] grantResults, String... permissions);

    /**
     * show the permissions rationale whether or not(why your app need their permissions?)
     * 用户已拒绝权限申请
     * @param requestCode
     * @param isShowRationale
     * @param permissions
     */
    public void onShowRequestPermissionRationale(int requestCode, boolean isShowRationale, String... permissions);

    /**
     * get a permissions error: almost params are wrong
     * 失败
     * @param requestCode
     * @param grantResults
     * @param errorMsg
     * @param permissions
     */
    public void onPermissionsError(int requestCode, int[] grantResults, String errorMsg, String... permissions);

}
