package by.rssreader;

import android.app.Application;
import android.content.Intent;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import by.rssreader.models.ApplicationData;
import by.rssreader.models.Rss;
import by.rssreader.models.RssList;

public class ApplicationViewModel extends AndroidViewModel {

    private MutableLiveData<List<Rss>> rssListLive;
    private final ApplicationData applicationData;

    public ApplicationViewModel(Application application) {
        super(application);
        rssListLive = new MutableLiveData<>();
        applicationData = DataUtils.loadData(getApplication());
        rssListLive.postValue(applicationData.getItems());
    }

    protected void updateRssList(Intent data) {
        RssList rssList = data.getParcelableExtra(DownloadRssIntentService.RSS_RESULT_EXTRA);
        applicationData.setRssItems(rssList);
        rssListLive.postValue(applicationData.getItems());
    }

    public String getRssUrl() {
        return applicationData.getRssUrl();
    }

    public void setRssUrl(String url) {
        applicationData.setRssUrl(url);
    }

    public LiveData<List<Rss>> getRssListLiveData() {
        return rssListLive;
    }

    public void saveData() {
        DataUtils.saveData(applicationData, getApplication());
    }
}
