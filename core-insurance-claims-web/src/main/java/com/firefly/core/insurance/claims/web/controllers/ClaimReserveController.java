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
import com.firefly.core.insurance.claims.core.services.ClaimReserveService;
import com.firefly.core.insurance.claims.interfaces.dtos.ClaimReserveDTO;
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
 * REST API for reserve recorded against a claim.
 */
@RestController
@RequestMapping("/api/v1/claims/{claimId}/reserves")
@RequiredArgsConstructor
@Validated
@Tag(name = "Claim Reserves", description = "Reserve recorded against a claim")
public class ClaimReserveController {

    private final ClaimReserveService claimReserveService;

    @PostMapping("/filter")
    @Operation(summary = "Filter claimReserves", description = "Retrieve a page of claimReserves matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<ClaimReserveDTO>>> filterClaimReserves(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<ClaimReserveDTO> filterRequest) {
        return claimReserveService.filterClaimReserves(claimId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create ClaimReserve", description = "Create a ClaimReserve")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "ClaimReserve created", content = @Content(schema = @Schema(implementation = ClaimReserveDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<ClaimReserveDTO>> createClaimReserve(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "ClaimReserve to create", required = true)
            @Valid @RequestBody ClaimReserveDTO dto) {
        return claimReserveService.createClaimReserve(claimId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{claimReserveId}")
    @Operation(summary = "Get ClaimReserve by id", description = "Retrieve one ClaimReserve")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ClaimReserve found", content = @Content(schema = @Schema(implementation = ClaimReserveDTO.class))),
        @ApiResponse(responseCode = "404", description = "ClaimReserve not found", content = @Content)
    })
    public Mono<ResponseEntity<ClaimReserveDTO>> getClaimReserveById(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimReserve", required = true)
            @PathVariable UUID claimReserveId) {
        return claimReserveService.getClaimReserveById(claimId, claimReserveId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{claimReserveId}")
    @Operation(summary = "Update ClaimReserve", description = "Update an existing ClaimReserve")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ClaimReserve updated", content = @Content(schema = @Schema(implementation = ClaimReserveDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "ClaimReserve not found", content = @Content)
    })
    public Mono<ResponseEntity<ClaimReserveDTO>> updateClaimReserve(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimReserve", required = true)
            @PathVariable UUID claimReserveId,
            @Parameter(description = "Updated ClaimReserve", required = true)
            @Valid @RequestBody ClaimReserveDTO dto) {
        return claimReserveService.updateClaimReserve(claimId, claimReserveId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{claimReserveId}")
    @Operation(summary = "Delete ClaimReserve", description = "Delete a ClaimReserve")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "ClaimReserve deleted"),
        @ApiResponse(responseCode = "404", description = "ClaimReserve not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteClaimReserve(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimReserve", required = true)
            @PathVariable UUID claimReserveId) {
        return claimReserveService.deleteClaimReserve(claimId, claimReserveId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
