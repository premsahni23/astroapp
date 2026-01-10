package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.ResourceNotFoundException;
import com.dekhokaun.mindarobackend.model.Kundli;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.payload.request.KundliAnalysisRequest;
import com.dekhokaun.mindarobackend.payload.request.KundliGenerateRequest;
import com.dekhokaun.mindarobackend.payload.request.KundliMatchRequest;
import com.dekhokaun.mindarobackend.repository.KundliRepository;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KundliService {

    private final KundliRepository kundliRepository;
    private final UserRepository userRepository;

    public Kundli generate(KundliGenerateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Kundli k = new Kundli();
        k.setUser(user);
        k.setFullName(request.getFullName());
        k.setDob(request.getDob());
        k.setTob(request.getTob());
        k.setPlace(request.getPlace());
        // NOTE: chartJson would be filled by a real kundli provider integration.
        k.setChartJson("{\"status\":\"generated\",\"provider\":\"stub\"}");
        return kundliRepository.save(k);
    }

    public List<Kundli> byUser(UUID userId) {
        return kundliRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Map<String, Object> match(KundliMatchRequest request) {
        Kundli a = kundliRepository.findById(request.getKundliAId())
                .orElseThrow(() -> new ResourceNotFoundException("Kundli A not found"));
        Kundli b = kundliRepository.findById(request.getKundliBId())
                .orElseThrow(() -> new ResourceNotFoundException("Kundli B not found"));

        Map<String, Object> res = new HashMap<>();
        res.put("match", "stub");
        res.put("kundliA", a.getId());
        res.put("kundliB", b.getId());
        res.put("score", 0);
        res.put("note", "Integrate a kundli matching provider to compute real score.");
        return res;
    }

    public Map<String, Object> analysis(KundliAnalysisRequest request) {
        Kundli k = kundliRepository.findById(request.getKundliId())
                .orElseThrow(() -> new ResourceNotFoundException("Kundli not found"));

        Map<String, Object> res = new HashMap<>();
        res.put("analysis", "stub");
        res.put("kundliId", k.getId());
        res.put("focus", request.getFocus());
        res.put("note", "Integrate a kundli analysis provider to compute real insights.");
        return res;
    }
}
