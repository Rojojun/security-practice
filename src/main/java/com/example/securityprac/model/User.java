package com.example.securityprac.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private int id;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String email;
    @Column
    private String role;
    @CreationTimestamp
    @Column
    private Timestamp createDate;
}
