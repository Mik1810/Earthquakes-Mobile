package com.example.earthquakemobile.service;

import android.content.Context;

import com.example.earthquakemobile.service.Request;

public class Repository {
    public void downloadData(Context context, Request.RequestCallback callback){
        Request.getInstance(context).requestDownload(callback);
    }
}
