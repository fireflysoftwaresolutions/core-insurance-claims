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


package io.fireflysoftware.core.insurance.claims.core.services.impl;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.web.error.exceptions.ResourceNotFoundException;
import io.fireflysoftware.core.insurance.claims.core.mappers.ClaimRecoveryMapper;
import io.fireflysoftware.core.insurance.claims.core.services.ClaimRecoveryService;
import io.fireflysoftware.core.insurance.claims.interfaces.dtos.ClaimRecoveryDTO;
import io.fireflysoftware.core.insurance.claims.models.entities.ClaimRecovery;
import io.fireflysoftware.core.insurance.claims.models.repositories.ClaimRecoveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class ClaimRecoveryServiceImpl implements ClaimRecoveryService {

    @Autowired
    private ClaimRecoveryRepository repository;

    @Autowired
    private ClaimRecoveryMapper mapper;

    @Override
    public Mono<PaginationResponse<ClaimRecoveryDTO>> filterClaimRecoveries(UUID claimId, FilterRequest<ClaimRecoveryDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new ClaimRecoveryDTO());
        }
        filterRequest.getFilters().setClaimId(claimId);
        return FilterUtils.createFilter(ClaimRecovery.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<ClaimRecoveryDTO> createClaimRecovery(UUID claimId, ClaimRecoveryDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setClaimId(claimId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<ClaimRecoveryDTO> updateClaimRecovery(UUID claimId, UUID claimRecoveryId, ClaimRecoveryDTO dto) {
        return repository.findById(claimRecoveryId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("ClaimRecovery", claimRecoveryId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "ClaimRecovery with id '" + claimRecoveryId + "' does not belong to claim '" + claimId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteClaimRecovery(UUID claimId, UUID claimRecoveryId) {
        return repository.findById(claimRecoveryId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("ClaimRecovery", claimRecoveryId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "ClaimRecovery with id '" + claimRecoveryId + "' does not belong to claim '" + claimId + "'"));
                    }
                    return repository.deleteById(claimRecoveryId);
                });
    }

    @Override
    public Mono<ClaimRecoveryDTO> getClaimRecoveryById(UUID claimId, UUID claimRecoveryId) {
        return repository.findById(claimRecoveryId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("ClaimRecovery", claimRecoveryId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "ClaimRecovery with id '" + claimRecoveryId + "' does not belong to claim '" + claimId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
