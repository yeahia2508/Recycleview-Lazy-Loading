package com.y34h1a.rvlazyloading.provider;

import com.y34h1a.rvlazyloading.interfaces.OnDataLoadListener;
import com.y34h1a.rvlazyloading.model.User;
import java.util.ArrayList;
import java.util.List;

public class DataProvider {

    private OnDataLoadListener onDataLoadListener;


    public void getData(){
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 39 ; i++){
            User user = new User();
            user.setName("User " + (i+1));
            user.setEmail("yeahia.arif@gmail.com");
            users.add(user);
        }

        onDataLoadListener.onDataLoaded(users);
    }

    public void setOnDataLoadListener(OnDataLoadListener onDataLoadListener) {
        this.onDataLoadListener  = onDataLoadListener;
    }
}
