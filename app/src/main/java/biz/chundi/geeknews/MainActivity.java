package biz.chundi.geeknews;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
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

import static android.R.id.edit;

public class MainActivity extends AppCompatActivity  {

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

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
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
        pref = getPreferences(MODE_PRIVATE);

        //Code to remember previously selected tab

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                               @Override
                                               public void onTabSelected(TabLayout.Tab tab) {
                                                   int selTab = tab.getPosition();

                                                   mViewPager.setCurrentItem(selTab);
                                                   SharedPreferences.Editor editor = pref.edit();
                                                   editor.putInt("Tab", selTab);
                                                   editor.apply();
                                               }

                                               @Override
                                               public void onTabUnselected(TabLayout.Tab tab) {

                                               }

                                               @Override
                                               public void onTabReselected(TabLayout.Tab tab) {

                                               }
                                           });


        int selectedTabIndex = pref.getInt("Tab",1);
        TabLayout.Tab selectedTab = tabLayout.getTabAt(selectedTabIndex);
        selectedTab.select();

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
        // Retrieve the last checked value and set accordingly.
        pref = getPreferences(MODE_PRIVATE);
        String menuSelected = pref.getString("NewsSrc","wired-de");
        int menuItemId=0;
        switch(menuSelected) {
            case "wired.de":
                menuItemId = 0;
                break;
            case "recode":
                menuItemId = 1;
                break;
            case "ars-technica":
                menuItemId = 2;
                break;
            case "engadget":
                menuItemId = 3;
                break;
            case "hacker-news":
                menuItemId = 4;
                break;

        }
        MenuItem item = menu.getItem(menuItemId);
        item.setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        pref = getPreferences(MODE_PRIVATE); // 0 - for private mode
        editor = pref.edit();
        switch (id) {
            case R.id.item1:
                Utility.setNewsSource(getString(R.string.wiredSrc));
                item.setChecked(true);
                editor.putString("NewsSrc","wired-de");
                Log.d(LOG_TAG," SHARED PREF : " + "wired-de");
                break;
            case R.id.item2:
                Utility.setNewsSource(getString(R.string.recodeSrc));
                item.setChecked(true);
                editor.putString("NewsSrc","recode");
                Log.d(LOG_TAG," SHARED PREF : " + "recode");
                break;
            case R.id.item3:
                Utility.setNewsSource(getString(R.string.arsSrc));
                item.setChecked(true);
                editor.putString("NewsSrc","ars-technica");
                Log.d(LOG_TAG," SHARED PREF : " + "ars-technica");
                break;
            case R.id.item4:
                Utility.setNewsSource(getString(R.string.engadgetSrc));
                item.setChecked(true);
                editor.putString("NewsSrc","engadget");
                Log.d(LOG_TAG," SHARED PREF : " + "engadget");
                break;
            case R.id.item5:
                Utility.setNewsSource(getString(R.string.hackernewsSrc));
                item.setChecked(true);
                editor.putString("NewsSrc","hacker-news");
                Log.d(LOG_TAG," SHARED PREF : " + "hacker-news");
                break;
            default:
                Utility.setNewsSource(getString(R.string.wiredSrc));
                editor.putString("NewsSrc","wired.de");
                Log.d(LOG_TAG," SHARED PREF : " + "wired.de");
                break;

        }

        editor.commit();
        Log.d(LOG_TAG," OPTION SELECTED "+item.toString());

        mViewPager.getAdapter().notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
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

            Log.d(LOG_TAG,"MainActivity Position : "+position);

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

        @Override
        public int getItemPosition(Object object) {

            Log.i(LOG_TAG, "Menu selected : "+object.toString());

            return POSITION_NONE;
        }

    }
}

