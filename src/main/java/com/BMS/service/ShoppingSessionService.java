package com.BMS.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ShoppingSessionService {
    public String startSession(HttpServletResponse response, HttpServletRequest request);
}
