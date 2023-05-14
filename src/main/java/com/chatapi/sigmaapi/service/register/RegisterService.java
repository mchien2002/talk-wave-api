package com.chatapi.sigmaapi.service.register;

import com.chatapi.sigmaapi.entity.User;

public interface RegisterService {
    Boolean genrateOTPAndSendOnMobile(String phone);
    User verifyOTP(String phone, String otp);
    void genrateOTPAndSendOnEmail(String email);
    User verifyOTPMail(String email, String otp);    
}
