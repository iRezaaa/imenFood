package ir.bvar.imenfood.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import java.io.FileDescriptor;
import java.io.IOException;

import ir.bvar.imenfood.ui.activities.SendFactorActivity;

/**
 * Created by rezapilehvar on 14/6/2018 AD.
 */

public class BitmapUtility {
    public static Bitmap uriToBitmap(Context context, Uri selectedFileUri) {
        Bitmap bitmap = null;

        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    context.getContentResolver().openFileDescriptor(selectedFileUri, "r");
            assert parcelFileDescriptor != null;
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);


            parcelFileDescriptor.close();
        } catch (IOException e) {
            bitmap = null;
            e.printStackTrace();
        }

        return bitmap;
    }
}
