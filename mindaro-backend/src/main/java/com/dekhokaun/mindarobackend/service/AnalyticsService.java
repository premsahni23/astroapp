package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.ResourceNotFoundException;
import com.dekhokaun.mindarobackend.model.AnalyticsEvent;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.payload.request.AnalyticsEventRequest;
import com.dekhokaun.mindarobackend.repository.AnalyticsEventRepository;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final UserRepository userRepository;

    public AnalyticsEvent record(AnalyticsEventRequest request) {
        AnalyticsEvent e = new AnalyticsEvent();
        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            e.setUser(user);
        }
        e.setEventName(request.getEventName());
        e.setPropertiesJson(request.getPropertiesJson());
        e.setSource(request.getSource());
        return analyticsEventRepository.save(e);
    }

    public Map<String, Object> dashboard() {
        long total = analyticsEventRepository.countAll();
        long last24h = analyticsEventRepository.countByCreatedAtAfter(LocalDateTime.now().minusHours(24));

        List<Object[]> byEvent = analyticsEventRepository.countByEventName();
        List<Map<String, Object>> top = new ArrayList<>();
        for (Object[] row : byEvent) {
            Map<String, Object> m = new HashMap<>();
            m.put("event_name", String.valueOf(row[0]));
            m.put("count", ((Number) row[1]).longValue());
            top.add(m);
        }
        top.sort((a, b) -> Long.compare((Long) b.get("count"), (Long) a.get("count")));
        if (top.size() > 10) top = top.subList(0, 10);

        Map<String, Object> res = new LinkedHashMap<>();
        res.put("total_events", total);
        res.put("events_last_24h", last24h);
        res.put("top_events", top);
        return res;
    }
}
