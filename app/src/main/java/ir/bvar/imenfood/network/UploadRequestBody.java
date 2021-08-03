package ir.bvar.imenfood.network;

import java.io.IOException;
import java.io.InputStream;

import ir.bvar.imenfood.App;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Created by rezapilehvar on 26/1/2018 AD.
 */

public class UploadRequestBody extends RequestBody {
    private static final int SEGMENT_SIZE = 2048; // okio.Segment.SIZE

    private InputStream inputStream;
    private MediaType mediaType;
    private UploadRequestStreamerListener uploadRequestStreamerListener;
    private long totalSize = 0;

    public UploadRequestBody(InputStream inputStream, MediaType mediaType, UploadRequestStreamerListener uploadRequestStreamerListener) {
        this.inputStream = inputStream;
        this.mediaType = mediaType;
        this.uploadRequestStreamerListener = uploadRequestStreamerListener;
    }

    @Override
    public MediaType contentType() {
        return mediaType;
    }

    @Override
    public long contentLength() throws IOException {
        this.totalSize = inputStream.available();
        return totalSize;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {

        Source source = null;
        try {
            source = Okio.source(inputStream);
            long transfer = 0;
            long read;

            while ((read = source.read(sink.buffer(), SEGMENT_SIZE)) != -1) {
                transfer += read;
                sink.flush();

                if (uploadRequestStreamerListener != null) {
                    final long finalTransfer = transfer;
                    App.getInstance().getActivityManager().getCurrentActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int progress = (int) ((finalTransfer * 100) / totalSize);
                            uploadRequestStreamerListener.onProgressChanged(progress);
                        }
                    });
                }
            }
        } finally {
            Util.closeQuietly(source);
        }
    }

    public interface UploadRequestStreamerListener {
        void onProgressChanged(int progress);
    }
}
