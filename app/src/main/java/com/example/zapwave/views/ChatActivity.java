package com.example.zapwave.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.zapwave.R;
import com.example.zapwave.databinding.ActivityChatBinding;
import com.example.zapwave.model.ChatMessage;
import com.example.zapwave.viewmodel.MyViewModel;
import com.example.zapwave.views.adapter.ChatAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private MyViewModel myViewModel;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_chat);

        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        recyclerView = binding.rvChat;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Getting group name from the clicked item in the groups activity
        String groupName = getIntent().getStringExtra("GROUP_NAME");

        myViewModel.getGroupMessages(groupName).observe(this, new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(List<ChatMessage> chatMessages) {
                chatMessageList = new ArrayList<>();
                chatMessageList.addAll(chatMessages);

                chatAdapter = new ChatAdapter(chatMessageList,getApplicationContext());
                recyclerView.setAdapter(chatAdapter);
                chatAdapter.notifyDataSetChanged();

                // Scroll automatically to the latest message added:-
                int latestPosition = chatAdapter.getItemCount()-1;
                if(latestPosition > 0){
                    recyclerView.smoothScrollToPosition(latestPosition);
                }

            }
        });

        binding.setVModel(myViewModel);

        binding.btnSend.setOnClickListener(v -> {
            String msg = binding.edtChatMessage.getText().toString();
            myViewModel.sendMessage(msg, groupName);
            binding.edtChatMessage.getText().clear();
        });


    }
}