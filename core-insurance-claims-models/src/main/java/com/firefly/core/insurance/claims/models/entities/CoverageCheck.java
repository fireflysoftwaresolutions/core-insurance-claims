/*
 * Copyright 2026 Firefly Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.firefly.core.insurance.claims.models.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.firefly.core.insurance.claims.interfaces.enums.CoverageCheckOutcome;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Record of the coverage facts checked for a claim: in force, trigger, and deductible.
 * Maps to the 'coverage_check' table.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("coverage_check")
public class CoverageCheck {

    @Id
    @Column("coverage_check_id")
    private UUID coverageCheckId;

    @Column("claim_id")
    private UUID claimId;

    @Column("policy_id")
    private UUID policyId;

    @Column("coverage_id")
    private UUID coverageId;

    @Column("in_force")
    private Boolean inForce;

    @Column("trigger_met")
    private Boolean triggerMet;

    @Column("deductible_amount")
    private BigDecimal deductibleAmount;

    @Column("check_outcome")
    private CoverageCheckOutcome checkOutcome;

    @Column("checked_at")
    private LocalDateTime checkedAt;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
