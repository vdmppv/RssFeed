package by.rssreader;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import by.rssreader.models.RssList;
import by.rssreader.parser.Parser;
import by.rssreader.parser.RssParser;

public class DownloadRssIntentService extends IntentService {
    private static final String TAG = DownloadRssIntentService.class.getSimpleName();

    public static final String PENDING_RESULT_EXTRA = "pending_result";
    public static final String URL_EXTRA = "url";
    public static final String RSS_RESULT_EXTRA = "url";

    public static final int RESULT_CODE = 0;
    public static final int INVALID_URL_CODE = 1;
    public static final int ERROR_CODE = 2;

    private Parser<RssList> parser;

    public DownloadRssIntentService() {
        super(TAG);
        parser = new RssParser();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        assert intent != null;
        PendingIntent reply = intent.getParcelableExtra(PENDING_RESULT_EXTRA);
        try {
            try {
                URL url = new URL(intent.getStringExtra(URL_EXTRA));
                InputStream in = url.openStream();
                RssList rss = parser.parse(in, new URL(intent.getStringExtra(URL_EXTRA)));

                Intent result = new Intent();
                result.putExtra(RSS_RESULT_EXTRA, (Parcelable) rss);

                reply.send(this, RESULT_CODE, result);
            } catch (MalformedURLException ex) {
                reply.send(INVALID_URL_CODE);
                Log.e(TAG, "", ex);
            } catch (Exception ex) {
                reply.send(ERROR_CODE);
                Log.e(TAG, "", ex);
            }
        } catch (PendingIntent.CanceledException ex) {
            Log.e(TAG, "Reply cancelled", ex);
        }
    }
}
