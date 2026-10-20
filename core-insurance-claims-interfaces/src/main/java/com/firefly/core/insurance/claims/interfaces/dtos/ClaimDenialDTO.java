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


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fireflyframework.utils.annotations.FilterableId;

/**
 * Denial recorded by the insurer. This service does not decide coverage..
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimDenialDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID claimDenialId;

    @FilterableId
    @NotNull(message = "Claim id is required")
    private UUID claimId;

    @NotBlank(message = "denialReason is required")
    @Size(max = 4000, message = "denialReason must not exceed 4000 characters")
    private String denialReason;

    @NotNull(message = "deniedAt is required")
    private LocalDateTime deniedAt;

    @Size(max = 200, message = "authorityReference must not exceed 200 characters")
    private String authorityReference;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
