package com.example.arslicious;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShakesFragment extends Fragment {
    List<ModelClass> modelClassList;
    ModelClassAdapter modelClassAdapter;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    ProgressBar progressBar;
    FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shakes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rvShakes);
        progressBar = view.findViewById(R.id.progressBar);
        modelClassList = new ArrayList<>(100);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        String uId = auth.getCurrentUser().getUid();
        Log.d("iid", "onViewCreated: " + uId);
        db.collection("Shakes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ModelClass modelClass = document.toObject(ModelClass.class);
                                modelClassList.add(modelClass);
                            }
                            modelClassAdapter = new ModelClassAdapter(getContext(), modelClassList);

                            modelClassAdapter.setOnCardLongClickListener(new ModelClassAdapter.OnCardLongClickListener() {
                                @Override
                                public void onCardLongClick(View view, int position) {
                                    Intent intent = new Intent(getContext(), CalorieDialog.class);

                                    Random random = new Random();
                                    ModelClass model = modelClassList.get(position);

                                    int minCal = 350;
                                    int maxCal = 650;
                                    int minPro = 15;
                                    int maxPro = 45;
                                    int minCarb = 20;
                                    int maxCarb = 60;
                                    int minFat = 15;
                                    int maxFat = 40;

                                    int Calrange = maxCal - minCal;
                                    int Prorange = maxPro - minPro;
                                    int Carbrange = maxCarb - minCarb;
                                    int Fatrange = maxFat - minFat;

                                    String name = model.getName();
                                    String imageUrl = model.getImageUrl();
                                    String calories = String.valueOf(random.nextInt(Calrange + 1) + minCal);
                                    String protein = String.valueOf(random.nextInt(Prorange + 1) + minPro);
                                    String carbs = String.valueOf(random.nextInt(Carbrange + 1) + minCarb);
                                    String fats = String.valueOf(random.nextInt(Fatrange + 1) + minFat);


                                    intent.putExtra("EXTRA_IMAGE", imageUrl);
                                    intent.putExtra("EXTRA_NAME", name);
                                    intent.putExtra("EXTRA_CALORIES", calories);
                                    intent.putExtra("EXTRA_PROTEIN", protein);
                                    intent.putExtra("EXTRA_CARBS", carbs);
                                    intent.putExtra("EXTRA_FATS", fats);

                                    startActivity(intent);

                                }
                            });

                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                            recyclerView.setAdapter(modelClassAdapter);
                            recyclerView.setHasFixedSize(true);
                        }
                    }
                });
    }
}