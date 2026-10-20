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
import io.fireflysoftware.core.insurance.claims.interfaces.dtos.ClaimReserveDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository operations for reserve recorded against a claim.
 */
public interface ClaimReserveService {

    /**
     * Filters claimReserves with the given criteria.
     *
     * @param parentId and filterRequest filter criteria
     * @return a page of ClaimReserveDTO
     */
    Mono<PaginationResponse<ClaimReserveDTO>> filterClaimReserves(UUID claimId, FilterRequest<ClaimReserveDTO> filterRequest);

    /**
     * Creates a ClaimReserve.
     *
     * @param parentId and dto values to store
     * @return the stored DTO
     */
    Mono<ClaimReserveDTO> createClaimReserve(UUID claimId, ClaimReserveDTO dto);

    /**
     * Updates a ClaimReserve.
     *
     * @param parentId, id and dto
     * @return the updated DTO
     */
    Mono<ClaimReserveDTO> updateClaimReserve(UUID claimId, UUID claimReserveId, ClaimReserveDTO dto);

    /**
     * Deletes a ClaimReserve.
     *
     * @param parentId and id identifier
     * @return completion signal
     */
    Mono<Void> deleteClaimReserve(UUID claimId, UUID claimReserveId);

    /**
     * Loads a ClaimReserve by id.
     *
     * @param parentId and id identifier
     * @return the DTO
     */
    Mono<ClaimReserveDTO> getClaimReserveById(UUID claimId, UUID claimReserveId);
}
