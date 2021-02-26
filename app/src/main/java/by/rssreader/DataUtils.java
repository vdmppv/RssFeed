package by.rssreader;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import by.rssreader.models.ApplicationData;
import by.rssreader.models.RssList;

public final class DataUtils {

    private static final String FILE_NAME = "RSS";
    private static final int MAX_RSS_COUNT = 10;

    private DataUtils() {
        throw new UnsupportedOperationException();
    }

    public static void saveData(ApplicationData applicationData, Context context) {
        try (FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
             ObjectOutputStream os = new ObjectOutputStream(fos)) {
            os.writeUTF(applicationData.getRssUrl());
            RssList rssList = applicationData.getRssList();
            if (rssList.getItems().size() > MAX_RSS_COUNT)
                rssList.setItems(new ArrayList<>(rssList.getItems().subList(0, MAX_RSS_COUNT)));
            os.writeObject(rssList);
        } catch (Exception e) {
            Log.e(DataUtils.class.getSimpleName(), "", e);
        }
    }

    public static ApplicationData loadData(Context context) {
        ApplicationData applicationData = ApplicationData.getInstance();
        try (FileInputStream fis = context.openFileInput(FILE_NAME);
             ObjectInputStream is = new ObjectInputStream(fis)) {
            applicationData.setRssUrl(is.readUTF());
            applicationData.setRssItems((RssList) is.readObject());
        } catch (Exception e) {
            Log.e(DataUtils.class.getSimpleName(), "", e);
        }
        return applicationData;
    }
}
