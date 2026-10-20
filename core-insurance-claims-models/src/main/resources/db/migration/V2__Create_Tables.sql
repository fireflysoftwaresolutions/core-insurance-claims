-- Flyway V2: tables for core-insurance-claims

-- Claim notification and file
CREATE TABLE claim (
    claim_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    claim_number VARCHAR(64) NOT NULL UNIQUE,
    policy_id UUID NOT NULL,
    contract_id UUID NULL,
    policy_transaction_id UUID NULL,
    claim_status claim_status_enum NOT NULL,
    loss_date DATE NOT NULL,
    notified_at TIMESTAMP WITH TIME ZONE NOT NULL,
    cause_code VARCHAR(64) NULL,
    description TEXT NULL,
    currency VARCHAR(3) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Party role on a claim. The party itself is owned by customer management.
CREATE TABLE claim_party (
    claim_party_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    claim_id UUID NOT NULL REFERENCES claim (claim_id) ON DELETE CASCADE,
    party_id UUID NOT NULL,
    role claim_party_role_enum NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Coverage on the policy against which the claim is made
CREATE TABLE claim_coverage (
    claim_coverage_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    claim_id UUID NOT NULL REFERENCES claim (claim_id) ON DELETE CASCADE,
    coverage_id UUID NOT NULL,
    claimed_amount DECIMAL(19, 4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Reserve recorded against a claim
CREATE TABLE claim_reserve (
    claim_reserve_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    claim_id UUID NOT NULL REFERENCES claim (claim_id) ON DELETE CASCADE,
    reserve_type reserve_type_enum NOT NULL,
    amount DECIMAL(19, 4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    effective_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Assessment recorded against a claim
CREATE TABLE claim_assessment (
    claim_assessment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    claim_id UUID NOT NULL REFERENCES claim (claim_id) ON DELETE CASCADE,
    assessment_outcome assessment_outcome_enum NOT NULL,
    assessed_amount DECIMAL(19, 4) NULL,
    notes TEXT NULL,
    assessed_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Evidence reference on a claim. The document bytes stay in document management.
CREATE TABLE claim_evidence (
    claim_evidence_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    claim_id UUID NOT NULL REFERENCES claim (claim_id) ON DELETE CASCADE,
    document_id UUID NOT NULL,
    evidence_type claim_evidence_type_enum NOT NULL,
    received_at TIMESTAMP WITH TIME ZONE NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Authorized settlement. Payment execution is a payment order, not this record.
CREATE TABLE claim_settlement (
    claim_settlement_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    claim_id UUID NOT NULL REFERENCES claim (claim_id) ON DELETE CASCADE,
    payee_party_id UUID NOT NULL,
    settlement_status settlement_status_enum NOT NULL,
    authorized_amount DECIMAL(19, 4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    payment_order_id UUID NULL,
    authorized_at TIMESTAMP WITH TIME ZONE NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Status change recorded against a claim
CREATE TABLE claim_status_history (
    claim_status_history_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    claim_id UUID NOT NULL REFERENCES claim (claim_id) ON DELETE CASCADE,
    claim_status claim_status_enum NOT NULL,
    status_reason VARCHAR(500) NULL,
    changed_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Record of the coverage facts checked for a claim: in force, trigger, and deductible
CREATE TABLE coverage_check (
    coverage_check_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    claim_id UUID NOT NULL REFERENCES claim (claim_id) ON DELETE CASCADE,
    policy_id UUID NOT NULL,
    coverage_id UUID NULL,
    in_force BOOLEAN NULL,
    trigger_met BOOLEAN NULL,
    deductible_amount DECIMAL(19, 4) NULL,
    check_outcome coverage_check_outcome_enum NOT NULL,
    checked_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Routing of a claim to fast-track, complex handling, or SIU
CREATE TABLE claim_triage (
    claim_triage_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    claim_id UUID NOT NULL REFERENCES claim (claim_id) ON DELETE CASCADE,
    triage_path triage_path_enum NOT NULL,
    triaged_at TIMESTAMP WITH TIME ZONE NOT NULL,
    notes TEXT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Adjuster assigned to a claim. The adjuster is a party.
CREATE TABLE adjuster_assignment (
    adjuster_assignment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    claim_id UUID NOT NULL REFERENCES claim (claim_id) ON DELETE CASCADE,
    adjuster_party_id UUID NOT NULL,
    assigned_at TIMESTAMP WITH TIME ZONE NOT NULL,
    assignment_status assignment_status_enum NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Fraud indicator recorded against a claim. Investigation stays with the insurer.
CREATE TABLE fraud_indicator (
    fraud_indicator_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    claim_id UUID NOT NULL REFERENCES claim (claim_id) ON DELETE CASCADE,
    indicator_code VARCHAR(64) NOT NULL,
    severity indicator_severity_enum NOT NULL,
    noted_at TIMESTAMP WITH TIME ZONE NOT NULL,
    notes TEXT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Referral of a claim to the special investigations unit
CREATE TABLE siu_referral (
    siu_referral_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    claim_id UUID NOT NULL REFERENCES claim (claim_id) ON DELETE CASCADE,
    referral_status siu_referral_status_enum NOT NULL,
    referred_at TIMESTAMP WITH TIME ZONE NOT NULL,
    reason TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Subrogation, salvage, or contribution pursued on a claim
CREATE TABLE claim_recovery (
    claim_recovery_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    claim_id UUID NOT NULL REFERENCES claim (claim_id) ON DELETE CASCADE,
    recovery_type recovery_type_enum NOT NULL,
    recovery_status recovery_status_enum NOT NULL,
    expected_amount DECIMAL(19, 4) NULL,
    recovered_amount DECIMAL(19, 4) NULL,
    currency VARCHAR(3) NOT NULL,
    counterparty_party_id UUID NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Denial recorded by the insurer. This service does not decide coverage.
CREATE TABLE claim_denial (
    claim_denial_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    claim_id UUID NOT NULL REFERENCES claim (claim_id) ON DELETE CASCADE,
    denial_reason TEXT NOT NULL,
    denied_at TIMESTAMP WITH TIME ZONE NOT NULL,
    authority_reference VARCHAR(200) NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_claim_tenant_id ON claim (tenant_id);
CREATE INDEX idx_claim_policy_id ON claim (policy_id);
CREATE INDEX idx_claim_contract_id ON claim (contract_id);
CREATE INDEX idx_claim_policy_transaction_id ON claim (policy_transaction_id);
CREATE INDEX idx_claim_party_claim_id ON claim_party (claim_id);
CREATE INDEX idx_claim_party_party_id ON claim_party (party_id);
CREATE INDEX idx_claim_coverage_claim_id ON claim_coverage (claim_id);
CREATE INDEX idx_claim_coverage_coverage_id ON claim_coverage (coverage_id);
CREATE INDEX idx_claim_reserve_claim_id ON claim_reserve (claim_id);
CREATE INDEX idx_claim_assessment_claim_id ON claim_assessment (claim_id);
CREATE INDEX idx_claim_evidence_claim_id ON claim_evidence (claim_id);
CREATE INDEX idx_claim_evidence_document_id ON claim_evidence (document_id);
CREATE INDEX idx_claim_settlement_claim_id ON claim_settlement (claim_id);
CREATE INDEX idx_claim_settlement_payee_party_id ON claim_settlement (payee_party_id);
CREATE INDEX idx_claim_settlement_payment_order_id ON claim_settlement (payment_order_id);
CREATE INDEX idx_claim_status_history_claim_id ON claim_status_history (claim_id);
CREATE INDEX idx_coverage_check_claim_id ON coverage_check (claim_id);
CREATE INDEX idx_coverage_check_policy_id ON coverage_check (policy_id);
CREATE INDEX idx_coverage_check_coverage_id ON coverage_check (coverage_id);
CREATE INDEX idx_claim_triage_claim_id ON claim_triage (claim_id);
CREATE INDEX idx_adjuster_assignment_claim_id ON adjuster_assignment (claim_id);
CREATE INDEX idx_adjuster_assignment_adjuster_party_id ON adjuster_assignment (adjuster_party_id);
CREATE INDEX idx_fraud_indicator_claim_id ON fraud_indicator (claim_id);
CREATE INDEX idx_siu_referral_claim_id ON siu_referral (claim_id);
CREATE INDEX idx_claim_recovery_claim_id ON claim_recovery (claim_id);
CREATE INDEX idx_claim_recovery_counterparty_party_id ON claim_recovery (counterparty_party_id);
CREATE INDEX idx_claim_denial_claim_id ON claim_denial (claim_id);
COMMENT ON COLUMN claim.tenant_id IS 'Logical reference: Firefly deployment tenant. No foreign key across services.';
COMMENT ON COLUMN claim.policy_id IS 'Logical reference: core-insurance-policies policy. No foreign key across services.';
COMMENT ON COLUMN claim.contract_id IS 'Logical reference: core-common-contract-mgmt contract. No foreign key across services.';
COMMENT ON COLUMN claim.policy_transaction_id IS 'Logical reference: core-insurance-policies policy transaction. No foreign key across services.';
COMMENT ON COLUMN claim_party.party_id IS 'Logical reference: core-common-customer-mgmt party. No foreign key across services.';
COMMENT ON COLUMN claim_coverage.coverage_id IS 'Logical reference: core-insurance-policies coverage. No foreign key across services.';
COMMENT ON COLUMN claim_evidence.document_id IS 'Logical reference: core-common-document-mgmt document. No foreign key across services.';
COMMENT ON COLUMN claim_settlement.payee_party_id IS 'Logical reference: core-common-customer-mgmt party. No foreign key across services.';
COMMENT ON COLUMN claim_settlement.payment_order_id IS 'Logical reference: core-banking-payments payment order. No foreign key across services.';
COMMENT ON COLUMN coverage_check.policy_id IS 'Logical reference: core-insurance-policies policy. No foreign key across services.';
COMMENT ON COLUMN coverage_check.coverage_id IS 'Logical reference: core-insurance-policies coverage. No foreign key across services.';
COMMENT ON COLUMN adjuster_assignment.adjuster_party_id IS 'Logical reference: core-common-customer-mgmt party. No foreign key across services.';
COMMENT ON COLUMN claim_recovery.counterparty_party_id IS 'Logical reference: core-common-customer-mgmt party. No foreign key across services.';
