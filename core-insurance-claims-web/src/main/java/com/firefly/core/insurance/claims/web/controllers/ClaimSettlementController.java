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
import com.firefly.core.insurance.claims.core.services.ClaimSettlementService;
import com.firefly.core.insurance.claims.interfaces.dtos.ClaimSettlementDTO;
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
 * REST API for authorized settlement. Payment execution is a payment order, not this record..
 */
@RestController
@RequestMapping("/api/v1/claims/{claimId}/settlements")
@RequiredArgsConstructor
@Validated
@Tag(name = "Claim Settlements", description = "Authorized settlement. Payment execution is a payment order, not this record.")
public class ClaimSettlementController {

    private final ClaimSettlementService claimSettlementService;

    @PostMapping("/filter")
    @Operation(summary = "Filter claimSettlements", description = "Retrieve a page of claimSettlements matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<ClaimSettlementDTO>>> filterClaimSettlements(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<ClaimSettlementDTO> filterRequest) {
        return claimSettlementService.filterClaimSettlements(claimId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create ClaimSettlement", description = "Create a ClaimSettlement")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "ClaimSettlement created", content = @Content(schema = @Schema(implementation = ClaimSettlementDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<ClaimSettlementDTO>> createClaimSettlement(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "ClaimSettlement to create", required = true)
            @Valid @RequestBody ClaimSettlementDTO dto) {
        return claimSettlementService.createClaimSettlement(claimId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{claimSettlementId}")
    @Operation(summary = "Get ClaimSettlement by id", description = "Retrieve one ClaimSettlement")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ClaimSettlement found", content = @Content(schema = @Schema(implementation = ClaimSettlementDTO.class))),
        @ApiResponse(responseCode = "404", description = "ClaimSettlement not found", content = @Content)
    })
    public Mono<ResponseEntity<ClaimSettlementDTO>> getClaimSettlementById(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimSettlement", required = true)
            @PathVariable UUID claimSettlementId) {
        return claimSettlementService.getClaimSettlementById(claimId, claimSettlementId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{claimSettlementId}")
    @Operation(summary = "Update ClaimSettlement", description = "Update an existing ClaimSettlement")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ClaimSettlement updated", content = @Content(schema = @Schema(implementation = ClaimSettlementDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "ClaimSettlement not found", content = @Content)
    })
    public Mono<ResponseEntity<ClaimSettlementDTO>> updateClaimSettlement(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimSettlement", required = true)
            @PathVariable UUID claimSettlementId,
            @Parameter(description = "Updated ClaimSettlement", required = true)
            @Valid @RequestBody ClaimSettlementDTO dto) {
        return claimSettlementService.updateClaimSettlement(claimId, claimSettlementId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{claimSettlementId}")
    @Operation(summary = "Delete ClaimSettlement", description = "Delete a ClaimSettlement")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "ClaimSettlement deleted"),
        @ApiResponse(responseCode = "404", description = "ClaimSettlement not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteClaimSettlement(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimSettlement", required = true)
            @PathVariable UUID claimSettlementId) {
        return claimSettlementService.deleteClaimSettlement(claimId, claimSettlementId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
