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
import com.firefly.core.insurance.claims.core.mappers.SiuReferralMapper;
import com.firefly.core.insurance.claims.core.services.SiuReferralService;
import com.firefly.core.insurance.claims.interfaces.dtos.SiuReferralDTO;
import com.firefly.core.insurance.claims.models.entities.SiuReferral;
import com.firefly.core.insurance.claims.models.repositories.SiuReferralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class SiuReferralServiceImpl implements SiuReferralService {

    @Autowired
    private SiuReferralRepository repository;

    @Autowired
    private SiuReferralMapper mapper;

    @Override
    public Mono<PaginationResponse<SiuReferralDTO>> filterSiuReferrals(UUID claimId, FilterRequest<SiuReferralDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new SiuReferralDTO());
        }
        filterRequest.getFilters().setClaimId(claimId);
        return FilterUtils.createFilter(SiuReferral.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<SiuReferralDTO> createSiuReferral(UUID claimId, SiuReferralDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setClaimId(claimId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<SiuReferralDTO> updateSiuReferral(UUID claimId, UUID siuReferralId, SiuReferralDTO dto) {
        return repository.findById(siuReferralId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("SiuReferral", siuReferralId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "SiuReferral with id '" + siuReferralId + "' does not belong to claim '" + claimId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteSiuReferral(UUID claimId, UUID siuReferralId) {
        return repository.findById(siuReferralId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("SiuReferral", siuReferralId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "SiuReferral with id '" + siuReferralId + "' does not belong to claim '" + claimId + "'"));
                    }
                    return repository.deleteById(siuReferralId);
                });
    }

    @Override
    public Mono<SiuReferralDTO> getSiuReferralById(UUID claimId, UUID siuReferralId) {
        return repository.findById(siuReferralId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("SiuReferral", siuReferralId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "SiuReferral with id '" + siuReferralId + "' does not belong to claim '" + claimId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
