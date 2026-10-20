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


package io.fireflysoftware.core.insurance.claims.web.controllers;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.insurance.claims.core.services.ClaimService;
import io.fireflysoftware.core.insurance.claims.interfaces.dtos.ClaimDTO;
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
 * REST API for claim notification and file.
 */
@RestController
@RequestMapping("/api/v1/claims")
@RequiredArgsConstructor
@Validated
@Tag(name = "Claims", description = "Claim notification and file")
public class ClaimController {

    private final ClaimService claimService;

    @PostMapping("/filter")
    @Operation(summary = "Filter claims", description = "Retrieve a page of claims matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<ClaimDTO>>> filterClaims(
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<ClaimDTO> filterRequest) {
        return claimService.filterClaims(filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create Claim", description = "Create a Claim")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Claim created", content = @Content(schema = @Schema(implementation = ClaimDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<ClaimDTO>> createClaim(
            @Parameter(description = "Claim to create", required = true)
            @Valid @RequestBody ClaimDTO dto) {
        return claimService.createClaim(dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{claimId}")
    @Operation(summary = "Get Claim by id", description = "Retrieve one Claim")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Claim found", content = @Content(schema = @Schema(implementation = ClaimDTO.class))),
        @ApiResponse(responseCode = "404", description = "Claim not found", content = @Content)
    })
    public Mono<ResponseEntity<ClaimDTO>> getClaimById(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId) {
        return claimService.getClaimById(claimId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{claimId}")
    @Operation(summary = "Update Claim", description = "Update an existing Claim")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Claim updated", content = @Content(schema = @Schema(implementation = ClaimDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "Claim not found", content = @Content)
    })
    public Mono<ResponseEntity<ClaimDTO>> updateClaim(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Updated Claim", required = true)
            @Valid @RequestBody ClaimDTO dto) {
        return claimService.updateClaim(claimId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{claimId}")
    @Operation(summary = "Delete Claim", description = "Delete a Claim")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Claim deleted"),
        @ApiResponse(responseCode = "404", description = "Claim not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteClaim(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId) {
        return claimService.deleteClaim(claimId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
