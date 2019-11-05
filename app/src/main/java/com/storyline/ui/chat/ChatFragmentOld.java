//package com.storyline.ui.chat;
//
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.content.ContextCompat;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.storyline.R;
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class ChatFragment extends Fragment {
//
//    private static final String FIRST_LINE_BUNDLE = "FIRST_LINE_BUNDLE";
//    private static final String LAST_WORD_BUNDLE = "LAST_WORD_BUNDLE";
//
//    private ProgressBar progressBar;
//    private EditText chatEditText;
//    private TextView firstLineTextView;
//
//    private String firstLine;
//    private String lastWord;
//
//    public static ChatFragment newInstance(String firstLine, String lastWord) {
//
//        Bundle args = new Bundle();
//        args.putString(FIRST_LINE_BUNDLE, firstLine);
//        args.putString(LAST_WORD_BUNDLE, lastWord);
//        ChatFragment fragment = new ChatFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_chat, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        progressBar = view.findViewById(R.id.progress_bar);
//        chatEditText = view.findViewById(R.id.chat_edit_text);
//        firstLineTextView = view.findViewById(R.id.first_line_text_view);
//
//        firstLine = getFromBunndle(FIRST_LINE_BUNDLE);
//        lastWord = getFromBunndle(LAST_WORD_BUNDLE);
//
//        firstLineTextView.setText(firstLine);
//        chatEditText.setHint(lastWord);
//    }
//
//    @Nullable
//    private String getFromBunndle(String key) {
//        Bundle bundle = getArguments();
//        if (bundle == null) return null;
//
//        return bundle.getString(key);
//    }
//}
