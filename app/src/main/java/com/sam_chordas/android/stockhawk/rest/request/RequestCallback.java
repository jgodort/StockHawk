package com.sam_chordas.android.stockhawk.rest.request;

import com.sam_chordas.android.stockhawk.rest.request.listener.RequestListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Javier Godino on 24/08/2016.
 * Email: jgodort.software@gmail.com
 */

public class RequestCallback<T> implements Callback<T> {
    protected RequestListener<T> mListener;


    public RequestCallback(RequestListener<T> listener) {
        mListener = listener;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            mListener.onSuccess(response.body());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        mListener.onFailure((T) call);
    }
}
