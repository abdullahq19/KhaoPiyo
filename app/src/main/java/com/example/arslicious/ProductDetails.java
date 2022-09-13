package com.example.arslicious;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ProductDetails extends AppCompatActivity {
    AppCompatButton btnCart, btnAddToCart, btnDetailRemove, btnDetailAdd;
    TextView tvNameTag, tvPriceTag, tvCurrencyTypeTag, tvDetailQuantity;
    ImageView ivImage;
    ModelClass modelClass = null;
    int totalQuantity = 1;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        btnCart = findViewById(R.id.btnCart);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        ivImage = findViewById(R.id.ivImage);
        tvNameTag = findViewById(R.id.tvNameTag);
        tvPriceTag = findViewById(R.id.tvPriceTag);
        tvCurrencyTypeTag = findViewById(R.id.tvCurrencyTypeTag);
        btnDetailRemove = findViewById(R.id.btnDetailRemove);
        btnDetailAdd = findViewById(R.id.btnDetailAdd);
        tvDetailQuantity = findViewById(R.id.tvDetailQuantity);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

//       for Status bar colour
        getWindow().setStatusBarColor(ContextCompat.getColor(ProductDetails.this, R.color.teal_700));

        final Object object = getIntent().getSerializableExtra("details");
        if (object instanceof ModelClass) {
            modelClass = (ModelClass) object;
        }

        if (modelClass != null) {
            Glide.with(getApplicationContext()).load(modelClass.getImageUrl()).into(ivImage);
            tvNameTag.setText(modelClass.getName());
            tvCurrencyTypeTag.setText(modelClass.getCurrencyType());
            tvPriceTag.setText(Integer.toString(modelClass.getPrice()));
        }

        btnDetailRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalQuantity > 1) {
                    totalQuantity--;
                    tvDetailQuantity.setText(String.valueOf(totalQuantity));
                    int totalAmount = modelClass.getPrice() * totalQuantity;
                    tvPriceTag.setText(String.valueOf(totalAmount));
                }
            }
        });

        btnDetailAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalQuantity < 100) {
                    totalQuantity++;
                    tvDetailQuantity.setText(String.valueOf(totalQuantity));
                    int totalAmount = modelClass.getPrice() * totalQuantity;
                    tvPriceTag.setText(String.valueOf(totalAmount));
                }
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductDetails.this, CartActivity.class));
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addedToCart();
            }
        });
    }

    private void addedToCart() {
        final HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ProductName", modelClass.getName());
        hashMap.put("ProductPrice", tvPriceTag.getText().toString());
        hashMap.put("totalQuantity", tvDetailQuantity.getText().toString());
        hashMap.put("ProductImage", modelClass.getImageUrl());
        firebaseFirestore.collection("AddToCart").document(firebaseAuth.getCurrentUser().getUid())
                .collection("CurrentUser").add(hashMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(ProductDetails.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}