package com.roh44x.medlinker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.roh44x.medlinker.fragment.HomeFragment;
import com.roh44x.medlinker.fragment.ProgressFragment;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public NavigationView navigationView;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        loadFragment(new HomeFragment());

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        Fragment fragment = null;
        switch (menuItem.getItemId()){
            case R.id.explore:
                fragment = new HomeFragment();
                break;

            case R.id.progress:
                fragment = new ProgressFragment();
                break;

            case R.id.add_diagnosis:
                startActivity(new Intent(HomeActivity.this, AddDiagnosis.class));
                break;
//
//            case R.id.settings:
//                fragment = new SettingsFragment();
//                break;
//
//            case R.id.profile:
//                fragment = new ProfileFragment();
//                break;
            case R.id.disconnect:
                disconnectUser();
                break;
            default:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return loadFragment(fragment);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }

        return false;
    }

    private void disconnectUser(){
        mAuth.signOut();
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
    }
}
