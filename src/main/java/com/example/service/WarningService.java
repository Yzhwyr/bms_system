package com.example.service;

import com.example.model.Car;
import com.example.model.WarningRule;
import com.example.repository.CarRepository;
import com.example.repository.WarningRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WarningService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private WarningRuleRepository warningRuleRepository;


    public List<Car> getAllCars() {
        return carRepository.findAll();
    }


    public List<WarningRule> getAllWarningRules() {
        return warningRuleRepository.findAll();
    }


    public Optional<Car> getCarById(int carId) {
        return carRepository.findById((long) carId);
    }


    public List<WarningRule> getRulesByBatteryType(String batteryType) {
        return warningRuleRepository.findAll()
                .stream()
                .filter(rule -> rule.getBatteryType().equals(batteryType))
                .toList();
    }


    public List<WarningRule> getRulesByRuleIdAndBatteryType(Long warnId, String batteryType) {
        return warningRuleRepository.findByRuleIdAndBatteryType(warnId, batteryType);
    }
}
