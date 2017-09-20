package biz.chundi.geeknews;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import biz.chundi.geeknews.data.model.Article;
import biz.chundi.geeknews.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnListArticleListener {

    /*
    https://learnpainless.com/android/how-to-get-fragment-from-viewpager-android
     */
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    public String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Utility.setNewsSource(getString(R.string.wiredSrc));
                item.setChecked(true);

            case R.id.item2:
                Utility.setNewsSource(getString(R.string.recodeSrc));
                item.setChecked(true);

            case R.id.item3:
                Utility.setNewsSource(getString(R.string.arsSrc));
                item.setChecked(true);

            case R.id.item4:
                Utility.setNewsSource(getString(R.string.engadgetSrc));
                item.setChecked(true);

            case R.id.item5:
                Utility.setNewsSource(getString(R.string.hackernewsSrc));
                item.setChecked(true);

            default:
                Utility.setNewsSource(getString(R.string.wiredSrc));


        }
        Log.d(LOG_TAG," OPTION SELECTED "+item.toString());
//        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + 0);
//        TopFragment topFragment = (TopFragment) page;
//        topFragment.loadArticles();
        mViewPager.getAdapter().notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onArticleClick(long id) {

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //            return new TopFragment();
            switch (position) {
                case 0:
                    return new TopFragment();
                case 1:
                    return new LatestFragment();
                case 2:
                    return new PopularFragment();
            }
            return null;

        }


        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "TOP";
                case 1:
                    return "LATEST";
                case 2:
                    return "POPULAR";
            }
            return null;
        }

    }
}

