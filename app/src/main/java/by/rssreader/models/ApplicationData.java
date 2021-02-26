package by.rssreader.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class ApplicationData implements Parcelable, Serializable {
    private static volatile ApplicationData appData;

    private String rssUrl;
    private RssList items;

    private ApplicationData() {
        rssUrl = "";
        items = new RssList();
    }

    public static ApplicationData getInstance() {
        if (appData == null) {
            synchronized (ApplicationData.class) {
                if (appData == null) {
                    appData = new ApplicationData();
                }
            }
        }
        return appData;
    }

    protected ApplicationData(Parcel in) {
        rssUrl = in.readString();
        items = in.readParcelable(RssList.class.getClassLoader());
    }

    public static final Creator<ApplicationData> CREATOR = new Creator<ApplicationData>() {
        @Override
        public ApplicationData createFromParcel(Parcel in) {
            return new ApplicationData(in);
        }

        @Override
        public ApplicationData[] newArray(int size) {
            return new ApplicationData[size];
        }
    };

    @Override
    public int describeContents() {
        return items.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rssUrl);
        dest.writeParcelable(items, flags);
    }

    public String getRssUrl() {
        return rssUrl;
    }

    public void setRssUrl(String rssUrl) {
        this.rssUrl = rssUrl;
    }

    public List<Rss> getItems() {
        return items.getItems();
    }

    public RssList getRssList() {
        return items;
    }

    public void setRssItems(RssList items) {
        this.items = items;
    }
}
