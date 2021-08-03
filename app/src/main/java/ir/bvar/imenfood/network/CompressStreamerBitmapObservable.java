package ir.bvar.imenfood.network;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import ir.bvar.imenfood.App;
import okhttp3.MediaType;
import okhttp3.MultipartBody;

/**
 * Created by rezapilehvar on 26/1/2018 AD.
 */

public class CompressStreamerBitmapObservable implements ObservableOnSubscribe<MultipartBody.Part> {
    private Bitmap bitmap;
    private UploadRequestBody.UploadRequestStreamerListener uploadRequestStreamerListener;
    private ImageView previewImageView;

    public CompressStreamerBitmapObservable(ImageView previewImageView, Bitmap bitmap, UploadRequestBody.UploadRequestStreamerListener uploadRequestStreamerListener) {
        this.bitmap = bitmap;
        this.uploadRequestStreamerListener = uploadRequestStreamerListener;
        this.previewImageView = previewImageView;
    }

    @Override
    public void subscribe(ObservableEmitter<MultipartBody.Part> emitter) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.4), (int) (bitmap.getHeight() * 0.4), true);
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 60, bos);

        if (App.getInstance().getActivityManager().getCurrentActivity() != null) {
            App.getInstance().getActivityManager().getCurrentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (CompressStreamerBitmapObservable.this.previewImageView != null) {
                        CompressStreamerBitmapObservable.this.previewImageView.setImageBitmap(resizedBitmap);
                    }
                }
            });
        }

        InputStream in = new ByteArrayInputStream(bos.toByteArray());
        UploadRequestBody uploadRequestBody = new UploadRequestBody(in, MediaType.parse("image/*"), uploadRequestStreamerListener);
        emitter.onNext(MultipartBody.Part.createFormData("file", "factor_" + System.currentTimeMillis() + ".jpg", uploadRequestBody));
    }
}
