package com.example.controller;

import com.example.model.Car;
import com.example.model.WarningRule;
import com.example.service.WarningService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.regex.Pattern;

import java.io.IOException;
import java.util.*;

import static java.sql.DriverManager.println;

@RestController
@RequestMapping("/api")
public class WarningController {

    @Autowired
    private WarningService warningService;

    @PostMapping("/warn")
    public Map<String, Object> warn(@RequestBody List<Map<String, Object>> requests) {
        List<Map<String, Object>> results = new ArrayList<>();
        for (Map<String, Object> request : requests) {
            Integer carId = (Integer) request.get("carId");
            Integer warnId = (Integer) request.get("warnId");
            String signalJson = (String) request.get("signal");
            Optional<Car> carOpt = warningService.getCarById(carId);
            if (carOpt.isPresent()) {
                Car car = carOpt.get();
                System.out.println("Car found: " + car);
                List<WarningRule> rules;
                // 如果 warnId 为空，则返回所有 batteryType 相同的规则，如果 warnId 不为空，则返回 warnId 对应的规则。
                if (warnId == null) {
                    rules = warningService.getRulesByBatteryType(car.getBatteryType());
                } else {
                    rules = warningService.getRulesByRuleIdAndBatteryType(Long.valueOf(warnId), car.getBatteryType());
                }
                Map<String, Double> signals = parseSignal(signalJson);
                //输出检测信息是否匹配正确
                System.out.println("Received request: carId=" + carId + ", warnId=" + warnId + ", signal=" + signalJson);
                System.out.println("Applicable rules: " + rules);

                for (WarningRule rule : rules) {
                    // 这里就返回是个 null
                    Map<String, Object> result = processWarning(car, rule, signals);
                    if (result != null) {
                        results.add(result);
                    }
                }
            }
        }
        return new LinkedHashMap<String, Object>() {{
            // 这里返回的结果是个 LinkedHashMap，保持插入顺序
            put("status", 200);
            put("msg", "ok");
            put("data", results);
        }};
    }

    private Map<String, Double> parseSignal(String signalJson) {
        // 使用Jackson解析JSON字符串
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(signalJson, new TypeReference<Map<String, Double>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    private Map<String, Object> processWarning(Car car, WarningRule rule, Map<String, Double> signals) {
        // 解析警告规则
        String ruleStr = rule.getWarningRule();
        String[] ruleParts = ruleStr.split(";");

        Map<String, Object> result = new LinkedHashMap<>(); // 使用LinkedHashMap保持插入顺序
        result.put("车架编号", car.getId());
        result.put("电池类型", car.getBatteryType());
        result.put("warnName", rule.getName());
        result.put("warnLevel", "不报警"); // 默认情况下设为不报警

        for (String part : ruleParts) {
            String[] conditions = part.split(",报警等级：");

            if (conditions.length < 2) {
                continue; // 跳过当前循环
            }
            String condition = conditions[0].trim();
            int warnLevel = Integer.parseInt(conditions[1].trim());

            if (evaluateCondition(condition, signals)) {
                result.put("warnLevel", warnLevel);
                break; // 找到符合条件的警告等级后退出循环
            }
        }

        return result;
    }

    private boolean evaluateCondition(String condition, Map<String, Double> signals) {
        // 解析警告条件
        double value = evaluateExpression(condition, signals);

        if (condition.contains("<=")) {
            String[] parts = condition.split("<=");
            double threshold = Double.parseDouble(parts[0].trim());
            return value >= threshold;
        } else if (condition.contains("<")) {
            String[] parts = condition.split("<");
            double lowerBound = parts.length > 1 ? Double.parseDouble(parts[0].trim()) : Double.MIN_VALUE;
            double upperBound = Double.parseDouble(parts[1].trim());
            return value >= lowerBound && value < upperBound;
        }
        return false;
    }

    private double evaluateExpression(String condition, Map<String, Double> signals) {
        // 简单解析表达式
        condition = condition.replaceAll("\\(", "").replaceAll("\\)", "");
        String[] parts = condition.split("<=|<");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid condition: " + condition);
        }
        String parts1 = parts[1].trim();
        String[] parts2 = parts1.split("-");
        if (parts2.length != 2) {
            throw new IllegalArgumentException("Invalid condition: " + condition);
        }
        double val1 = signals.getOrDefault(parts2[0].trim(), 0.0);
        double val2 = signals.getOrDefault(parts2[1].trim(), 0.0);
        return val1 - val2;
    }

    @GetMapping("/cars")
    public List<Car> getCars() {
        return warningService.getAllCars();
    }

    @GetMapping("/rules")
    public List<WarningRule> getRules() {
        return warningService.getAllWarningRules();
    }
}