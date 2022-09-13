package com.example.arslicious;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    AppCompatButton btnCart;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    FragmentAdapter adapter;
    ImageView ivNaruto;
    TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        DrawerLayout drawerLayout = findViewById(R.id.drawer_Layout);
        NavigationView navigationView = findViewById(R.id.navView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        btnCart = findViewById(R.id.btnCart);

//       for Status bar colour
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.teal_700));

        //for Current user's image
        View view = navigationView.getHeaderView(0);
        ivNaruto = view.findViewById(R.id.ivNaruto);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference();
        ref.child(Objects.requireNonNull(firebaseAuth.getCurrentUser().getEmail())).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(ivNaruto);
            }
        });

//        For Current user's name
//        View view1 = navigationView.getHeaderView(1);
        tvName = view.findViewById(R.id.tvName);
        firebaseFirestore.collection("UserName").document(firebaseAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String Name = value.get("Name").toString();
                tvName.setText(Name);
            }
        });

//        for tab layout
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new FragmentAdapter(fragmentManager, getLifecycle());
        viewPager.setAdapter(adapter);
        tabLayout.addTab(tabLayout.newTab().setText("Pizza"));
        tabLayout.addTab(tabLayout.newTab().setText("Burger"));
        tabLayout.addTab(tabLayout.newTab().setText("Fries"));
        tabLayout.addTab(tabLayout.newTab().setText("Shakes"));
        tabLayout.addTab(tabLayout.newTab().setText("Pasta"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

//       this is for tool bar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

//      this is for  side drawer
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        break;
                    case R.id.cart:
                        startActivity(new Intent(MainActivity.this, CartActivity.class));
                        Toast.makeText(MainActivity.this, " My Cart", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.order:
                        startActivity(new Intent(MainActivity.this, OrderHistory.class));
                        Toast.makeText(MainActivity.this, "My Orders", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.signOut:
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
                        Toast.makeText(MainActivity.this, "SignOut ", Toast.LENGTH_SHORT).show();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });
    }

    //    this is used to close app
    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_Layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finishAffinity();
        }
    }
}