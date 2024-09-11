package com.blueOcean.humanResourceSystem;

import com.blueOcean.humanResourceSystem.Model.ClockInRequest;
import com.blueOcean.humanResourceSystem.Model.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class HumanResourceSystemApplicationTests {

	@Test
	void contextLoads() {
	}
        @Autowired
        private RestTemplate restTemplate;

        private static final String URL = "http://localhost:5000/api/v1/ClockIn/normalClockIn";

        @Test
        public void testStressLoad() {
            ExecutorService executor = Executors.newFixedThreadPool(10); // Adjust thread pool size as needed

            for (int i = 1; i <= 10000; i++) {
                final int index = i;
                executor.submit(() -> {
                    ClockInRequest request = new ClockInRequest();
                    request.setUsername("TB_" + String.format("%03d", index));
                    request.setClockInTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("M/d/yyyy, HH:mm:ss")));
                    request.setLocation(new Location(37.7749, -122.4194)); // Example location
                    request.setLocationName("Example Location");
                    request.setDeviceInfo("Device_" + index);
                    request.setIsMorning((byte) 1);

                    ResponseEntity<String> response = restTemplate.postForEntity(URL, request, String.class);
                    assertEquals(200, response.getStatusCodeValue(), "Status code should be 200 OK");
                });
            }

            executor.shutdown(); // Stop accepting new tasks and start shutdown process

            // Wait until all submitted tasks have finished
            while (!executor.isTerminated()) {
                // This loop does nothing but waits for all tasks to finish
                // No need to put any code here, as we only need to block until isTerminated() returns true
            }
        }
}
