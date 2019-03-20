package com.example.angel.networkingchat.recyclerViewAvailablePeople;

import android.app.Person;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.angel.networkingchat.R;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PersonAvailableAdapter
        extends RecyclerView.Adapter<PersonAvailableAdapter.PersonAvailableViewHolder> {

    private ArrayList<PersonAvailableItem> mAvailableList;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // In this class will store the reference for the values for every item created
    public static class PersonAvailableViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView nick;
        public TextView addr;

        // Here we get the reference for the components given for the item
        public PersonAvailableViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            img = itemView.findViewById(R.id.img_profile);
            nick = itemView.findViewById(R.id.txt_usr_nick);
            addr = itemView.findViewById(R.id.txt_addr_usr);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public PersonAvailableAdapter(ArrayList<PersonAvailableItem> availableList) {
        mAvailableList = availableList;
    }

    @NonNull
    @Override
    public PersonAvailableViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_connected_person, viewGroup, false);
        PersonAvailableViewHolder pavh = new PersonAvailableViewHolder(v, mListener);
        return pavh;
    }

    @Override
    public void onBindViewHolder(@NonNull PersonAvailableViewHolder holder, int i) {
        PersonAvailableItem currentItem = mAvailableList.get(i);

        holder.img.setImageResource(currentItem.getImg());
        holder.nick.setText(currentItem.getNick());
        holder.addr.setText(currentItem.getAddr());
    }

    @Override
    public int getItemCount() {
        return mAvailableList.size();
    }

}
