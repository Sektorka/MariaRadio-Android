package eu.gyurasz.mariaradio;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import eu.gyurasz.mariaradio.mountpoint.MountPoint;
import eu.gyurasz.mariaradio.mountpoint.MountPointLoader;
import eu.gyurasz.mariaradio.program.Program;
import eu.gyurasz.mariaradio.program.ProgramFragment;
import eu.gyurasz.mariaradio.program.ProgramLoader;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener, ILoaded {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ProgramFragment mProgramFragment;
    private MainFragment mMainFragment;
    private List<MountPoint> mMountPoints;
    private List<Program> mPrograms;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMountPoints = new ArrayList<MountPoint>();
        mPrograms = new ArrayList<Program>();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.app_name));
        mProgressDialog.setMessage(getString(R.string.loading_datas));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(mProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setProgress(0);
        mProgressDialog.setMax(2);
        mProgressDialog.show();

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });




        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    public List<MountPoint> getMountPoints() {
        return mMountPoints;
    }

    public List<Program> getPrograms() {
        return mPrograms;
    }

    public ProgramFragment getProgramFragment() {
        return mProgramFragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void Loaded(List dataList) {
        if(dataList.size() > 0){
            Object firstItem = dataList.get(0);

            if (firstItem instanceof Program) {
                mProgramFragment.programsUpdated();
                incrementProgress();
            }
            else if(firstItem instanceof MountPoint){
                mMainFragment.mountPointsUpdated();
                incrementProgress();
            }
        }
    }

    private void incrementProgress(){
        mProgressDialog.setProgress(mProgressDialog.getProgress() + 1);

        if(mProgressDialog.getProgress() >= mProgressDialog.getMax()){
            mProgressDialog.dismiss();
            mMainFragment.updateStatus();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    mMainFragment = new MainFragment();
                    MountPointLoader mpl = new MountPointLoader(mMountPoints);
                    mpl.execute(MainActivity.this);
                    return mMainFragment;
                case 1:
                    mProgramFragment = new ProgramFragment();
                    ProgramLoader pl = new ProgramLoader(mPrograms);
                    pl.execute(MainActivity.this);
                    return mProgramFragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_radio).toUpperCase(l);
                case 1:
                    return getString(R.string.title_programs).toUpperCase(l);
            }
            return null;
        }
    }
}
