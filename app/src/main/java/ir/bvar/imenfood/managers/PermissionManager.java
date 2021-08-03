package ir.bvar.imenfood.managers;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.support.v4.app.Fragment;

/**
 * Created by hp on 1/23/2017.
 */

public class PermissionManager extends BasePermissionManager {

    public PermissionManager(Activity activity) {
        super(activity);
    }

    public PermissionManager(Fragment fragment){
        super(fragment);
    }

    public boolean hasPermissionToReadContacts() {
        return hasPermission(Manifest.permission.READ_CONTACTS);
    }

    public boolean hasPermissionToWriteExStorage() {
        return hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public boolean hasPermissionToRecordAudio() {
        return hasPermission(Manifest.permission.RECORD_AUDIO);
    }

    public boolean hasPermissionToShowCamera() {
        return hasPermission(Manifest.permission.CAMERA);
    }

    public boolean hasPermissionReadExternalStorage() {
        return hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
    }


    public void askForRecordAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // TODO: Show real description.
            showPermissionDescriptionDialog(
                    "Audio record needed.",
                    "Please confirm audio record access",
                    Manifest.permission.RECORD_AUDIO,
                    PERMISSIONS_REQUEST_RECORD_AUDIO);
        }
    }

    public void askForWriteExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // TODO: Show real description.
            showPermissionDescriptionDialog(
                    "If You Want to Upload or Download Files in Nested, You must Grant Storage Access.",
                    "Please confirm write storage access",
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    PERMISSIONS_REQUEST_WRITE_EX_STORAGE);
        }
    }

    public void askForReadContactsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // TODO: Show real description.
            showPermissionDescriptionDialog(
                    "In Order to Suggest Your Contacts, We Need Access to Them",
                    "Please confirm contacts access",
                    Manifest.permission.READ_CONTACTS,
                    PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }

    public void askForCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showPermissionDescriptionDialog(
                    "In Order to show the camera and take picture we need your permission",
                    "Please confirm camera access",
                    Manifest.permission.CAMERA,
                    PERMISSIONS_REQUEST_CAMERA);
        }
    }

    public void askForReadExternalPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showPermissionDescriptionDialog(
                    "In Order to show the camera and take picture we need your permission",
                    "Please confirm camera access",
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    PERMISSIONS_REQUEST_READ_EX_STORAGE);
        }
    }

}
