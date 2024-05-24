package com.example.arslicious;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class OrderProcedure extends AppCompatActivity {
    AppCompatButton btnOrderNow, btnAddress;
    TextView tvProcedureTotalPrice;
    EditText etPhoneNo, etAddress, etDetails;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    //    Location
    FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_procedure);
        btnOrderNow = findViewById(R.id.btnOrderNow);
        tvProcedureTotalPrice = findViewById(R.id.tvProcedureTotalPrice);
        etPhoneNo = findViewById(R.id.etPhoneNo);
        etAddress = findViewById(R.id.etAddress);
        etDetails = findViewById(R.id.etDetails);
        btnAddress = findViewById(R.id.btnAddress);


//       for Status bar colour
        getWindow().setStatusBarColor(ContextCompat.getColor(OrderProcedure.this, R.color.teal_700));


        //        For Total Amount
        Intent name = getIntent();
        String totalPrice = name.getStringExtra("TotalPrice");
        tvProcedureTotalPrice.setText((totalPrice));

        //        Location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });

        btnOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderNow();
                finish();
                startActivity(new Intent(OrderProcedure.this, OrderPlacedScreen.class));
            }
        });
    }

    private void orderNow() {
        List<CartModelClass> cartModelClassList = (ArrayList<CartModelClass>) getIntent().getSerializableExtra("ItemList");
        if (cartModelClassList != null && cartModelClassList.size() > 0) {
            for (CartModelClass cartModelClass : cartModelClassList) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("Name", cartModelClass.getCartName());
                hashMap.put("Price", cartModelClass.getCartPrice());
                hashMap.put("Quantity", cartModelClass.getCartQuantity());
                hashMap.put("Image", cartModelClass.getCartImage());
                hashMap.put("TPrice", getIntent().getStringExtra("TotalPrice"));
                hashMap.put("PhoneNo", etPhoneNo.getText().toString());
                hashMap.put("Address", etAddress.getText().toString());
                hashMap.put("Addons", etDetails.getText().toString());
                firebaseFirestore.collection("AddToOrder").document(firebaseAuth.getCurrentUser().getUid())
                        .collection("UserCurrent").add(hashMap);
            }
            Toast.makeText(OrderProcedure.this, "Your order has been placed", Toast.LENGTH_SHORT).show();
        }
    }

    //  for Location
    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(OrderProcedure.this, Locale.getDefault());
                                List<Address> address = null;
                                try {
                                    address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    etAddress.setText(address.get(0).getAddressLine(0));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

        } else {
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(OrderProcedure.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Required Permission", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}