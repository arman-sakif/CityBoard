package com.example.cityboard.Model;

import java.util.ArrayList;

public class ItemGroup {
    private String headerTitle;
    private ArrayList<ItemData> listItem;

    public ItemGroup(){

    }

    public ItemGroup(String headerTitle, ArrayList<ItemData> listItem) {
        this.headerTitle = headerTitle;
        this.listItem = listItem;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<ItemData> getListItem() {
        return listItem;
    }

    public void setListItem(ArrayList<ItemData> listItem) {
        this.listItem = listItem;
    }
}
