package student.arcadia.com.gratyfime;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private TextView titleView;
    private TextView endsView;

    public MyViewHolder(View itemView) {
        super(itemView);
        this.imageView = (ImageView) itemView.findViewById(R.id.mImage);
        this.titleView = (TextView) itemView.findViewById(R.id.mTitle);
        this.endsView = (TextView) itemView.findViewById(R.id.mEnds);
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setTitleView(TextView titleView) {
        this.titleView = titleView;
    }

    public TextView getTitleView() {
        return titleView;
    }

    public void setEndsView(TextView endsView) {
        this.endsView = endsView;
    }

    public TextView getEndsView() {
        return endsView;
    }
}