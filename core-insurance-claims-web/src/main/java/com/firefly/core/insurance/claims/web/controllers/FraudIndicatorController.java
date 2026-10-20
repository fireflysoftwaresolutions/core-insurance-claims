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
import com.firefly.core.insurance.claims.core.services.FraudIndicatorService;
import com.firefly.core.insurance.claims.interfaces.dtos.FraudIndicatorDTO;
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
 * REST API for fraud indicator recorded against a claim. Investigation stays with the insurer..
 */
@RestController
@RequestMapping("/api/v1/claims/{claimId}/fraud-indicators")
@RequiredArgsConstructor
@Validated
@Tag(name = "Fraud Indicators", description = "Fraud indicator recorded against a claim. Investigation stays with the insurer.")
public class FraudIndicatorController {

    private final FraudIndicatorService fraudIndicatorService;

    @PostMapping("/filter")
    @Operation(summary = "Filter fraudIndicators", description = "Retrieve a page of fraudIndicators matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<FraudIndicatorDTO>>> filterFraudIndicators(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<FraudIndicatorDTO> filterRequest) {
        return fraudIndicatorService.filterFraudIndicators(claimId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create FraudIndicator", description = "Create a FraudIndicator")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "FraudIndicator created", content = @Content(schema = @Schema(implementation = FraudIndicatorDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<FraudIndicatorDTO>> createFraudIndicator(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "FraudIndicator to create", required = true)
            @Valid @RequestBody FraudIndicatorDTO dto) {
        return fraudIndicatorService.createFraudIndicator(claimId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{fraudIndicatorId}")
    @Operation(summary = "Get FraudIndicator by id", description = "Retrieve one FraudIndicator")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "FraudIndicator found", content = @Content(schema = @Schema(implementation = FraudIndicatorDTO.class))),
        @ApiResponse(responseCode = "404", description = "FraudIndicator not found", content = @Content)
    })
    public Mono<ResponseEntity<FraudIndicatorDTO>> getFraudIndicatorById(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the FraudIndicator", required = true)
            @PathVariable UUID fraudIndicatorId) {
        return fraudIndicatorService.getFraudIndicatorById(claimId, fraudIndicatorId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{fraudIndicatorId}")
    @Operation(summary = "Update FraudIndicator", description = "Update an existing FraudIndicator")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "FraudIndicator updated", content = @Content(schema = @Schema(implementation = FraudIndicatorDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "FraudIndicator not found", content = @Content)
    })
    public Mono<ResponseEntity<FraudIndicatorDTO>> updateFraudIndicator(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the FraudIndicator", required = true)
            @PathVariable UUID fraudIndicatorId,
            @Parameter(description = "Updated FraudIndicator", required = true)
            @Valid @RequestBody FraudIndicatorDTO dto) {
        return fraudIndicatorService.updateFraudIndicator(claimId, fraudIndicatorId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{fraudIndicatorId}")
    @Operation(summary = "Delete FraudIndicator", description = "Delete a FraudIndicator")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "FraudIndicator deleted"),
        @ApiResponse(responseCode = "404", description = "FraudIndicator not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteFraudIndicator(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the FraudIndicator", required = true)
            @PathVariable UUID fraudIndicatorId) {
        return fraudIndicatorService.deleteFraudIndicator(claimId, fraudIndicatorId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
