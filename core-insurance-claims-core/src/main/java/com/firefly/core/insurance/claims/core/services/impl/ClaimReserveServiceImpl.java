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
import com.firefly.core.insurance.claims.core.mappers.ClaimReserveMapper;
import com.firefly.core.insurance.claims.core.services.ClaimReserveService;
import com.firefly.core.insurance.claims.interfaces.dtos.ClaimReserveDTO;
import com.firefly.core.insurance.claims.models.entities.ClaimReserve;
import com.firefly.core.insurance.claims.models.repositories.ClaimReserveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class ClaimReserveServiceImpl implements ClaimReserveService {

    @Autowired
    private ClaimReserveRepository repository;

    @Autowired
    private ClaimReserveMapper mapper;

    @Override
    public Mono<PaginationResponse<ClaimReserveDTO>> filterClaimReserves(UUID claimId, FilterRequest<ClaimReserveDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new ClaimReserveDTO());
        }
        filterRequest.getFilters().setClaimId(claimId);
        return FilterUtils.createFilter(ClaimReserve.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<ClaimReserveDTO> createClaimReserve(UUID claimId, ClaimReserveDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setClaimId(claimId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<ClaimReserveDTO> updateClaimReserve(UUID claimId, UUID claimReserveId, ClaimReserveDTO dto) {
        return repository.findById(claimReserveId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("ClaimReserve", claimReserveId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "ClaimReserve with id '" + claimReserveId + "' does not belong to claim '" + claimId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteClaimReserve(UUID claimId, UUID claimReserveId) {
        return repository.findById(claimReserveId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("ClaimReserve", claimReserveId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "ClaimReserve with id '" + claimReserveId + "' does not belong to claim '" + claimId + "'"));
                    }
                    return repository.deleteById(claimReserveId);
                });
    }

    @Override
    public Mono<ClaimReserveDTO> getClaimReserveById(UUID claimId, UUID claimReserveId) {
        return repository.findById(claimReserveId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("ClaimReserve", claimReserveId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "ClaimReserve with id '" + claimReserveId + "' does not belong to claim '" + claimId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
