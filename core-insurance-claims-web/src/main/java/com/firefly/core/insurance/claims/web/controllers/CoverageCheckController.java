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
import com.firefly.core.insurance.claims.core.services.CoverageCheckService;
import com.firefly.core.insurance.claims.interfaces.dtos.CoverageCheckDTO;
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
 * REST API for record of the coverage facts checked for a claim: in force, trigger, and deductible.
 */
@RestController
@RequestMapping("/api/v1/claims/{claimId}/coverage-checks")
@RequiredArgsConstructor
@Validated
@Tag(name = "Coverage Checks", description = "Record of the coverage facts checked for a claim: in force, trigger, and deductible")
public class CoverageCheckController {

    private final CoverageCheckService coverageCheckService;

    @PostMapping("/filter")
    @Operation(summary = "Filter coverageChecks", description = "Retrieve a page of coverageChecks matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<CoverageCheckDTO>>> filterCoverageChecks(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<CoverageCheckDTO> filterRequest) {
        return coverageCheckService.filterCoverageChecks(claimId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create CoverageCheck", description = "Create a CoverageCheck")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "CoverageCheck created", content = @Content(schema = @Schema(implementation = CoverageCheckDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<CoverageCheckDTO>> createCoverageCheck(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "CoverageCheck to create", required = true)
            @Valid @RequestBody CoverageCheckDTO dto) {
        return coverageCheckService.createCoverageCheck(claimId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{coverageCheckId}")
    @Operation(summary = "Get CoverageCheck by id", description = "Retrieve one CoverageCheck")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "CoverageCheck found", content = @Content(schema = @Schema(implementation = CoverageCheckDTO.class))),
        @ApiResponse(responseCode = "404", description = "CoverageCheck not found", content = @Content)
    })
    public Mono<ResponseEntity<CoverageCheckDTO>> getCoverageCheckById(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the CoverageCheck", required = true)
            @PathVariable UUID coverageCheckId) {
        return coverageCheckService.getCoverageCheckById(claimId, coverageCheckId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{coverageCheckId}")
    @Operation(summary = "Update CoverageCheck", description = "Update an existing CoverageCheck")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "CoverageCheck updated", content = @Content(schema = @Schema(implementation = CoverageCheckDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "CoverageCheck not found", content = @Content)
    })
    public Mono<ResponseEntity<CoverageCheckDTO>> updateCoverageCheck(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the CoverageCheck", required = true)
            @PathVariable UUID coverageCheckId,
            @Parameter(description = "Updated CoverageCheck", required = true)
            @Valid @RequestBody CoverageCheckDTO dto) {
        return coverageCheckService.updateCoverageCheck(claimId, coverageCheckId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{coverageCheckId}")
    @Operation(summary = "Delete CoverageCheck", description = "Delete a CoverageCheck")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "CoverageCheck deleted"),
        @ApiResponse(responseCode = "404", description = "CoverageCheck not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteCoverageCheck(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the CoverageCheck", required = true)
            @PathVariable UUID coverageCheckId) {
        return coverageCheckService.deleteCoverageCheck(claimId, coverageCheckId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
