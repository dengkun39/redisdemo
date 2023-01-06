package com.ken.flinklearn;

import lombok.Data;


@Data
public class AuditTrail {
    int id;
    String user;
    String entity;
    String operation;
    long timestamp;
    int duration;
    int count;

    //Convert String array to AuditTrail object
    public AuditTrail(String auditStr) {

        //Split the string
        String[] attributes = auditStr
                .replace("\"","")
                .split(",");

        //Assign values
        this.id = Integer.valueOf(attributes[0]);
        this.user = attributes[1];
        this.entity = attributes[2];
        this.operation = attributes[3];
        this.timestamp = Long.valueOf(attributes[4]);
        this.duration = Integer.valueOf(attributes[5]);
        this.count = Integer.valueOf(attributes[6]);
    }
}
