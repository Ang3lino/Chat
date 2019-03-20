package com.example.angel.networkingchat.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.Person;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.angel.networkingchat.R;
import com.example.angel.networkingchat.dialog.PrivateMessageDialog;
import com.example.angel.networkingchat.recyclerViewAvailablePeople.PersonAvailableAdapter;
import com.example.angel.networkingchat.recyclerViewAvailablePeople.PersonAvailableItem;
import com.example.angel.networkingchat.utilidades.MutableStore;

import java.util.ArrayList;

public class ActivePeopleFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private PersonAvailableAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private void init(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerViewAvailablePeople);

        // set it if you know recycler view won't change in size, it improves performance in the app
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(view.getContext());
        mAdapter = new PersonAvailableAdapter(MutableStore.getAvailables());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new PersonAvailableAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) { // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                String nick = MutableStore.getAvailables().get(position).getNick(); // get position from ArrayList
                Toast.makeText(getContext(), nick, Toast.LENGTH_LONG).show();
                PrivateMessageDialog dialog = new PrivateMessageDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "Ejemplo");
            }
        });
    }

    public void addActivePerson(PersonAvailableItem person) {
        MutableStore.getAvailables().add(person);
        mAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_active_people, container, false);
        init(v);
        return v;
    }
}
