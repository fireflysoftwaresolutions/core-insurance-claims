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

import java.time.LocalDateTime;
import java.util.UUID;

import com.firefly.core.insurance.claims.interfaces.enums.ClaimStatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fireflyframework.utils.annotations.FilterableId;

/**
 * Status change recorded against a claim.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimStatusHistoryDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID claimStatusHistoryId;

    @FilterableId
    @NotNull(message = "Claim id is required")
    private UUID claimId;

    @NotNull(message = "claimStatus is required")
    private ClaimStatus claimStatus;

    @Size(max = 500, message = "statusReason must not exceed 500 characters")
    private String statusReason;

    @NotNull(message = "changedAt is required")
    private LocalDateTime changedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
