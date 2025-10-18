package com.wise.buddy.wiseBuddy.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Import do conversor de criptografia (LGPD)
import com.wise.buddy.wiseBuddy.crypto.AesGcmConverter;

@Entity
@Table(name = "user")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long Id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "surname", nullable = false, length = 100)
    private String surname;

    // Campo de dado pessoal sensível – agora criptografado em repouso (LGPD)
    @Convert(converter = AesGcmConverter.class)
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "income_range")
    private String incomeRange;

    @Column(name = "sing_on_date")
    private LocalDateTime signOnDate;

    public UserModel() {}

    public UserModel(Long id, String name, String surname, String email, String passwordHash, String incomeRange, LocalDateTime signOnDate) {
        this.Id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.passwordHash = passwordHash;
        this.incomeRange = incomeRange;
        this.signOnDate = signOnDate;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getIncomeRange() {
        return incomeRange;
    }

    public void setIncomeRange(String incomeRange) {
        this.incomeRange = incomeRange;
    }

    public LocalDateTime getSignOnDate() {
        return signOnDate;
    }

    public void setSignOnDate(LocalDateTime signOnDate) {
        this.signOnDate = signOnDate;
    }
}
