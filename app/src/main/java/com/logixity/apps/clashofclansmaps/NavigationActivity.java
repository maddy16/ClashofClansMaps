package com.logixity.apps.clashofclansmaps;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    static int townHallNum;
    static HashMap<String,Object[]> listsMap;

    TabLayout tbl_pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        townHallNum = 11;
        prepareData();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tbl_pages= (TabLayout) findViewById(R.id.tbl_pages);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        setTabs();
    }
    void prepareData(){
        listsMap = new HashMap<>();
        Field[] fields = R.drawable.class.getFields();
        int [][] indexers = new int[11][4];
        for(int i =0;i<indexers.length;i++){
            for(int j=0;j<indexers[i].length;j++){
                indexers[i][j]=0;
            }
        }
        Object [][] mainArray = new Object[11][4];
        for(int i =0;i<mainArray.length;i++){
            for(int j=0;j<mainArray[i].length;j++){
                ArrayList<MapModel> list = new ArrayList<>();
                mainArray[i][j] = list;
            }
        }
        for(Field field : fields){
            String name = field.getName();
            if(name.startsWith("coc")){
                String [] cats = name.split("_");
                String townHall = cats[1].substring(2);
                int tIndex = Integer.parseInt(townHall)-1;
                int catIndex = 0;
                String category = cats[2];
                MapModel model = new MapModel();
                model.setMapImage(getResources().getIdentifier(field.getName(), "drawable", getPackageName()));
                String mapName = "";
                if(category.equalsIgnoreCase("w")){
                    mapName +="War_";
                    catIndex=0;
                } else if(category.equalsIgnoreCase("t")){
                    mapName +="Trophy_";
                    catIndex=1;
                } else if(category.equalsIgnoreCase("f")){
                    mapName +="Farming_";
                    catIndex=2;
                } else if(category.equalsIgnoreCase("h")){
                    mapName +="Hybrid_";
                    catIndex=3;
                }
                model.setMapName(mapName+townHall+0+cats[3]);
                ArrayList<MapModel> list  = (ArrayList<MapModel>) mainArray[tIndex][catIndex];
                list.add(model);
            }
        }
        for(int i = 0;i<mainArray.length;i++){
            listsMap.put((i+1)+"",mainArray[i]);
        }
    }
    void setTabs(){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.

        ViewPager vp_pages= (ViewPager) findViewById(R.id.vp_pages);
        vp_pages.setAdapter(mSectionsPagerAdapter);



        tbl_pages.setupWithViewPager(vp_pages);
        vp_pages.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    void rateApp(){
        Uri uri = Uri.parse("market://details?id="+getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }


    }

    /**
     * A placeholder fragment containing a simple view.
     */

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a TabFragment (defined as a static inner class below).
//            if(position==0){
                return TabFragment.newInstance(position + 1);
//            } else {
//                return NotificationsFragment.newInstance(position + 1);
//            }

        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "War";
                case 1:
                    return "Trophy";
                case 2:
                    return "Farming";
                case 3:
                    return "Hybrid";
            }
            return null;
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String title = item.getTitle()+"";
        if(title.contains("Town")){
            String [] wordsArr =title.split(" ");
            int townId = Integer.parseInt(wordsArr[wordsArr.length-1]);
            townHallNum = townId;
            setTabs();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
