package com.example.buddy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private List<Friend> friendList;
    private Context context;

    public FriendAdapter(Context context, List<Friend> friendList) {
        this.context = context;
        this.friendList = friendList;
    }


    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        Friend friend = friendList.get(position);
        holder.tvFriendName.setText(friend.getName());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FriendDetails.class);
            intent.putExtra("name", friend.getName());
            intent.putExtra("dob", friend.getDob());
            intent.putExtra("phone", friend.getPhone());
            intent.putExtra("gender", friend.getGender());
            intent.putExtra("email", friend.getEmail());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    static class FriendViewHolder extends RecyclerView.ViewHolder {
        TextView tvFriendName;

        FriendViewHolder(View itemView) {
            super(itemView);
            tvFriendName = itemView.findViewById(R.id.tvFriendName);
        }
    }

    public void updateList(ArrayList<Friend> newList) {
        this.friendList = newList;
        notifyDataSetChanged();
    }

}
