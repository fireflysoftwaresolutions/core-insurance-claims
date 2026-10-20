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


package io.fireflysoftware.core.insurance.claims.models.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import io.fireflysoftware.core.insurance.claims.interfaces.enums.RecoveryStatus;
import io.fireflysoftware.core.insurance.claims.interfaces.enums.RecoveryType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Subrogation, salvage, or contribution pursued on a claim.
 * Maps to the 'claim_recovery' table.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("claim_recovery")
public class ClaimRecovery {

    @Id
    @Column("claim_recovery_id")
    private UUID claimRecoveryId;

    @Column("claim_id")
    private UUID claimId;

    @Column("recovery_type")
    private RecoveryType recoveryType;

    @Column("recovery_status")
    private RecoveryStatus recoveryStatus;

    @Column("expected_amount")
    private BigDecimal expectedAmount;

    @Column("recovered_amount")
    private BigDecimal recoveredAmount;

    @Column("currency")
    private String currency;

    @Column("counterparty_party_id")
    private UUID counterpartyPartyId;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
