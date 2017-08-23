package com.y34h1a.rvlazyloading.interfaces;

import com.y34h1a.rvlazyloading.model.User;

import java.util.List;

public interface OnDataLoadListener {
    void onDataLoaded(List<User> users);
}
