package cn.ac.ict.alpha.Entities;

/**
 * Author: saukymo
 * Date: 9/12/16
 */
public class PermissionEntity {
    public final String permissionName;
    public Boolean permissionStatus;

    public PermissionEntity(String permission) {
        permissionName = permission;
        permissionStatus = true;
    }

}
