package com.example.arslicious;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartActivity extends AppCompatActivity {
    AppCompatButton btnProceedTOCheckout;
    TextView tvCartQuantity, tvCartPrice, tvTotal;
    RecyclerView recyclerView;
    CartMCAdapter cartMCAdapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    List<CartModelClass> cartModelClassList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        btnProceedTOCheckout = findViewById(R.id.btnProceedTOCheckout);
        tvCartQuantity = findViewById(R.id.tvCartQuantity);
        tvCartPrice = findViewById(R.id.tvCartPrice);
        progressBar = findViewById(R.id.progressBar);
        tvTotal = findViewById(R.id.tvTotal);

//       for Status bar colour
        getWindow().setStatusBarColor(ContextCompat.getColor(CartActivity.this, R.color.teal_700));

        recyclerView = findViewById(R.id.rv_Cart);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        cartModelClassList = new ArrayList<>();

        firebaseFirestore.collection("AddToCart").document(firebaseAuth.getCurrentUser().getUid())
                .collection("CurrentUser").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
//                            progressBar.setVisibility(View.VISIBLE);
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                String documentId = documentSnapshot.getId();
                                CartModelClass cartModelClass = new CartModelClass();
                                cartModelClass.setCartImage((String) documentSnapshot.get("ProductImage"));
                                cartModelClass.setCartPrice(Integer.parseInt((String) Objects.requireNonNull(documentSnapshot.get("ProductPrice"))));
                                cartModelClass.setCartQuantity((String) documentSnapshot.get("totalQuantity"));
                                cartModelClass.setCartName((String) documentSnapshot.get("ProductName"));

                                cartModelClass.setDocumentId(documentId);
                                cartModelClassList.add(cartModelClass);
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                            cartMCAdapter = new CartMCAdapter(cartModelClassList);
                            recyclerView.setAdapter(cartMCAdapter);
                        }
                        calculateTotalAmount();
//                        progressBar.setVisibility(View.GONE);
                    }
                });

        btnProceedTOCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, OrderProcedure.class);
                intent.putExtra("ItemList", (Serializable) cartModelClassList);
                intent.putExtra("TotalPrice", tvTotal.getText().toString());
                startActivity(intent);
            }
        });
    }

    //    for totalPrice
    private void calculateTotalAmount() {
        int totalAmount = 0;
        for (CartModelClass cartModelClass : cartModelClassList) {
            totalAmount += cartModelClass.getCartPrice();
        }
        tvTotal.setText("PKR:" + totalAmount);
    }
}