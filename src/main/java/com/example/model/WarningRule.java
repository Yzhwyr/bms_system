package com.example.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class WarningRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int ruleId;
    private String name;
    private String batteryType;
    private String warningRule;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBatteryType() {
        return batteryType;
    }

    public void setBatteryType(String batteryType) {
        this.batteryType = batteryType;
    }

    public String getWarningRule() {
        return warningRule;
    }

    public void setWarningRule(String warningRule) {
        this.warningRule = warningRule;
    }

}