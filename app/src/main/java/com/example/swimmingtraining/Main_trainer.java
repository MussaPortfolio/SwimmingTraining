package com.example.swimmingtraining;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Main_trainer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final FirebaseListeners listeners = new FirebaseListeners();

    //Объявление элементов GUI
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference user = database.getReference("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_trainer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //инициализвация navigationview
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //чтение с бд
        DatabaseReference uid = user.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        listeners.add(uid, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
                View header = nv.getHeaderView(0);
                TextView nname = (TextView)header.findViewById(R.id.name1);
                String valuename = dataSnapshot.child("name").getValue(String.class);
                nname.setText(valuename);
                TextView nemail = (TextView)header.findViewById(R.id.email1);
                String valueemail = dataSnapshot.child("email").getValue(String.class);
                nemail.setText(valueemail);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failed to read value.", error.toException());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_trainer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sign_out) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Main_trainer.this, Login.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //описание действий по нажатию на кнопки меню
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        int id = item.getItemId();

        if (id == R.id.nav_message) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    new FragmentMessage()).commit();
            navigationView.setCheckedItem(R.id.nav_message);

        } else if (id == R.id.nav_list) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    new FragmentListTrainerAboutSportsman()).commit();
            navigationView.setCheckedItem(R.id.nav_list);

        } else if (id == R.id.nav_zapros) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    new FragmentZapros()).commit();
            navigationView.setCheckedItem(R.id.nav_zapros);

        } else if (id == R.id.video_sportsman) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    new FragmentVideoSportsman()).commit();
            navigationView.setCheckedItem(R.id.nav_zapros);

        } else if (id == R.id.nav_rairing) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    new FragmentRating()).commit();
            navigationView.setCheckedItem(R.id.nav_rairing);

        } else if (id == R.id.nav_about) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    new FragmentAbout()).commit();
            navigationView.setCheckedItem(R.id.nav_about);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        listeners.removeAll();
        super.onDestroy();
    }
}
