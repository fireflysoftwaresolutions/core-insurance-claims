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


package com.firefly.core.insurance.claims.interfaces.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.firefly.core.insurance.claims.interfaces.enums.ClaimStatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fireflyframework.utils.annotations.FilterableId;

/**
 * Claim notification and file.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID claimId;

    /** Logical reference: Firefly deployment tenant. This service does not own that record. */
    @FilterableId
    @NotNull(message = "tenantId is required")
    private UUID tenantId;

    @NotBlank(message = "claimNumber is required")
    @Size(max = 64, message = "claimNumber must not exceed 64 characters")
    private String claimNumber;

    /** Logical reference: core-insurance-policies policy. This service does not own that record. */
    @FilterableId
    @NotNull(message = "policyId is required")
    private UUID policyId;

    /** Logical reference: core-common-contract-mgmt contract. This service does not own that record. */
    @FilterableId
    private UUID contractId;

    /** Logical reference: core-insurance-policies policy transaction. This service does not own that record. */
    @FilterableId
    private UUID policyTransactionId;

    @NotNull(message = "claimStatus is required")
    private ClaimStatus claimStatus;

    @NotNull(message = "lossDate is required")
    private LocalDate lossDate;

    @NotNull(message = "notifiedAt is required")
    private LocalDateTime notifiedAt;

    @Size(max = 64, message = "causeCode must not exceed 64 characters")
    private String causeCode;

    @Size(max = 4000, message = "description must not exceed 4000 characters")
    private String description;

    @Pattern(regexp = "[A-Z]{3}", message = "currency must be a 3-letter ISO code")
    private String currency;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
