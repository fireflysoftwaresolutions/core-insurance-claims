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

import io.fireflysoftware.core.insurance.claims.interfaces.enums.SettlementStatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fireflyframework.utils.annotations.FilterableId;

/**
 * Authorized settlement. Payment execution is a payment order, not this record..
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimSettlementDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID claimSettlementId;

    @FilterableId
    @NotNull(message = "Claim id is required")
    private UUID claimId;

    /** Logical reference: core-common-customer-mgmt party. This service does not own that record. */
    @FilterableId
    @NotNull(message = "payeePartyId is required")
    private UUID payeePartyId;

    @NotNull(message = "settlementStatus is required")
    private SettlementStatus settlementStatus;

    @NotNull(message = "authorizedAmount is required")
    private BigDecimal authorizedAmount;

    @Pattern(regexp = "[A-Z]{3}", message = "currency must be a 3-letter ISO code")
    private String currency;

    /** Logical reference: core-banking-payments payment order. This service does not own that record. */
    @FilterableId
    private UUID paymentOrderId;

    private LocalDateTime authorizedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
