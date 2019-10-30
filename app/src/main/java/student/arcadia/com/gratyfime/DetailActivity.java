package student.arcadia.com.gratyfime;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.viewpagerindicator.CirclePageIndicator;

public class DetailActivity extends ActionBarActivity {

    private final int delay_slideshow = 2000;
    private Record record;
    private MyViewPager viewPager;
    private ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Gson gson = new Gson();
        record = gson.fromJson(bundle.getString("json"), Record.class);

        TextView title = (TextView) findViewById(R.id.title);
        if (title != null) title.setText(record.getTitle());

        viewPager = (MyViewPager) findViewById(R.id.view_pager);
        adapter = new ImageAdapter(DetailActivity.this, record.getImages());
        viewPager.setAdapter(adapter);
        final CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.circle);
        indicator.setViewPager(viewPager);

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                indicator.setCurrentItem(position);
                viewPager.setCurrentItem(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, delay_slideshow);
            }
        });
        TextView description = (TextView) findViewById(R.id.detail);
        if (description != null) description.setText(record.getDescription());

        TextView busyness = (TextView) findViewById(R.id.company_name);
        if (busyness != null) busyness.setText(record.getBusiness_id());

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.company);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, MapActivity.class);
                Gson gson = new Gson();
                intent.putExtra("json", gson.toJson(record));
                startActivity(intent);
            }
        });

        TextView remaining = (TextView) findViewById(R.id.remaining);
        if (remaining != null)
            remaining.setText(String.format("Only %d remaining!", record.getQuantity()));

        TextView ends = (TextView) findViewById(R.id.ends);
        if (ends != null) ends.setText(record.getEnd());

        ExpandableTextView expandableTextView = (ExpandableTextView) findViewById(R.id.expand_text_view);
        expandableTextView.setText(String.format("%s\n%s", getString(R.string.conditions_label), record.getTerms()));
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            int position = viewPager.getCurrentItem();
            int count = adapter.getCount();
            if (position >= count - 1) {
                position = 0;
            } else {
                position++;
            }
            viewPager.setCurrentItem(position, true);
            handler.postDelayed(runnable, delay_slideshow);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, delay_slideshow);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                NavUtils.navigateUpTo(this, intent);
                return true;

            case R.id.share_offer:
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);

                share.putExtra(Intent.EXTRA_SUBJECT, record.getTitle());
                share.putExtra(Intent.EXTRA_TITLE, record.getTitle());
                share.putExtra(Intent.EXTRA_TEXT, String.format("%s: %s%d", record.getTitle(), getString(R.string.offer_id_link), record.getId()));

                startActivity(Intent.createChooser(share, "Share this offer with:"));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
