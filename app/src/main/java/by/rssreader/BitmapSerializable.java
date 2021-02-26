package by.rssreader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class BitmapSerializable implements Serializable, Parcelable {
    private Bitmap bitmap;

    public static final Creator<BitmapSerializable> CREATOR = new Creator<BitmapSerializable>() {
        @Override
        public BitmapSerializable createFromParcel(Parcel in) {
            return new BitmapSerializable(in);
        }

        @Override
        public BitmapSerializable[] newArray(int size) {
            return new BitmapSerializable[size];
        }
    };

    public BitmapSerializable(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    protected BitmapSerializable(Parcel in) {
        this((Bitmap) in.readParcelable(Bitmap.class.getClassLoader()));
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteStream);
        byte[] bitmapBytes = byteStream.toByteArray();
        out.write(bitmapBytes, 0, bitmapBytes.length);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int b;
        while ((b = in.read()) != -1)
            byteStream.write(b);
        byte[] bitmapBytes = byteStream.toByteArray();
        bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public int describeContents() {
        return bitmap.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(bitmap, flags);
    }
}
