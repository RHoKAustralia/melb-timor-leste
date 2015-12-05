package org.rhok.linguist.network;

import com.koushikdutta.ion.Response;

import org.rhok.linguist.R;
import org.rhok.linguist.application.LinguistApplication;

/**
 * Created by bramleyt on 5/12/2015.
 */
public class NetworkErrorHandler {

    public static String getErrorMessage(Response<?> result){
        String msg;
        String userMsg;
        if(result.getException()!=null){
            //userMsg = "Request failed: "+getErrorMessageForUser(result.getException());
            msg= "Error: "+ result.getException().getMessage();//+"\nProd message: "+userMsg;
            userMsg=msg;
        }

        else if (result.getHeaders().code()==401){
            userMsg= LinguistApplication.getContextStatic().getString(R.string.request_fail_unauthorized);
            msg= "HTTP "+ result.getHeaders().code()+" "+result.getHeaders().message()+"\nProd message: "+userMsg;
        }
        else{
            userMsg= LinguistApplication.getContextStatic().getString(R.string.request_fail_server_error);
            msg= "HTTP "+ result.getHeaders().code()+" "+result.getHeaders().message()+"\nProd message: "+userMsg;
        }

        if(LinguistApplication.DEBUG){
            return msg;
        }
        else{
            return userMsg;
        }
    }
}
