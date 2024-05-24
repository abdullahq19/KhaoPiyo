package com.example.arslicious;

import static android.view.View.inflate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ModelClassAdapter extends RecyclerView.Adapter<ModelClassAdapter.productViewHolder> {
    List<ModelClass> modelClassList;
    Context context;

    private static OnCardLongClickListener onCardLongClickListener;

    public void setOnCardLongClickListener(OnCardLongClickListener onCardLongClickListener) {
        ModelClassAdapter.onCardLongClickListener = onCardLongClickListener;
    }

    public ModelClassAdapter(Context context, List<ModelClass> modelClassList) {
        this.modelClassList = modelClassList;
        this.context = context;
    }

    @NonNull
    @Override
    public productViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_layout, parent, false);
        return new productViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull productViewHolder holder, int position) {
        Glide.with(context).load(modelClassList.get(position).getImageUrl()).into(holder.ivDImage);
        holder.tvDName.setText(modelClassList.get(position).getName());
        holder.tvDCurrencyType.setText(modelClassList.get(position).getCurrencyType());
        holder.tvDPrice.setText(Integer.toString(modelClassList.get(position).getPrice()));
//        Picasso.get().load(modelClassList.get(position).getImageUrl()).into(holder.ivDImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetails.class);
                intent.putExtra("details", modelClassList.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });

//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                if (onCardLongClickListener != null) {
//                    onCardLongClickListener.OnCardLongClick(view, position);
//                }
//                return true;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return modelClassList.size();
    }

    public class productViewHolder extends RecyclerView.ViewHolder {
        ImageView ivDImage;
        TextView tvDCurrencyType, tvDName, tvDPrice;

        public productViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDImage = itemView.findViewById(R.id.ivDImage);
            tvDCurrencyType = itemView.findViewById(R.id.tvDCurrencyType);
            tvDName = itemView.findViewById(R.id.tvDName);
            tvDPrice = itemView.findViewById(R.id.tvDPrice);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onCardLongClickListener.onCardLongClick(view, position);
                    }
                    return true;
                }
            });
        }
    }
    interface OnCardLongClickListener{
        void onCardLongClick(View view, int position);
    }
}