package student.arcadia.com.gratyfime;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<Record> records;
    private Context context;

    RecyclerViewAdapter(List<Record> records, Context context) {
        this.records = records;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_item, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        Record record = records.get(i);
        Uri uri = Uri.parse(record.getImages()[0]);
        Picasso.with(context).load(uri).into(viewHolder.getImageView());
        viewHolder.getTitleView().setText(record.getTitle());
        viewHolder.getEndsView().setText(String.format("%d offers left\n%s", record.getQuantity(), record.getEnd()));
    }

    @Override
    public int getItemCount() {
        return records.size();
    }
}