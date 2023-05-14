package com.chatapi.sigmaapi.controllers.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chatapi.sigmaapi.constant.RouteConstant;
import com.chatapi.sigmaapi.entity.User;
import com.chatapi.sigmaapi.helper.MyResponse;
import com.chatapi.sigmaapi.service.register.JwtService;
import com.chatapi.sigmaapi.service.register.RegisterService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class RegisterController {
    @Autowired
    private RegisterService registerService;
    @Autowired
    private JwtService jwtService;

    @PostMapping(RouteConstant.REGISTER_MOBILE)
    public ResponseEntity<Object> sendOTP(@RequestParam("phone") String phone) {
        try {
            registerService.genrateOTPAndSendOnMobile(phone);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new MyResponse(true, "Mã OTP đang được gửi", null, null));
        } catch (Exception e) {
            log.error("Error: " + e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MyResponse().errorResponse());
        }
    }

    @PostMapping(RouteConstant.VERIFY_OTP_MOBILE)
    public ResponseEntity<Object> verifyOTPLogin(@RequestParam("phone") String phone, @RequestParam("otp") String otp) {
        try {
            if (registerService.verifyOTP(phone, otp) == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new MyResponse(false, "Xác thực tài khoản không thành công", null,
                                registerService.verifyOTP(phone, otp)));
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new MyResponse(true, "Xác thực tài khoản thành công", null,
                            registerService.verifyOTP(phone, otp)));
        } catch (Exception e) {
            log.error("Error: " + e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MyResponse().errorResponse());
        }
    }

    @PostMapping(RouteConstant.REGISTER_EMAIL)
    public ResponseEntity<Object> sendOTPOnMail(@RequestParam("email") String email) {
        try {
            registerService.genrateOTPAndSendOnEmail(email);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new MyResponse(true, "Đã gửi mail xác nhận", null, null));
        } catch (Exception e) {
            log.error("Error: " + e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MyResponse().errorResponse());
        }
    }

    @PostMapping(RouteConstant.VERIFY_OTP_EMAIL)
    public ResponseEntity<Object> verifyOTPLoginOnMail(@RequestParam("email") String email,
            @RequestParam("otp") String otp) {
        try {
            User user = registerService.verifyOTPMail(email, otp);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new MyResponse(false, "Bạn đã nhập sai OTP", null,
                                user));
            }
            user.setToken(jwtService.generateToken(user));
            return ResponseEntity.status(HttpStatus.OK).body(
                    new MyResponse(true, "Xác thực tài khoản thành công", null, user));
        } catch (Exception e) {
            log.error("Error: " + e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MyResponse().errorResponse());
        }
    }
}
