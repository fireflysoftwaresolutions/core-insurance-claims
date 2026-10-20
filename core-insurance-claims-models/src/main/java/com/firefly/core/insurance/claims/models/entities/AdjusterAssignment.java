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

import com.firefly.core.insurance.claims.interfaces.enums.AssignmentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Adjuster assigned to a claim. The adjuster is a party..
 * Maps to the 'adjuster_assignment' table.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("adjuster_assignment")
public class AdjusterAssignment {

    @Id
    @Column("adjuster_assignment_id")
    private UUID adjusterAssignmentId;

    @Column("claim_id")
    private UUID claimId;

    @Column("adjuster_party_id")
    private UUID adjusterPartyId;

    @Column("assigned_at")
    private LocalDateTime assignedAt;

    @Column("assignment_status")
    private AssignmentStatus assignmentStatus;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
