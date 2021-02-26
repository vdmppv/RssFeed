package by.rssreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import by.rssreader.R;

public class MainActivity extends AppCompatActivity {
    private static final int RSS_DOWNLOAD_REQUEST_CODE = 0;
    public static boolean connected = true;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private FeedAdapter feedAdapter;
    private ApplicationViewModel viewModel;
    private ConnectivityDetectorBroadcastReceiver detectorBroadcastReceiver = new ConnectivityDetectorBroadcastReceiver() {
        @Override
        public void onChangedConnectionState(boolean connected) {
            MainActivity.connected = connected;
            updateConnectionInfo();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        feedAdapter = new FeedAdapter();
        recyclerView = findViewById(R.id.rss_feed);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        recyclerView.setAdapter(feedAdapter);
        swipeRefreshLayout = findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this::requestRssData);
        viewModel = ViewModelProviders.of(this).get(ApplicationViewModel.class);
        viewModel.getRssListLiveData().observe(this, feedAdapter::setData);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.setPriority(999);
        registerReceiver(detectorBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RSS_DOWNLOAD_REQUEST_CODE) {
            swipeRefreshLayout.setRefreshing(false);
            switch (resultCode) {
                case DownloadRssIntentService.INVALID_URL_CODE:
                    Snackbar.make(recyclerView, "Incorrect URL. Please, set correct rss feed url.", BaseTransientBottomBar.LENGTH_LONG).show();
                    break;
                case DownloadRssIntentService.ERROR_CODE:
                    Snackbar.make(recyclerView, "Unexpected error. Please, check internet connection or try again later", BaseTransientBottomBar.LENGTH_LONG).show();
                    break;
                case DownloadRssIntentService.RESULT_CODE:
                    viewModel.updateRssList(data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_set_feed) {
            requestRssFeed();
            return true;
        } else if (id == R.id.menu_refresh) {
            swipeRefreshLayout.setRefreshing(true);
            requestRssData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void requestRssData() {
        if (connected) {
            PendingIntent pendingResult = createPendingResult(RSS_DOWNLOAD_REQUEST_CODE, new Intent(), 0);
            Intent intent = new Intent(getApplication(), DownloadRssIntentService.class);
            intent.putExtra(DownloadRssIntentService.URL_EXTRA, viewModel.getRssUrl());
            intent.putExtra(DownloadRssIntentService.PENDING_RESULT_EXTRA, pendingResult);
            getApplication().startService(intent);
        } else {
            Snackbar.make(recyclerView, "No internet connection", BaseTransientBottomBar.LENGTH_LONG).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    protected void requestRssFeed() {
        final EditText edittext = new EditText(getApplication());
        edittext.setText(viewModel.getRssUrl());
        new MaterialAlertDialogBuilder(this)
                .setTitle("Enter RSS Feed Url")
                .setView(edittext)
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Ok", (dialog, whichButton) -> {
                    viewModel.setRssUrl(edittext.getText().toString());
                    swipeRefreshLayout.setRefreshing(true);
                    requestRssData();
                })
                .show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(detectorBroadcastReceiver);
        viewModel.saveData();
    }

    protected void updateConnectionInfo() {
        String appName = getResources().getString(R.string.app_name);
        if (!connected)
            appName += " (offline)";
        setTitle(appName);
    }
}
