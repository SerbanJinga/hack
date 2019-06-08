package com.roh44x.medlinker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roh44x.medlinker.components.User;
import com.roh44x.medlinker.fragment.HomeFragment;
import com.roh44x.medlinker.fragment.ProgressFragment;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public NavigationView navigationView;

    private FirebaseAuth mAuth;
    private DatabaseReference diagnosisRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        diagnosisRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

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
                checkDiseaseExist();
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


    private void checkDiseaseExist(){
        diagnosisRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                    if(!user.disease.equals("")){
                        startActivity(new Intent(HomeActivity.this, AddTreatment.class));
                    }else{
                        startActivity(new Intent(HomeActivity.this, AddDiagnosis.class));
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
