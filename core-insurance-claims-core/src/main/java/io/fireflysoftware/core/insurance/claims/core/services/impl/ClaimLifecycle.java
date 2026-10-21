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


package io.fireflysoftware.core.insurance.claims.core.services.impl;

import io.fireflysoftware.core.insurance.claims.interfaces.enums.ClaimStatus;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Allowed moves for a claim. A notification that has not been opened can be removed.
 */
final class ClaimLifecycle {

    private static final Map<ClaimStatus, Set<ClaimStatus>> ALLOWED = Map.of(
            ClaimStatus.NOTIFIED, EnumSet.of(ClaimStatus.OPEN, ClaimStatus.DENIED, ClaimStatus.CLOSED),
            ClaimStatus.OPEN, EnumSet.of(ClaimStatus.UNDER_ASSESSMENT, ClaimStatus.DENIED, ClaimStatus.CLOSED),
            ClaimStatus.UNDER_ASSESSMENT, EnumSet.of(
                    ClaimStatus.RESERVED, ClaimStatus.OPEN, ClaimStatus.DENIED, ClaimStatus.CLOSED),
            ClaimStatus.RESERVED, EnumSet.of(
                    ClaimStatus.SETTLED, ClaimStatus.UNDER_ASSESSMENT, ClaimStatus.DENIED, ClaimStatus.CLOSED),
            ClaimStatus.SETTLED, EnumSet.of(ClaimStatus.CLOSED, ClaimStatus.REOPENED),
            ClaimStatus.CLOSED, EnumSet.of(ClaimStatus.REOPENED),
            ClaimStatus.REOPENED, EnumSet.of(ClaimStatus.OPEN, ClaimStatus.UNDER_ASSESSMENT, ClaimStatus.CLOSED),
            ClaimStatus.DENIED, EnumSet.of(ClaimStatus.REOPENED)
    );

    private ClaimLifecycle() {
    }

    static boolean canTransition(ClaimStatus from, ClaimStatus to) {
        if (from == to) {
            return true;
        }
        Set<ClaimStatus> next = ALLOWED.get(from);
        return next != null && next.contains(to);
    }
}
