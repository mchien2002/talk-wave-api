package com.chatapi.sigmaapi.storage;

import java.util.HashMap;
import java.util.Map;

public class StorageManager {
    private static StorageManager instance;
    private Map<String, String> listTempAccount;;

    private StorageManager(){
        listTempAccount = new HashMap<>();
    }
    public static synchronized StorageManager getInstance(){
        if (instance == null){
            instance = new StorageManager();
        }
        return instance;
    } 

    public void setTempUserByEmail(String email, String otp) throws Exception{
        if (listTempAccount.containsKey(email)){
            throw new Exception("User aready exists with email: " + email);
        }
        listTempAccount.put(email, otp);
    }

    public String getOtpTempAccount(String email){
        return listTempAccount.get(email);
    }

    public void deleteTempAccount(String email){
        listTempAccount.remove(email);
    }

}