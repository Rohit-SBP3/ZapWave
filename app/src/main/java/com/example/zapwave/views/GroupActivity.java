package com.example.zapwave.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.zapwave.R;
import com.example.zapwave.databinding.ActivityGroupBinding;
import com.example.zapwave.model.ChatGroup;
import com.example.zapwave.viewmodel.MyViewModel;
import com.example.zapwave.views.adapter.GroupAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {

    private ArrayList<ChatGroup> chatGroupArrayList;
    private Dialog dialog;
    private FloatingActionButton fabGroup;
    private RecyclerView recyclerView;
    private GroupAdapter groupAdapter;
    private ActivityGroupBinding binding;
    private MyViewModel myViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_group);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        // RecyclerView with Data Binding
        recyclerView = binding.rvGroup;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.pbGroup.setVisibility(View.VISIBLE);

        // Set an observer to listen for changes in a "Live Data" object
        myViewModel.getGroupList().observe(this, new Observer<List<ChatGroup>>() {
            @Override
            public void onChanged(List<ChatGroup> chatGroups) {
                // The updated data is received as "ChatGroups" parameter in onChanged()
                chatGroupArrayList = new ArrayList<>();
                chatGroupArrayList.addAll(chatGroups);

                groupAdapter = new GroupAdapter(chatGroupArrayList);

                recyclerView.setAdapter(groupAdapter);
                groupAdapter.notifyDataSetChanged();
                binding.pbGroup.setVisibility(View.GONE);
            }
        });

        binding.fabGroup.setOnClickListener(v -> {
            showCreateGroupDialog();
        });

    }

    public void showCreateGroupDialog(){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);
        dialog.setContentView(view);
        dialog.show();

        Button createGroup = view.findViewById(R.id.btnCreateGroup);
        TextInputEditText edtName = view.findViewById(R.id.add_group_name_dialog_edt);

        createGroup.setOnClickListener(v -> {
            String groupName = edtName.getText().toString().trim();

            myViewModel.createNewGroup(groupName);

            dialog.dismiss();
        });

    }
}