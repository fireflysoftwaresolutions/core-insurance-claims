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


package com.firefly.core.insurance.claims.core.services;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import com.firefly.core.insurance.claims.interfaces.dtos.ClaimTriageDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository operations for routing of a claim to fast-track, complex handling, or SIU.
 */
public interface ClaimTriageService {

    /**
     * Filters claimTriages with the given criteria.
     *
     * @param parentId and filterRequest filter criteria
     * @return a page of ClaimTriageDTO
     */
    Mono<PaginationResponse<ClaimTriageDTO>> filterClaimTriages(UUID claimId, FilterRequest<ClaimTriageDTO> filterRequest);

    /**
     * Creates a ClaimTriage.
     *
     * @param parentId and dto values to store
     * @return the stored DTO
     */
    Mono<ClaimTriageDTO> createClaimTriage(UUID claimId, ClaimTriageDTO dto);

    /**
     * Updates a ClaimTriage.
     *
     * @param parentId, id and dto
     * @return the updated DTO
     */
    Mono<ClaimTriageDTO> updateClaimTriage(UUID claimId, UUID claimTriageId, ClaimTriageDTO dto);

    /**
     * Deletes a ClaimTriage.
     *
     * @param parentId and id identifier
     * @return completion signal
     */
    Mono<Void> deleteClaimTriage(UUID claimId, UUID claimTriageId);

    /**
     * Loads a ClaimTriage by id.
     *
     * @param parentId and id identifier
     * @return the DTO
     */
    Mono<ClaimTriageDTO> getClaimTriageById(UUID claimId, UUID claimTriageId);
}
