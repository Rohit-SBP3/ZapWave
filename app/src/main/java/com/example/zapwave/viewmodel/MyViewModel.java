package com.example.zapwave.viewmodel;

import static com.example.zapwave.BR.chatGroup;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.zapwave.model.ChatGroup;
import com.example.zapwave.model.ChatMessage;
import com.example.zapwave.repository.Repository;

import java.util.List;

public class MyViewModel extends AndroidViewModel {

    Repository repository;

    public MyViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    // Anonymous Authentication
    public void loginAnonymousUser(){
        Context context = this.getApplication();
        repository.firebaseAnonymousAuth(context, new Repository.AuthCallback() {
            @Override
            public void onSuccess() {
                // Display a toast  message
                Toast.makeText(context, "Authentication Successful", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Exception e) {
                // Display a toast with an error message
                Toast.makeText(context, "Authentication Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Get Current User ID
    public String getCurrentUserId(){
        return repository.getCurrentUserID();
    }

    // SignOut User
    public void signOut() {
        repository.signOut();
    }

    public MutableLiveData<List<ChatGroup>> getGroupList(){
        return repository.getChatGroupMutableLiveData();
    }

    public MutableLiveData<List<ChatMessage>> getGroupMessages(String groupName){
        return repository.getChatMessagesMutableLiveData(groupName);
    }

    public void createNewGroup(String groupName){
        repository.createNewChatGroup(groupName);
    }

    public void sendMessage(String message, String groupName){
        repository.sendMessages(message,groupName);
    }

}
