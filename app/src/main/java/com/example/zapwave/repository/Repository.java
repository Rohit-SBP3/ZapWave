package com.example.zapwave.repository;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.zapwave.model.ChatGroup;
import com.example.zapwave.model.ChatMessage;
import com.example.zapwave.views.GroupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


// It act as a bridge between the ViewModel and the data source.
public class Repository {

    MutableLiveData<List<ChatGroup>> chatGroupMutableLiveData;
    MutableLiveData<List<ChatMessage>> chatMessagesMutableLiveData;

    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference groupReference;
    ChatGroup chatGroup;

    public Repository(){
        this.chatGroupMutableLiveData = new MutableLiveData<>();
        this.chatMessagesMutableLiveData = new MutableLiveData<>();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    // Anonymous Authentication with a callback for success and failure handling
    public void firebaseAnonymousAuth(Context context, AuthCallback callback) {
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Success callback
                    callback.onSuccess();
                    // Navigate to GroupActivity after successful sign-in
                    Intent i = new Intent(context, GroupActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(i);
                } else {
                    // Failure callback
                    callback.onFailure(task.getException());

                    // Display a toast with an error message
                    Toast.makeText(context, "Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Getting Current User ID
    public String getCurrentUserID(){
        return FirebaseAuth.getInstance().getUid();
    }

    // Sign Out Functionality
    public void signOut(){
        FirebaseAuth.getInstance().signOut();
    }

    // Getting Chat Groups available from FireStore Realtime DB.

    public MutableLiveData<List<ChatGroup>> getChatGroupMutableLiveData() {
        List<ChatGroup> chatGroupList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatGroupList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ChatGroup group = new ChatGroup(dataSnapshot.getKey());
                    chatGroupList.add(group);
                }
                chatGroupMutableLiveData.postValue(chatGroupList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return chatGroupMutableLiveData;
    }


    // Getting Chat Messages Live Data
    public MutableLiveData<List<ChatMessage>> getChatMessagesMutableLiveData(String groupName) {
        groupReference = database.getReference().child(groupName);
        List<ChatMessage> messageList = new ArrayList<>();
        groupReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
                    messageList.add(message);
                }
                chatMessagesMutableLiveData.postValue(messageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return chatMessagesMutableLiveData;
    }

    // Creating a new group
    public void createNewChatGroup(String groupName){
        reference.child(groupName).setValue(groupName);
    }

    public void sendMessages(String messsage, String groupName){
        DatabaseReference ref = database.getReference(groupName);
        if(!messsage.trim().equals("")){
            ChatMessage msg = new ChatMessage(
                    FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    messsage,
                    System.currentTimeMillis()
            );
            String randomKey = ref.push().getKey();
            ref.child(randomKey).setValue(msg);
        }
    }


    // Callback Interface for authentication results
    public interface AuthCallback {
        void onSuccess();
        void onFailure(Exception e);
    }

}
