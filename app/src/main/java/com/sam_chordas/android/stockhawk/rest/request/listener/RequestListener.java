package com.sam_chordas.android.stockhawk.rest.request.listener;

/**
 * Created by Javier Godino on 24/08/2016.
 * Email: jgodort.software@gmail.com
 */

public interface RequestListener<T> {

    void onSuccess(T response);

    void onFailure(T error);
}
