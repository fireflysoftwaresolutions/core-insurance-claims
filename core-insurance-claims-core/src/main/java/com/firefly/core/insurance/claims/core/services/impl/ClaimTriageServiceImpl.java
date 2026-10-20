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


package com.firefly.core.insurance.claims.core.services.impl;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.web.error.exceptions.ResourceNotFoundException;
import com.firefly.core.insurance.claims.core.mappers.ClaimTriageMapper;
import com.firefly.core.insurance.claims.core.services.ClaimTriageService;
import com.firefly.core.insurance.claims.interfaces.dtos.ClaimTriageDTO;
import com.firefly.core.insurance.claims.models.entities.ClaimTriage;
import com.firefly.core.insurance.claims.models.repositories.ClaimTriageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class ClaimTriageServiceImpl implements ClaimTriageService {

    @Autowired
    private ClaimTriageRepository repository;

    @Autowired
    private ClaimTriageMapper mapper;

    @Override
    public Mono<PaginationResponse<ClaimTriageDTO>> filterClaimTriages(UUID claimId, FilterRequest<ClaimTriageDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new ClaimTriageDTO());
        }
        filterRequest.getFilters().setClaimId(claimId);
        return FilterUtils.createFilter(ClaimTriage.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<ClaimTriageDTO> createClaimTriage(UUID claimId, ClaimTriageDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setClaimId(claimId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<ClaimTriageDTO> updateClaimTriage(UUID claimId, UUID claimTriageId, ClaimTriageDTO dto) {
        return repository.findById(claimTriageId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("ClaimTriage", claimTriageId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "ClaimTriage with id '" + claimTriageId + "' does not belong to claim '" + claimId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteClaimTriage(UUID claimId, UUID claimTriageId) {
        return repository.findById(claimTriageId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("ClaimTriage", claimTriageId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "ClaimTriage with id '" + claimTriageId + "' does not belong to claim '" + claimId + "'"));
                    }
                    return repository.deleteById(claimTriageId);
                });
    }

    @Override
    public Mono<ClaimTriageDTO> getClaimTriageById(UUID claimId, UUID claimTriageId) {
        return repository.findById(claimTriageId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("ClaimTriage", claimTriageId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "ClaimTriage with id '" + claimTriageId + "' does not belong to claim '" + claimId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
