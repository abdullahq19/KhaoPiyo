package com.example.arslicious;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartMCAdapter extends RecyclerView.Adapter<CartMCAdapter.viewHolder> {
    List<CartModelClass> cartMCList;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    Context context;

    public CartMCAdapter(List<CartModelClass> cartMCList) {
        this.cartMCList = cartMCList;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_cart_layout, null);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {
        Picasso.get().load(cartMCList.get(position).getCartImage()).into(holder.ivCartImage);
        holder.tvCartName.setText(cartMCList.get(position).getCartName());
        holder.tvCartQuantity.setText(cartMCList.get(position).getCartQuantity());
        holder.tvCartPrice.setText(String.valueOf(cartMCList.get(position).getCartPrice()));

        holder.btnCartDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("AddToCart").document(firebaseAuth.getCurrentUser().getUid())
                        .collection("CurrentUser")
                        .document(cartMCList.get(position).documentId)
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    cartMCList.remove(cartMCList.get(position));
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(context, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartMCList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        ImageView ivCartImage;
        TextView tvCartName, tvCartQuantity, tvCartPrice;
        AppCompatButton btnCartDelete;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ivCartImage = itemView.findViewById(R.id.ivCartImage);
            tvCartName = itemView.findViewById(R.id.tvCartName);
            tvCartQuantity = itemView.findViewById(R.id.tvCartQuantity);
            tvCartPrice = itemView.findViewById(R.id.tvCartPrice);
            btnCartDelete = itemView.findViewById(R.id.btnCartDelete);
        }
    }
}
