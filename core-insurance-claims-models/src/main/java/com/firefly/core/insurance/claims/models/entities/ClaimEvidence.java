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

import java.time.LocalDateTime;
import java.util.UUID;

import com.firefly.core.insurance.claims.interfaces.enums.ClaimEvidenceType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Evidence reference on a claim. The document bytes stay in document management..
 * Maps to the 'claim_evidence' table.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("claim_evidence")
public class ClaimEvidence {

    @Id
    @Column("claim_evidence_id")
    private UUID claimEvidenceId;

    @Column("claim_id")
    private UUID claimId;

    @Column("document_id")
    private UUID documentId;

    @Column("evidence_type")
    private ClaimEvidenceType evidenceType;

    @Column("received_at")
    private LocalDateTime receivedAt;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
