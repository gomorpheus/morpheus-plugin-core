package com.morpheusdata.model;

import java.time.Instant;
import java.util.List;

import com.morpheusdata.model.CheckLevel.CheckLevelEnum;

import java.util.ArrayList;

/**
 * Represents the state of a drift check, including its status,
 * progress, timestamps, and results of executed drift rules.
 */
public class DriftState {
    protected String code;               // unique code if needed
    protected String refType;            // associated type of object this drift check is for
    protected Long refId;                // associated id of the object this drift check is for

    protected DriftCheckStatus status;     // status of the drift check (e.g., pending, in-progress, failed, completed)
    protected Integer percentComplete = 0; // completion percentage

    protected Instant startedAt;            // start timestamp
    protected Instant completedAt;          // completion timestamp

    protected DriftSummary driftSummary;    // summary of the drift check (e.g., no-drift, drift-detected, error)
    protected List<DriftRuleResult> ruleResults = new ArrayList<>(); // list of rules executed and their status
    protected CheckLevelEnum checkLevel; // level of the drift check (e.g., all, update)


    /** Enum for drift summary. */
    public enum DriftSummary { NO_DRIFT, DRIFT_DETECTED, ERROR }

    /** Enum for drift check status. */
    public enum DriftCheckStatus { PENDING, IN_PROGRESS, FAILED, COMPLETED }

    /** Enum for rule status. */
    public enum RuleStatus { PASSED, FAILED, SKIPPED, WARNING }

    /**
     * Drift rule result structure.
     */
    public static class DriftRuleResult {
    protected String id;                  // unique id
    protected String ruleName;            // name
    protected String ruleDescription;     // additional details about the rule
    protected String ruleType;            // DHCI vs non-DHCI etc.
    protected RuleStatus status;          // passed / failed / skipped
    protected String resultDescription;   // failure details with embedded resource info
    protected List<String> affectedObjects = new ArrayList<>(); // list of affected objects/resources
    protected String recommendation;      // how to resolve the issue

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getRuleName() { return ruleName; }
        public void setRuleName(String ruleName) { this.ruleName = ruleName; }

        public String getRuleDescription() { return ruleDescription; }
        public void setRuleDescription(String ruleDescription) { this.ruleDescription = ruleDescription; }

        public String getRuleType() { return ruleType; }
        public void setRuleType(String ruleType) { this.ruleType = ruleType; }

        public RuleStatus getStatus() { return status; }
        public void setStatus(RuleStatus status) { this.status = status; }

        public String getResultDescription() { return resultDescription; }
        public void setResultDescription(String resultDescription) { this.resultDescription = resultDescription; }

        public List<String> getAffectedObjects() { return affectedObjects; }
        public void setAffectedObjects(List<String> affectedObjects) { this.affectedObjects = affectedObjects; }

        public String getRecommendation() { return recommendation; }
        public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
    }

    // Getters and setters for DriftState
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getRefType() { return refType; }
    public void setRefType(String refType) { this.refType = refType; }

    public Long getRefId() { return refId; }
    public void setRefId(Long refId) { this.refId = refId; }

    public DriftCheckStatus getStatus() { return status; }
    public void setStatus(DriftCheckStatus status) { this.status = status; }

    public Integer getPercentComplete() { return percentComplete; }
    public void setPercentComplete(Integer percentComplete) { this.percentComplete = percentComplete; }

    public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant startedAt) { this.startedAt = startedAt; }

    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }

    public DriftSummary getDriftSummary() { return driftSummary; }
    public void setDriftSummary(DriftSummary driftSummary) { this.driftSummary = driftSummary; }

    public List<DriftRuleResult> getRuleResults() { return ruleResults; }
    public void setRuleResults(List<DriftRuleResult> ruleResults) { this.ruleResults = ruleResults; }
    
    public CheckLevelEnum getCheckLevel() { return checkLevel; }
    public void setCheckLevel(CheckLevelEnum checkLevel) { this.checkLevel = checkLevel; }
}