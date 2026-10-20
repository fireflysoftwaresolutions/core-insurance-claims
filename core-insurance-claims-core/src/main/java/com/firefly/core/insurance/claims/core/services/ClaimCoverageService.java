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
import com.firefly.core.insurance.claims.interfaces.dtos.ClaimCoverageDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository operations for coverage on the policy against which the claim is made.
 */
public interface ClaimCoverageService {

    /**
     * Filters claimCoverages with the given criteria.
     *
     * @param parentId and filterRequest filter criteria
     * @return a page of ClaimCoverageDTO
     */
    Mono<PaginationResponse<ClaimCoverageDTO>> filterClaimCoverages(UUID claimId, FilterRequest<ClaimCoverageDTO> filterRequest);

    /**
     * Creates a ClaimCoverage.
     *
     * @param parentId and dto values to store
     * @return the stored DTO
     */
    Mono<ClaimCoverageDTO> createClaimCoverage(UUID claimId, ClaimCoverageDTO dto);

    /**
     * Updates a ClaimCoverage.
     *
     * @param parentId, id and dto
     * @return the updated DTO
     */
    Mono<ClaimCoverageDTO> updateClaimCoverage(UUID claimId, UUID claimCoverageId, ClaimCoverageDTO dto);

    /**
     * Deletes a ClaimCoverage.
     *
     * @param parentId and id identifier
     * @return completion signal
     */
    Mono<Void> deleteClaimCoverage(UUID claimId, UUID claimCoverageId);

    /**
     * Loads a ClaimCoverage by id.
     *
     * @param parentId and id identifier
     * @return the DTO
     */
    Mono<ClaimCoverageDTO> getClaimCoverageById(UUID claimId, UUID claimCoverageId);
}
