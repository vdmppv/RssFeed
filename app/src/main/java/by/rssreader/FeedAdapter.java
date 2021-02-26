package by.rssreader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import by.rssreader.models.Rss;
import by.rssreader.R;

public class FeedAdapter extends RecyclerView.Adapter<RssViewHolder> {
    private List<Rss> data = new ArrayList<>();

    @NonNull
    @Override
    public RssViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new RssViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RssViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Rss> newData) {
        data = newData;
        notifyDataSetChanged();
    }
}
