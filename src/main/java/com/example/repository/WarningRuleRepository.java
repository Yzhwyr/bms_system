package com.example.repository;

import com.example.model.WarningRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarningRuleRepository extends JpaRepository<WarningRule, Long> {
    List<WarningRule> findByRuleIdAndBatteryType(Long ruleId, String batteryType);
}