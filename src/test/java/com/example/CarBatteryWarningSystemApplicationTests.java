package com.example;

import com.example.controller.WarningController;
import com.example.model.Car;
import com.example.model.WarningRule;
import com.example.service.WarningService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CarBatteryWarningSystemApplicationTests {

    @InjectMocks
    private WarningController warningController;

    @Mock
    private WarningService warningService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }


    @Test
    public void testWarningSuccessRate() throws IOException {
        // Mock data
        Car car = new Car();
        car.setId(1);
        car.setBatteryType("三元电池");
        WarningRule rule = new WarningRule();
        rule.setRuleId(1);
        rule.setName("电压差报警");
        rule.setBatteryType("三元电池");
        rule.setWarningRule("5<=(Mx-Mi)报警等级：0; 3<=(Mx-Mi)<5报警等级：1; 1<=(Mx-Mi)<3报警等级：2; 0.6<=(Mx-Mi)<1报警等级：3; 0.2<=(Mx-Mi)<0.6报警等级：4; (Mx-Mi)<0.2,不报警");
        when(warningService.getCarById(1)).thenReturn(Optional.of(car));
        when(warningService.getRulesByBatteryType("三元电池")).thenReturn(List.of(rule));

        int totalRequests = 100;
        int successfulWarnings = 0;

        for (int i = 0; i < totalRequests; i++) {
            Map<String, Double> signals = new HashMap<>();
            signals.put("Mx", 4.0 + Math.random());  // Randomize signals slightly for each test
            signals.put("Mi", 1.0 + Math.random());

            List<Map<String, Object>> request = new ArrayList<>();
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("carId", 1);
            requestData.put("warnId", null);
            requestData.put("signal", objectMapper.writeValueAsString(signals));
            request.add(requestData);

            Map<String, Object> response = warningController.warn(request);
            List<Map<String, Object>> responseData = (List<Map<String, Object>>) response.get("data");

            if (!responseData.isEmpty()) {
                successfulWarnings++;
            }
        }
        double successRate = (double) successfulWarnings / totalRequests;
        System.out.println("Warning Success Rate: " + (successRate * 100) + "%");

        assertTrue(successRate >= 0.9, "Warning success rate is less than 90%");
    }
}
