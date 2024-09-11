package com.blueOcean.humanResourceSystem.Controller;

import com.blueOcean.humanResourceSystem.Model.ClockInRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/ClockIn")
@CrossOrigin(origins = "http://localhost:5173")
public class ClockInController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final long TTL_SECONDS = 3 * 24 * 60 * 60; // TTL of 3 days in seconds

    @PostMapping("/normalClockIn")
    public ResponseEntity<String> clockIn(@RequestBody ClockInRequest request) {
//        System.out.println("normalClockIn go");

        String datePart =  request.getClockInTime().split(",")[0].trim(); // Extract part before the comma

        // Construct Redis key
        String key = request.getUsername() + ":" + datePart + ":" + request.getIsMorning();
        // Create a map to store in Redis hash
        Map<String, Object> clockInData = new HashMap<>();
        clockInData.put("clockInTime", request.getClockInTime());
        clockInData.put("location", request.getLocation());
        clockInData.put("locationName", request.getLocationName());
        clockInData.put("deviceInfo", request.getDeviceInfo());

        // Store data in Redis hash
        redisTemplate.opsForHash().putAll(key, clockInData);
        // Set TTL for the key
        redisTemplate.expire(key, TTL_SECONDS, TimeUnit.SECONDS);

        // Set bitmap key for completion tracking
        if (request.getIsMorning() == 0) {
            LocalDate today = LocalDate.now();
//            System.out.println(today);
            String year = String.valueOf(today.getYear());
            int dayOfYear = today.getDayOfYear();
//            System.out.println(dayOfYear);
            // Track clock-in using bitmap
            String bitmapKey = request.getUsername() + ":" + year;
            redisTemplate.opsForValue().setBit(bitmapKey, dayOfYear-1, true);
        }

//        System.out.println(request);
        return ResponseEntity.ok("Clock-in successful");
    }

    @GetMapping("/hash")
    public ResponseEntity<Map<Object, Object>> getHashByKey(@RequestParam("key") String key) {
//        System.out.println(key);
        Map<Object, Object> hash = redisTemplate.opsForHash().entries(key);

//        if (hash.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        return ResponseEntity.ok(hash);
        // Respond with 200 OK even if the hash is empty
        return ResponseEntity.ok(hash);
    }
}
