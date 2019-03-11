package com.example.angel.networkingchat.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.angel.networkingchat.R;
import com.example.angel.networkingchat.recyclerViewAvailablePeople.PersonAvailableAdapter;
import com.example.angel.networkingchat.recyclerViewAvailablePeople.PersonAvailableItem;

import java.util.ArrayList;

public class ActivePeopleFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private void init(View view) {
        ArrayList<PersonAvailableItem> availables = new ArrayList<>();
        for (int i = 0; i < 20; ++i) availables.add(
                new PersonAvailableItem(
                        R.drawable.ic_tag_faces_black_24dp, "Yo " + i, "Addr " + i));

        mRecyclerView = view.findViewById(R.id.recyclerViewAvailablePeople);

        // set it if you know recycler view won't change in size, it improves performance in the app
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(view.getContext());
        mAdapter = new PersonAvailableAdapter(availables);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_active_people, container, false);
        init(v);
        return v;
    }
}
