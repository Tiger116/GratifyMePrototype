package student.arcadia.com.gratyfime;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageAdapter extends PagerAdapter {
    private Context context;
    private String[] images = null;

    ImageAdapter(Context context, String[] urls) {
        this.context = context;
        this.images = urls;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_medium);
        imageView.setPadding(padding, padding, padding, padding);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        Picasso.with(context).load(images[position]).centerCrop().resize(dm.widthPixels, dm.heightPixels / 3).into(imageView);
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }
}