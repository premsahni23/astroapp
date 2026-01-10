package com.dekhokaun.mindarobackend.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class HoroscopeService {

    public Map<String, Object> daily(String sign) {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("sign", sign);
        res.put("date", LocalDate.now().format(DateTimeFormatter.ISO_DATE));
        res.put("message", "Stub horoscope. Integrate a horoscope provider to return real content.");
        return res;
    }

    public Map<String, Object> weekly(String sign) {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("sign", sign);
        res.put("week_start", LocalDate.now().with(java.time.DayOfWeek.MONDAY).format(DateTimeFormatter.ISO_DATE));
        res.put("message", "Stub weekly horoscope. Integrate a provider for real content.");
        return res;
    }
}
