package com.jograt.atenatics.wordplay_offlinedictionary;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    boolean doubleBackToExitPressedOnce = false;
    Bundle bundle;
    LoginManager manager;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-7442329123714606~1329017174");

        //facebook integration
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        manager = LoginManager.getInstance();
        shareDialog = new ShareDialog(this);

        //Notification
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 13);
            calendar.set(Calendar.MINUTE, 16);
            calendar.set(Calendar.SECOND, 30);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);
        } else{

        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //get word of the day
        setTitle("");
        WordOfTheDayFragment wordOfTheDayFragmentFragment = new WordOfTheDayFragment();
        wordOfTheDayFragmentFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment, wordOfTheDayFragmentFragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav_search, menu);
        MenuItem item = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                setTitle("Search Results");
                Bundle bundle = new Bundle();
                bundle.putString("query", query);
                ResultFragment resultFragment = new ResultFragment();
                resultFragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment, resultFragment).commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_games) {
            setTitle("");
            GameListFragment gameListFragment = new GameListFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction  = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.fadein, R.anim.nolaman);
            transaction.replace(R.id.fragment, gameListFragment).commit();
        }
        else if (id == R.id.nav_random) {
            setTitle("");
            RandomWordFragment randomFragment = new RandomWordFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction  = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.fadein, R.anim.nolaman);
            transaction.replace(R.id.fragment, randomFragment).commit();
        } else if(id == R.id.nav_dayword){
            setTitle("");
            WordOfTheDayFragment wordOfTheDayFragmentFragment = new WordOfTheDayFragment();
            wordOfTheDayFragmentFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction  = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.fadein, R.anim.nolaman);
            transaction.replace(R.id.fragment, wordOfTheDayFragmentFragment).commit();
        } else if(id == R.id.nav_trivia){
            setTitle("");
            NumberTriviaFragment numberTriviaFragment = new NumberTriviaFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction  = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.fadein, R.anim.nolaman);
            transaction.replace(R.id.fragment, numberTriviaFragment).commit();
        }else if(id == R.id.nav_filter){
            setTitle("");
            SynonymsFragment synonymsFragment = new SynonymsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction  = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.fadein, R.anim.nolaman);
            transaction.replace(R.id.fragment, synonymsFragment).commit();
        }else if(id == R.id.nav_share){
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.jograt.atenatics.wordplay_offlinedictionary"))
                        .build();
                shareDialog.show(linkContent);
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
