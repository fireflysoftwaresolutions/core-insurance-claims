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


package io.fireflysoftware.core.insurance.claims.interfaces.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import io.fireflysoftware.core.insurance.claims.interfaces.enums.CoverageCheckOutcome;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fireflyframework.utils.annotations.FilterableId;

/**
 * Record of the coverage facts checked for a claim: in force, trigger, and deductible.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoverageCheckDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID coverageCheckId;

    @FilterableId
    @NotNull(message = "Claim id is required")
    private UUID claimId;

    /** Logical reference: core-insurance-policies policy. This service does not own that record. */
    @FilterableId
    @NotNull(message = "policyId is required")
    private UUID policyId;

    /** Logical reference: core-insurance-policies coverage. This service does not own that record. */
    @FilterableId
    private UUID coverageId;

    private Boolean inForce;

    private Boolean triggerMet;

    private BigDecimal deductibleAmount;

    @NotNull(message = "checkOutcome is required")
    private CoverageCheckOutcome checkOutcome;

    @NotNull(message = "checkedAt is required")
    private LocalDateTime checkedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
