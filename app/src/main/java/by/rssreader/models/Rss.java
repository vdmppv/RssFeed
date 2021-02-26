package by.rssreader.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import by.rssreader.BitmapSerializable;

public class Rss implements Parcelable, Serializable {
    private String title;
    private String description;
    private String link;
    private String imageUrl;
    private BitmapSerializable image;
    private Date pubDate;

    public Rss(String title, String description, String link, String imageUrl, BitmapSerializable image, Date pubDate) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.imageUrl = imageUrl;
        this.image = image;
        this.pubDate = pubDate;
    }

    public Rss() {
        this("", "", "", "", null, null);
    }

    protected Rss(Parcel in) {
        this(
                in.readString(),
                in.readString(),
                in.readString(),
                in.readString(),
                in.readParcelable(BitmapSerializable.class.getClassLoader()),
                (Date) in.readSerializable()
        );
    }

    public static final Creator<Rss> CREATOR = new Creator<Rss>() {
        @Override
        public Rss createFromParcel(Parcel in) {
            return new Rss(in);
        }

        @Override
        public Rss[] newArray(int size) {
            return new Rss[size];
        }
    };

    @Override
    public int describeContents() {
        return image.getBitmap().describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(link);
        dest.writeString(imageUrl);
        dest.writeParcelable(image, flags);
        dest.writeSerializable(pubDate);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Bitmap getImage() {
        return Objects.nonNull(image) ? image.getBitmap() : null;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setImage(Bitmap image) {
        this.image = new BitmapSerializable(image);
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }
}
