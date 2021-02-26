package by.rssreader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

import by.rssreader.models.Rss;
import by.rssreader.R;

import static androidx.core.content.ContextCompat.startActivity;

public class RssViewHolder extends RecyclerView.ViewHolder {
    private static DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH);

    private View mainView;
    private MaterialTextView title;
    private ImageView image;
    private MaterialTextView description;
    private MaterialTextView pubDate;
    private WebView articleView;

    public RssViewHolder(View itemView) {
        super(itemView);
        mainView = itemView;
        title = itemView.findViewById(R.id.title);
        image = itemView.findViewById(R.id.image);
        description = itemView.findViewById(R.id.description);
        pubDate = itemView.findViewById(R.id.pubDate);
    }

    void bind(Rss rss) {
        assert rss != null;
        mainView.setOnClickListener(v -> {
            if (MainActivity.connected)
                openWebView(mainView.getContext(), rss.getLink());
            else
                Snackbar.make(mainView, "No internet connection", BaseTransientBottomBar.LENGTH_LONG).show();
        });

        title.setText(rss.getTitle());
        description.setText(rss.getDescription());
        pubDate.setText(formatter.format(rss.getPubDate()));
        if (rss.getImageUrl().isEmpty()) {
            image.setVisibility(View.GONE);
        } else {
            setImage(rss, image);
        }
    }

    private void setImage(Rss rss, ImageView image) {
        if (Objects.isNull(rss.getImage()) && !rss.getImageUrl().isEmpty()) {
            Picasso.get()
                    .load(rss.getImageUrl())
                    .transform(new Transformation() {
                        @Override
                        public Bitmap transform(Bitmap source) {
                            rss.setImage(source);
                            return source;
                        }

                        @Override
                        public String key() {
                            return "";
                        }
                    })
                    .resize((int) image.getContext().getResources().getDimension(R.dimen.img_width),
                            (int) image.getContext().getResources().getDimension(R.dimen.img_height))
                    .centerCrop()
                    .into(image);
        } else {
            image.setImageBitmap(rss.getImage());
        }


    }

    private void openWebView(Context context, String link) {
        articleView = new WebView(context);
        articleView.getSettings().setLoadWithOverviewMode(true);
        articleView.getSettings().setJavaScriptEnabled(true);
        articleView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });
        articleView.loadUrl(link);
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("URL", link);
        context.startActivity(intent);
    }
}