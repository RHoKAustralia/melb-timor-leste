package org.rhok.linguist.network;

import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

import org.rhok.linguist.application.LinguistApplication;


/**
 * Created by bramleyt on 20/07/2015.
 */
public abstract class BaseIonCallback<T> implements FutureCallback<Response<T>> {
    private Response<T> response;

    public void onCompleted(Exception e, Response<T> response) {
        if(response==null) response=new Response<>(null, null, null, e, null);
        this.response = response;
        if (e == null
                && response.getHeaders()!=null
                && response.getHeaders().code() < 400
                && response.getResult() != null

                )
            onSuccess(response.getResult());
        else onError(response);

    }

    public Response<T> getResponse() {
        return response;
    }

    public abstract void onSuccess(T result);

    public void onError(Response<T> response) {
        String msg= NetworkErrorHandler.getErrorMessage(response);
        Toast.makeText(LinguistApplication.getContextStatic(), msg, Toast.LENGTH_SHORT).show();
    }

}
