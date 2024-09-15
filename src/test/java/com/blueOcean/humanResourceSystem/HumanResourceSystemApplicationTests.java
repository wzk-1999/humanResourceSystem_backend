package com.blueOcean.humanResourceSystem;

import com.blueOcean.humanResourceSystem.Model.BasicLawSalary;
import com.blueOcean.humanResourceSystem.Model.ClockInRequest;
import com.blueOcean.humanResourceSystem.Model.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.URL;
import java.nio.file.Paths;


import static com.blueOcean.humanResourceSystem.Utils.BasicLawSalaryImport.readFromFile;
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

        @Test
        public void testBasicLawSalaryImport() throws URISyntaxException {
            // Get the file from the classpath
            URL resource = getClass().getClassLoader().getResource("static/Base_Salary_202303.txt");

            // Convert the URL to a URI and then to a Path
            URI uri = resource.toURI();
            Path filePath = Paths.get(uri);
//            System.out.println(filePath);
            List<BasicLawSalary> salaryList = readFromFile(String.valueOf(filePath));
            salaryList.forEach(System.out::println);
        }

    @Test
    public void generateAESKey() throws NoSuchAlgorithmException {
        // Generate a 128-bit AES key
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128); // 128, 192, or 256
        SecretKey secretKey = keyGenerator.generateKey();

        // Convert the key to Base64-encoded string for readability
        String keyBase64 = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        // Print the Base64-encoded key
        System.out.println("Generated AES Key (Base64): " + keyBase64);
    }
}
