package com.example.cityboard.Interface;

import com.example.cityboard.Model.ItemGroup;

import java.util.List;

public interface IFirebaseLoadListener {

    void onFirebaseLoadSuccess(List<ItemGroup> itemGroupList);
    void onFirebaseLoadFailed(String message);
}
