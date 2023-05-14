package com.chatapi.sigmaapi.service.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.chatapi.sigmaapi.entity.User;
import com.chatapi.sigmaapi.entity.UserOnlineStatus;
import com.chatapi.sigmaapi.entity.enum_model.UserStatus;
import com.chatapi.sigmaapi.repositories.UserOnlineStatusRepository;
import com.chatapi.sigmaapi.repositories.UserRepository;
import com.chatapi.sigmaapi.storage.StorageManager;
import com.chatapi.sigmaapi.util.DateFormat;
import com.chatapi.sigmaapi.util.FormatString;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RegisterServiceIml implements RegisterService {
    @Autowired
    private UserRepository userReponsitory;
    @Autowired
    private UserOnlineStatusRepository userOnlineStatusRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    static final String Account_Sid = "ACdfb0d069b551c3bb92541b4d7536ea92";
    static final String Auth_Token = "c471fc354bd3883fa337dc51e0603468";
    static final String Trial_Phone = "+18126132180";
    static final String From_Mail = "minhchien77777@gmail.com";

    @Override
    public Boolean genrateOTPAndSendOnMobile(String phone) {
        Twilio.init(Account_Sid, Auth_Token);
        int otp = (int) (Math.random() * 9000) + 1000;
        User newUser = new User();
        newUser.setPhone(phone);
        Message message = Message
                .creator(new PhoneNumber(newUser.getPhone()), new PhoneNumber(Trial_Phone),
                        "Mã OTP của bạn là: " + otp + ".Xin hãy xác thực")
                .create();
        if (message.getErrorCode() == null) {
            // save(newUser);
            return true;
        } else
            return false;
    }

    @Override
    public User verifyOTP(String phone, String otp) {
        // User user =
        // userReponsitory.findByPhone(FormatString.customPhoneVNRegion(phone));
        // if (user.getOtp().equals(otp)) {
        // return user;
        // }
        return null;
    }

    @Override
    public void genrateOTPAndSendOnEmail(String email) {
        if (FormatString.getAccountTest(email)) {
            try {
                StorageManager.getInstance().setTempUserByEmail(email, "000000");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        try {
            String otp = FormatString.getRandomNumberOtp();
            simpleMailMessage.setFrom(From_Mail);
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("AppChat - Verify your email");
            simpleMailMessage.setText("Mã OTP của bạn là: " + otp + ". Xin hãy xác minh");
            javaMailSender.send(simpleMailMessage);
            try {
                StorageManager.getInstance().setTempUserByEmail(email, otp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (MailException e) {
            throw e;
        }
    }

    @Override
    public User verifyOTPMail(String email, String otp) {
        String otpOfTempAccount = StorageManager.getInstance().getOtpTempAccount(email);
        if (otpOfTempAccount.equals(otp)) {
            User newUser = new User().setFirstRegisterByMail(email);
            StorageManager.getInstance().deleteTempAccount(email);
            return saveByEmail(newUser);
        }
        StorageManager.getInstance().deleteTempAccount(email);
        return null;
    }

    public User saveByEmail(User user) {
        try {
            userReponsitory.saveAndFlush(user);
            UserOnlineStatus newUserStatus = new UserOnlineStatus();
            newUserStatus.setUserId(user.getUserId());
            newUserStatus.setStatus(UserStatus.OFFLINE.ordinal());
            newUserStatus.setLastTimeOnline(DateFormat.getCurrentTime());
            userOnlineStatusRepository.save(newUserStatus);     
            return user;
        } catch (Exception e) {
            log.error(e.toString());
            return userReponsitory.findByEmail(user.getEmail());
        }
    }
}
