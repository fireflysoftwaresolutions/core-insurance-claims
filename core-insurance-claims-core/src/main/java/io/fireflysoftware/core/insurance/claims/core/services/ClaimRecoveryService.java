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
import io.fireflysoftware.core.insurance.claims.interfaces.dtos.ClaimRecoveryDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository operations for subrogation, salvage, or contribution pursued on a claim.
 */
public interface ClaimRecoveryService {

    /**
     * Filters claimRecoveries with the given criteria.
     *
     * @param parentId and filterRequest filter criteria
     * @return a page of ClaimRecoveryDTO
     */
    Mono<PaginationResponse<ClaimRecoveryDTO>> filterClaimRecoveries(UUID claimId, FilterRequest<ClaimRecoveryDTO> filterRequest);

    /**
     * Creates a ClaimRecovery.
     *
     * @param parentId and dto values to store
     * @return the stored DTO
     */
    Mono<ClaimRecoveryDTO> createClaimRecovery(UUID claimId, ClaimRecoveryDTO dto);

    /**
     * Updates a ClaimRecovery.
     *
     * @param parentId, id and dto
     * @return the updated DTO
     */
    Mono<ClaimRecoveryDTO> updateClaimRecovery(UUID claimId, UUID claimRecoveryId, ClaimRecoveryDTO dto);

    /**
     * Deletes a ClaimRecovery.
     *
     * @param parentId and id identifier
     * @return completion signal
     */
    Mono<Void> deleteClaimRecovery(UUID claimId, UUID claimRecoveryId);

    /**
     * Loads a ClaimRecovery by id.
     *
     * @param parentId and id identifier
     * @return the DTO
     */
    Mono<ClaimRecoveryDTO> getClaimRecoveryById(UUID claimId, UUID claimRecoveryId);
}
