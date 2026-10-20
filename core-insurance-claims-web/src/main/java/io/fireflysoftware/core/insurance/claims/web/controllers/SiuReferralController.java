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
import io.fireflysoftware.core.insurance.claims.core.services.SiuReferralService;
import io.fireflysoftware.core.insurance.claims.interfaces.dtos.SiuReferralDTO;
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
 * REST API for referral of a claim to the special investigations unit.
 */
@RestController
@RequestMapping("/api/v1/claims/{claimId}/siu-referrals")
@RequiredArgsConstructor
@Validated
@Tag(name = "SIU Referrals", description = "Referral of a claim to the special investigations unit")
public class SiuReferralController {

    private final SiuReferralService siuReferralService;

    @PostMapping("/filter")
    @Operation(summary = "Filter siuReferrals", description = "Retrieve a page of siuReferrals matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<SiuReferralDTO>>> filterSiuReferrals(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<SiuReferralDTO> filterRequest) {
        return siuReferralService.filterSiuReferrals(claimId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create SiuReferral", description = "Create a SiuReferral")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "SiuReferral created", content = @Content(schema = @Schema(implementation = SiuReferralDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<SiuReferralDTO>> createSiuReferral(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "SiuReferral to create", required = true)
            @Valid @RequestBody SiuReferralDTO dto) {
        return siuReferralService.createSiuReferral(claimId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{siuReferralId}")
    @Operation(summary = "Get SiuReferral by id", description = "Retrieve one SiuReferral")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "SiuReferral found", content = @Content(schema = @Schema(implementation = SiuReferralDTO.class))),
        @ApiResponse(responseCode = "404", description = "SiuReferral not found", content = @Content)
    })
    public Mono<ResponseEntity<SiuReferralDTO>> getSiuReferralById(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the SiuReferral", required = true)
            @PathVariable UUID siuReferralId) {
        return siuReferralService.getSiuReferralById(claimId, siuReferralId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{siuReferralId}")
    @Operation(summary = "Update SiuReferral", description = "Update an existing SiuReferral")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "SiuReferral updated", content = @Content(schema = @Schema(implementation = SiuReferralDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "SiuReferral not found", content = @Content)
    })
    public Mono<ResponseEntity<SiuReferralDTO>> updateSiuReferral(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the SiuReferral", required = true)
            @PathVariable UUID siuReferralId,
            @Parameter(description = "Updated SiuReferral", required = true)
            @Valid @RequestBody SiuReferralDTO dto) {
        return siuReferralService.updateSiuReferral(claimId, siuReferralId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{siuReferralId}")
    @Operation(summary = "Delete SiuReferral", description = "Delete a SiuReferral")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "SiuReferral deleted"),
        @ApiResponse(responseCode = "404", description = "SiuReferral not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteSiuReferral(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the SiuReferral", required = true)
            @PathVariable UUID siuReferralId) {
        return siuReferralService.deleteSiuReferral(claimId, siuReferralId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
