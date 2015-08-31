package eu.gyurasz.mariaradio;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import eu.gyurasz.mariaradio.mountpoint.MountPoint;
import eu.gyurasz.mariaradio.mountpoint.MountPointLoader;
import eu.gyurasz.mariaradio.program.Program;
import eu.gyurasz.mariaradio.program.ProgramFragment;
import eu.gyurasz.mariaradio.program.ProgramLoader;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener, ILoaded, DialogInterface.OnCancelListener {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ProgramFragment mProgramFragment;
    private MainFragment mMainFragment;
    private List<MountPoint> mMountPoints;
    private List<Program> mPrograms;
    private ProgressDialog mProgressDialog;
    private MountPointLoader mpl;
    private ProgramLoader pl;

    private static MainActivity inst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inst = this;

        mMountPoints = new ArrayList<MountPoint>();
        mPrograms = new ArrayList<Program>();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.app_name));
        mProgressDialog.setMessage(getString(R.string.loading_datas));
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnCancelListener(this);
        mProgressDialog.setProgressStyle(mProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setProgress(0);
        mProgressDialog.setMax(2);
        mProgressDialog.show();

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    public static MainActivity instance(){
        return inst;
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

    public MainFragment getMainFragment() {
        return mMainFragment;
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

    @Override
    public void onCancel(DialogInterface dialog) {
        pl.cancel(false);
        mpl.cancel(false);
    }

    @Override
    public void OnException(Exception e) {
        Toast.makeText(this, e.getClass().getName() + "\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
        System.out.println("Exception: ");
        e.printStackTrace();

        if(mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
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
                    mpl = new MountPointLoader(mMountPoints);
                    mpl.execute(MainActivity.this);
                    return mMainFragment;
                case 1:
                    mProgramFragment = new ProgramFragment();
                    pl = new ProgramLoader(mPrograms);
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
