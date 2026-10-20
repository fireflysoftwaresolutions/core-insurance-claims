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


package com.firefly.core.insurance.claims.web.controllers;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import com.firefly.core.insurance.claims.core.services.ClaimRecoveryService;
import com.firefly.core.insurance.claims.interfaces.dtos.ClaimRecoveryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * REST API for subrogation, salvage, or contribution pursued on a claim.
 */
@RestController
@RequestMapping("/api/v1/claims/{claimId}/recoveries")
@RequiredArgsConstructor
@Validated
@Tag(name = "Claim Recoveries", description = "Subrogation, salvage, or contribution pursued on a claim")
public class ClaimRecoveryController {

    private final ClaimRecoveryService claimRecoveryService;

    @PostMapping("/filter")
    @Operation(summary = "Filter claimRecoveries", description = "Retrieve a page of claimRecoveries matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<ClaimRecoveryDTO>>> filterClaimRecoveries(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<ClaimRecoveryDTO> filterRequest) {
        return claimRecoveryService.filterClaimRecoveries(claimId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create ClaimRecovery", description = "Create a ClaimRecovery")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "ClaimRecovery created", content = @Content(schema = @Schema(implementation = ClaimRecoveryDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<ClaimRecoveryDTO>> createClaimRecovery(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "ClaimRecovery to create", required = true)
            @Valid @RequestBody ClaimRecoveryDTO dto) {
        return claimRecoveryService.createClaimRecovery(claimId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{claimRecoveryId}")
    @Operation(summary = "Get ClaimRecovery by id", description = "Retrieve one ClaimRecovery")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ClaimRecovery found", content = @Content(schema = @Schema(implementation = ClaimRecoveryDTO.class))),
        @ApiResponse(responseCode = "404", description = "ClaimRecovery not found", content = @Content)
    })
    public Mono<ResponseEntity<ClaimRecoveryDTO>> getClaimRecoveryById(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimRecovery", required = true)
            @PathVariable UUID claimRecoveryId) {
        return claimRecoveryService.getClaimRecoveryById(claimId, claimRecoveryId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{claimRecoveryId}")
    @Operation(summary = "Update ClaimRecovery", description = "Update an existing ClaimRecovery")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ClaimRecovery updated", content = @Content(schema = @Schema(implementation = ClaimRecoveryDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "ClaimRecovery not found", content = @Content)
    })
    public Mono<ResponseEntity<ClaimRecoveryDTO>> updateClaimRecovery(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimRecovery", required = true)
            @PathVariable UUID claimRecoveryId,
            @Parameter(description = "Updated ClaimRecovery", required = true)
            @Valid @RequestBody ClaimRecoveryDTO dto) {
        return claimRecoveryService.updateClaimRecovery(claimId, claimRecoveryId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{claimRecoveryId}")
    @Operation(summary = "Delete ClaimRecovery", description = "Delete a ClaimRecovery")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "ClaimRecovery deleted"),
        @ApiResponse(responseCode = "404", description = "ClaimRecovery not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteClaimRecovery(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimRecovery", required = true)
            @PathVariable UUID claimRecoveryId) {
        return claimRecoveryService.deleteClaimRecovery(claimId, claimRecoveryId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
