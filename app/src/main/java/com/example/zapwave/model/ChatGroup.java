package com.example.zapwave.model;

public class ChatGroup {

    private String groupName;
    private boolean isLoading;

    public ChatGroup(){
    }

    public boolean getIsLoading() {
        return false;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public ChatGroup(String groupName){
        this.groupName = groupName;
    }

    public String getGroupName(){
        return groupName;
    }

    public void setGroupName(String groupName){
        this.groupName = groupName;
    }
}
