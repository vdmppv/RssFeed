package by.rssreader.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RssList implements Parcelable, Serializable {
    private List<Rss> items;

    public RssList(List<Rss> items) {
        this.items = items;
    }

    public RssList() {
        this(new ArrayList<>());
    }

    public RssList(Parcel in) {
        items = in.createTypedArrayList(Rss.CREATOR);
    }

    public static final Creator<RssList> CREATOR = new Creator<RssList>() {
        @Override
        public RssList createFromParcel(Parcel in) {
            return new RssList(in);
        }

        @Override
        public RssList[] newArray(int size) {
            return new RssList[size];
        }
    };

    @Override
    public int describeContents() {
        return !items.isEmpty() ? items.get(0).describeContents() : 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(items);
    }

    public void addItem(Rss rss) {
        items.add(rss);
    }

    public int size() {
        return items.size();
    }

    public List<Rss> getItems() {
        return items;
    }

    public void setItems(List<Rss> items) {
        this.items = items;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(items);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        items = (List) in.readObject();
    }
}
