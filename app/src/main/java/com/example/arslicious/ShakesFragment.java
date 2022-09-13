package com.example.arslicious;

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
                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                            recyclerView.setAdapter(modelClassAdapter);
                            recyclerView.setHasFixedSize(true);
                        }
                    }
                });
    }
}