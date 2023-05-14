package com.chatapi.sigmaapi.constant;

public class RouteConstant {
    public static final String HEAD_END_POINT = "/api/v1";
    // CONFIG WEBSOCKET
    public static final String TOPIC = "/topic";
    public static final String APP = "/app";
    public static final String WEB_SOCKET_CONNECTION = "/socket-connection/ws";

    // API
    public static final String MOBILE_NUMBER = HEAD_END_POINT + "/mobileNo";
    public static final String REGISTER_MOBILE = HEAD_END_POINT + "/register-by-mobileNo";
    public static final String VERIFY_OTP_MOBILE = HEAD_END_POINT + "/verifyOTP-mobileNo";
    public static final String REGISTER_EMAIL = HEAD_END_POINT + "/register-by-email";
    public static final String VERIFY_OTP_EMAIL = HEAD_END_POINT + "/verifyOTP-email";
    public static final String CREATE_GROUP = HEAD_END_POINT + "/create-group";
    public static final String USER_PROFILE = HEAD_END_POINT + "/user-profile";
    public static final String GROUP_PROFILE = HEAD_END_POINT + "/group-profile";
    public static final String IMAGE_URL = "/image";
    public static final String AUDIO_URL = "/audio";
    public static final String VIDEO_URL = "/video";
    public static final String SEARCH_USER = HEAD_END_POINT + "/search-user";
    public static final String UPDATE_PROFILE_USER = HEAD_END_POINT + "/update-profile-user";
    public static final String UPLOAD_USER_AVATAR = HEAD_END_POINT + "/upload-user-avatar";
    public static final String USER_ONLINE_STATUS = HEAD_END_POINT + "/user-online-status";
    public static final String UPDATE_USER_ONLINE_STATUS = HEAD_END_POINT + "/update-user-online-status";
    public static final String GROUP_BY_MEMBERS = HEAD_END_POINT + "/group-profile-by-member";
    public static final String USER_HAVE_SAME_GROUP = HEAD_END_POINT + "/contact-by-group";

}
