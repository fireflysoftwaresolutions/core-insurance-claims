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


package io.fireflysoftware.core.insurance.claims.core.services;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.insurance.claims.interfaces.dtos.AdjusterAssignmentDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository operations for adjuster assigned to a claim. The adjuster is a party..
 */
public interface AdjusterAssignmentService {

    /**
     * Filters adjusterAssignments with the given criteria.
     *
     * @param parentId and filterRequest filter criteria
     * @return a page of AdjusterAssignmentDTO
     */
    Mono<PaginationResponse<AdjusterAssignmentDTO>> filterAdjusterAssignments(UUID claimId, FilterRequest<AdjusterAssignmentDTO> filterRequest);

    /**
     * Creates a AdjusterAssignment.
     *
     * @param parentId and dto values to store
     * @return the stored DTO
     */
    Mono<AdjusterAssignmentDTO> createAdjusterAssignment(UUID claimId, AdjusterAssignmentDTO dto);

    /**
     * Updates a AdjusterAssignment.
     *
     * @param parentId, id and dto
     * @return the updated DTO
     */
    Mono<AdjusterAssignmentDTO> updateAdjusterAssignment(UUID claimId, UUID adjusterAssignmentId, AdjusterAssignmentDTO dto);

    /**
     * Deletes a AdjusterAssignment.
     *
     * @param parentId and id identifier
     * @return completion signal
     */
    Mono<Void> deleteAdjusterAssignment(UUID claimId, UUID adjusterAssignmentId);

    /**
     * Loads a AdjusterAssignment by id.
     *
     * @param parentId and id identifier
     * @return the DTO
     */
    Mono<AdjusterAssignmentDTO> getAdjusterAssignmentById(UUID claimId, UUID adjusterAssignmentId);
}
