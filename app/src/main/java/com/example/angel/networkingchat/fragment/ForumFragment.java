package com.example.angel.networkingchat.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.bundled.BundledEmojiCompatConfig;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.angel.networkingchat.R;

public class ForumFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // we need to init EmojiText otherwise the app chashes
        EmojiCompat.Config config = new BundledEmojiCompatConfig(getActivity().getApplicationContext());
        EmojiCompat.init(config);

        return inflater.inflate(R.layout.fragment_forum, container, false);
    }
}
