package com.example.arslicious;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderHistory extends AppCompatActivity {
    List<OrderModelClass> orderModelClassList;
    OrderMCAdapter orderMCAdapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        recyclerView = findViewById(R.id.rvOrder);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        orderModelClassList = new ArrayList<>();

//       for Status bar colour
        getWindow().setStatusBarColor(ContextCompat.getColor(OrderHistory.this, R.color.teal_700));

        firebaseFirestore.collection("AddToOrder").document(firebaseAuth.getCurrentUser().getUid())
                .collection("UserCurrent").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
//                            progressBar.setVisibility(View.VISIBLE);
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                OrderModelClass orderModelClass = new OrderModelClass();
                                orderModelClass.setOrderImage((String) documentSnapshot.get("Image"));
                                orderModelClass.setOrderPrice(Math.toIntExact((Long) documentSnapshot.get("Price")));
                                orderModelClass.setOrderQuantity((String) documentSnapshot.get("Quantity"));
                                orderModelClass.setOrderName((String) documentSnapshot.get("Name"));
                                orderModelClass.setOrderAddress((String) documentSnapshot.get("Address"));
                                orderModelClass.setOrderAddons((String) documentSnapshot.get("Addons"));
                                orderModelClass.setOrderPhoneNo((String) documentSnapshot.get("PhoneNo"));
                                orderModelClassList.add(orderModelClass);
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(OrderHistory.this));
                            orderMCAdapter = new OrderMCAdapter(orderModelClassList);
                            recyclerView.setAdapter(orderMCAdapter);
//                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}