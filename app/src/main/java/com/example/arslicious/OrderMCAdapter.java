package com.example.arslicious;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderMCAdapter extends RecyclerView.Adapter<OrderMCAdapter.OrderViewHolder> {
    List<OrderModelClass> orderModelClasses;

    public OrderMCAdapter(List<OrderModelClass> orderModelClasses) {
        this.orderModelClasses = orderModelClasses;
    }


    @Override
    public OrderViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_order_layout, null);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Picasso.get().load(orderModelClasses.get(position).getOrderImage()).into(holder.ivOrderImage);
        holder.tvOrderName.setText(orderModelClasses.get(position).getOrderName());
        holder.tvOrderQuantity.setText(orderModelClasses.get(position).getOrderQuantity());
        holder.tvOrderPrice.setText(String.valueOf(orderModelClasses.get(position).getOrderPrice()));
        holder.tvOrderAddons.setText(orderModelClasses.get(position).getOrderAddons());
        holder.tvOrderPhoneNo.setText(orderModelClasses.get(position).getOrderPhoneNo());
        holder.tvOrderAddress.setText(orderModelClasses.get(position).getOrderAddress());
    }

    @Override
    public int getItemCount() {
        return orderModelClasses.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderName, tvOrderPrice, tvOrderQuantity, tvOrderAddons, tvOrderPhoneNo, tvOrderAddress;
        ImageView ivOrderImage;

        public OrderViewHolder(View itemView) {
            super(itemView);
            tvOrderName = itemView.findViewById(R.id.tvOrderName);
            tvOrderPrice = itemView.findViewById(R.id.tvOrderPrice);
            tvOrderQuantity = itemView.findViewById(R.id.tvOrderQuantity);
            tvOrderAddons = itemView.findViewById(R.id.tvOrderAddons);
            tvOrderPhoneNo = itemView.findViewById(R.id.tvOrderPhoneNo);
            tvOrderAddress = itemView.findViewById(R.id.tvOrderAddress);
            ivOrderImage = itemView.findViewById(R.id.ivOrderImage);

        }
    }
}