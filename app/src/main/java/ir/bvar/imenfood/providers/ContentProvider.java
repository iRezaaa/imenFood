package ir.bvar.imenfood.providers;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;

/**
 * Created by rezapilehvar on 29/6/2018 AD.
 */

public class ContentProvider extends android.content.ContentProvider {
    public static final String CONTENT_PROVIDER_AUTHORITY = "ir.bvar.imenfood.providers.ContentProvider";

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    public static Uri getPhotoUri(File file) {
        Uri outputUri = Uri.fromFile(file);
        Uri.Builder builder = new Uri.Builder()
                .authority(CONTENT_PROVIDER_AUTHORITY)
                .scheme("file")
                .path(outputUri.getPath())
                .query(outputUri.getQuery())
                .fragment(outputUri.getFragment());

        return builder.build();
    }
}
