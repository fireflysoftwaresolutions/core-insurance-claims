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

import java.time.LocalDateTime;
import java.util.UUID;

import io.fireflysoftware.core.insurance.claims.interfaces.enums.IndicatorSeverity;

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
 * Fraud indicator recorded against a claim. Investigation stays with the insurer..
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FraudIndicatorDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID fraudIndicatorId;

    @FilterableId
    @NotNull(message = "Claim id is required")
    private UUID claimId;

    @NotBlank(message = "indicatorCode is required")
    @Size(max = 64, message = "indicatorCode must not exceed 64 characters")
    private String indicatorCode;

    @NotNull(message = "severity is required")
    private IndicatorSeverity severity;

    @NotNull(message = "notedAt is required")
    private LocalDateTime notedAt;

    @Size(max = 4000, message = "notes must not exceed 4000 characters")
    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
