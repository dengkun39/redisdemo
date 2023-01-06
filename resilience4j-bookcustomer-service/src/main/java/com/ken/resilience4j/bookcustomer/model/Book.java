package com.ken.resilience4j.bookcustomer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {

    private Long id;

    private String name;

    private String author;

    private Date createTime;
    private Date updateTime;
}


