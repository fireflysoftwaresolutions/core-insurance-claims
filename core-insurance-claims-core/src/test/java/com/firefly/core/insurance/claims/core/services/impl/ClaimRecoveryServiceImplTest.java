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

import org.fireflyframework.web.error.exceptions.ResourceNotFoundException;
import com.firefly.core.insurance.claims.core.mappers.ClaimRecoveryMapper;
import com.firefly.core.insurance.claims.interfaces.dtos.ClaimRecoveryDTO;
import com.firefly.core.insurance.claims.models.entities.ClaimRecovery;
import com.firefly.core.insurance.claims.models.repositories.ClaimRecoveryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClaimRecoveryServiceImplTest {

    @Mock
    private ClaimRecoveryRepository repository;

    @Mock
    private ClaimRecoveryMapper mapper;

    @InjectMocks
    private ClaimRecoveryServiceImpl service;

    private ClaimRecoveryDTO dto;
    private ClaimRecovery entity;
    private UUID claimRecoveryId;
    private UUID claimId;

    @BeforeEach
    void setUp() {
        claimRecoveryId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        entity = new ClaimRecovery();
        entity.setClaimRecoveryId(claimRecoveryId);
        entity.setCreatedAt(LocalDateTime.parse("2026-01-15T10:15:30"));
        entity.setUpdatedAt(LocalDateTime.parse("2026-01-15T10:15:30"));
        dto = new ClaimRecoveryDTO();
        dto.setClaimRecoveryId(claimRecoveryId);
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        claimId = UUID.fromString("223e4567-e89b-12d3-a456-426614174000");
        entity.setClaimId(claimId);
        dto.setClaimId(claimId);
    }

    @Test
    void create_ShouldReturnDto_WhenSaveSucceeds() {
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        StepVerifier.create(service.createClaimRecovery(claimId, dto))
                .expectNext(dto)
                .verifyComplete();

        verify(repository).save(entity);
    }

    @Test
    void getById_ShouldReturnDto_WhenFound() {
        when(repository.findById(claimRecoveryId)).thenReturn(Mono.just(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        StepVerifier.create(service.getClaimRecoveryById(claimId, claimRecoveryId))
                .expectNext(dto)
                .verifyComplete();
    }

    @Test
    void getById_ShouldError_WhenMissing() {
        when(repository.findById(claimRecoveryId)).thenReturn(Mono.empty());

        StepVerifier.create(service.getClaimRecoveryById(claimId, claimRecoveryId))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void update_ShouldSaveExistingRow() {
        when(repository.findById(claimRecoveryId)).thenReturn(Mono.just(entity));
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        StepVerifier.create(service.updateClaimRecovery(claimId, claimRecoveryId, dto))
                .expectNext(dto)
                .verifyComplete();

        verify(mapper).updateEntityFromDto(dto, entity);
    }

    @Test
    void delete_ShouldDelete_WhenFound() {
        when(repository.findById(claimRecoveryId)).thenReturn(Mono.just(entity));
        when(repository.deleteById(claimRecoveryId)).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteClaimRecovery(claimId, claimRecoveryId))
                .verifyComplete();
    }

    @Test
    void getById_ShouldError_WhenParentDoesNotMatch() {
        entity.setClaimId(UUID.fromString("323e4567-e89b-12d3-a456-426614174000"));
        when(repository.findById(claimRecoveryId)).thenReturn(Mono.just(entity));

        StepVerifier.create(service.getClaimRecoveryById(claimId, claimRecoveryId))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }
}
