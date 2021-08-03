package ir.bvar.imenfood.ui.dialogs;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.bvar.imenfood.R;
import ir.bvar.imenfood.constants.RequestCodes;
import ir.bvar.imenfood.managers.PermissionManager;
import ir.bvar.imenfood.providers.ContentProvider;

/**
 * Created by rezapilehvar on 21/1/2018 AD.
 */

public class MediaTypePickerDialog extends AppCompatDialog {

    @BindView(R.id.dialog_mediapicker_galleryAction)
    LinearLayout galleryAction;

    @BindView(R.id.dialog_mediapicker_cameraAction)
    LinearLayout cameraAction;

    PermissionManager permissionManager;

    private Uri cameraOutPutUri;

    public MediaTypePickerDialog(Context context) {
        super(context);
    }

    public MediaTypePickerDialog(Context context, int theme) {
        super(context, theme);
    }

    protected MediaTypePickerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void show(Activity activity, PermissionManager permissionManager) {
        setOwnerActivity(activity);
        this.permissionManager = permissionManager;
        show();
    }

    private void init() {
        setContentView(R.layout.dialog_mediapicker);
        ButterKnife.bind(this);

        if (getWindow() != null)
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        cameraAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getOwnerActivity() != null) {
                    if (permissionManager.hasPermissionToShowCamera()) {
                        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        File file = new File(Environment.getExternalStorageDirectory(), "IMG_" + System.currentTimeMillis());


                        ContentResolver cr = getContext().getContentResolver();
                        try {
                            cameraOutPutUri = ContentProvider.getPhotoUri(new File(getContext().getCacheDir(), "cropped"));
                            cr.notifyChange(cameraOutPutUri, null);

                        }catch (Exception e){
                            e.printStackTrace();
                            cameraOutPutUri = Uri.fromFile(file);
                        }


                        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraOutPutUri);
                        getOwnerActivity().startActivityForResult(captureIntent, RequestCodes.PICK_FROM_CAMERA);
                    } else {
                        permissionManager.askForCameraPermission();
                    }
                }

                dismiss();
            }
        });

        galleryAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getOwnerActivity() != null) {
                    if (permissionManager.hasPermissionReadExternalStorage()) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        getOwnerActivity().startActivityForResult(galleryIntent, RequestCodes.PICK_FROM_GALLERY);
                    } else {
                        permissionManager.askForReadExternalPermission();
                    }
                }
                dismiss();
            }
        });
    }

    public Uri getCameraOutPutUri() {
        return cameraOutPutUri;
    }
}
