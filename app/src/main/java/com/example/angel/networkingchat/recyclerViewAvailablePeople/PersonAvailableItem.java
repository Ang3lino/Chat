package com.example.angel.networkingchat.recyclerViewAvailablePeople;

public class PersonAvailableItem {
    private int mImg;
    private String mNick;
    private String mAddr;


    public PersonAvailableItem(int img, String nick, String addr) {
        mImg = img;
        mNick = nick;
        mAddr = addr;
    }

    public int getImg() {
        return mImg;
    }

    public String getAddr() {
        return mAddr;
    }

    public String getNick() {
        return mNick;
    }

}
