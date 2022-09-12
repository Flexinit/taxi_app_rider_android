package com.quawlebs.drupp.view.ui.chat;

class ChatViewState {
    public String message="";
    public String error;
    public boolean progress;
    public boolean sent;

    public ChatViewState(String error, boolean progress) {
        this.error = error;
        this.progress = progress;
    }
}
