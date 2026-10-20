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
import com.firefly.core.insurance.claims.core.mappers.ClaimMapper;
import com.firefly.core.insurance.claims.core.services.ClaimService;
import com.firefly.core.insurance.claims.interfaces.dtos.ClaimDTO;
import com.firefly.core.insurance.claims.models.entities.Claim;
import com.firefly.core.insurance.claims.models.repositories.ClaimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class ClaimServiceImpl implements ClaimService {

    @Autowired
    private ClaimRepository repository;

    @Autowired
    private ClaimMapper mapper;

    @Override
    public Mono<PaginationResponse<ClaimDTO>> filterClaims(FilterRequest<ClaimDTO> filterRequest) {
        return FilterUtils.createFilter(Claim.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<ClaimDTO> createClaim(ClaimDTO dto) {
        return Mono.just(dto)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<ClaimDTO> updateClaim(UUID claimId, ClaimDTO dto) {
        return repository.findById(claimId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("Claim", claimId.toString())))
                .flatMap(existing -> {
                    mapper.updateEntityFromDto(dto, existing);
                    existing.setClaimId(claimId);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteClaim(UUID claimId) {
        return repository.findById(claimId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("Claim", claimId.toString())))
                .flatMap(existing -> repository.deleteById(claimId));
    }

    @Override
    public Mono<ClaimDTO> getClaimById(UUID claimId) {
        return repository.findById(claimId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("Claim", claimId.toString())))
                .map(mapper::toDTO);
    }
}
