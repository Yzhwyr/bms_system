INSERT INTO car (vid, battery_type, total_mileage, health_status)
VALUES
    (SUBSTRING(MD5(RAND()), 1, 16), '三元电池', 100, 100),
    (SUBSTRING(MD5(RAND()), 1, 16), '铁锂电池', 600, 95),
    (SUBSTRING(MD5(RAND()), 1, 16), '三元电池', 300, 98);

INSERT INTO warning_rule (rule_id, name, battery_type, warning_rule) VALUES
(1, '电压差报警', '三元电池', '5<=(Mx-Mi),报警等级：0;3<=(Mx-Mi)<5,报警等级：1;1<=(Mx-Mi)<3,报警等级：2;0.6<=(Mx-Mi)<1,报警等级：3;0.2<=(Mx-Mi)<0.6,报警等级：4;(Mx-Mi)<0.2,不报警'),
(1, '电压差报警', '铁锂电池', '2<=(Mx-Mi),报警等级：0;1<=(Mx-Mi)<2,报警等级：1;0.7<=(Mx-Mi)<1,报警等级：2;0.4<=(Mx-Mi)<0.7,报警等级：3;0.2<=(Mx-Mi)<0.4,报警等级：4;(Mx-Mi)<0.2,不报警'),
(2, '电流差报警', '三元电池', '3<=(Ix-Ii),报警等级：0;1<=(Ix-Ii)<3,报警等级：1;0.2<=(Ix-Ii)<1,报警等级：2;(Ix-Ii)<0.2,不报警'),
(2, '电流差报警', '铁锂电池', '1<=(Ix-Ii),报警等级：0;0.5<=(Ix-Ii)<1,报警等级：1;0.2<=(Ix-Ii)<0.5,报警等级：2;(Ix-Ii)<0.2,不报警');