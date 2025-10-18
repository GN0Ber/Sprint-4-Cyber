package com.wise.buddy.wiseBuddy.privacy;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "user_consent")
public class UserConsent {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long userId;
  private String policyVersion; // ex: "v1.0-2025-10-10"
  private Instant consentAt;
  private String ip;

  public UserConsent() {}

  public UserConsent(Long userId, String policyVersion, Instant consentAt, String ip) {
    this.userId = userId;
    this.policyVersion = policyVersion;
    this.consentAt = consentAt;
    this.ip = ip;
  }

  public Long getId() { return id; }
  public Long getUserId() { return userId; }
  public void setUserId(Long userId) { this.userId = userId; }
  public String getPolicyVersion() { return policyVersion; }
  public void setPolicyVersion(String policyVersion) { this.policyVersion = policyVersion; }
  public Instant getConsentAt() { return consentAt; }
  public void setConsentAt(Instant consentAt) { this.consentAt = consentAt; }
  public String getIp() { return ip; }
  public void setIp(String ip) { this.ip = ip; }
}
