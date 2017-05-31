package de.smartsquare.ddd.sonarqube.rules;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.server.debt.DebtRemediationFunction;
import org.sonar.api.server.rule.RulesDefinition;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;

/**
 * Enrich Rules with information from JSON files.
 */
class RuleEnricher {

    private static final String FILE_LOCATION = "/de/smartsquare/l10n/rules/%s/%s_%s%s";

    private final RulesDefinition.NewRepository repository;
    private final String languageKey;
    private final Gson gson = new Gson();

    RuleEnricher(RulesDefinition.NewRepository repository, String languageKey) {
        this.repository = repository;
        this.languageKey = languageKey;
    }

    void enrich() {
        repository.rules().forEach(rule -> {
            String metadataKey = rule.key();
            addHtmlDescription(rule, metadataKey);
            addMetadata(rule, metadataKey);
        });
    }

    private void addHtmlDescription(RulesDefinition.NewRule rule, String metadataKey) {
        String description = readRuleDefinitionResource(metadataKey, ".html");
        if (description != null && !description.isEmpty()) {
            rule.setHtmlDescription(description);
        }
    }

    @Nullable
    private String readRuleDefinitionResource(String ruleKey, String fileEnding) {
        String format = String.format(
                FILE_LOCATION, repository.key(), ruleKey, languageKey, fileEnding);
        URL resource = RuleEnricher.class.getResource(format);
        if (resource == null) {
            return null;
        }
        try {
            return Resources.toString(resource, Charsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read: " + resource, e);
        }
    }

    private void addMetadata(RulesDefinition.NewRule rule, String metadataKey) {
        String json = readRuleDefinitionResource(metadataKey, ".json");
        if (json != null) {
            RuleMetadata metadata = gson.fromJson(json, RuleMetadata.class);
            rule.setSeverity(metadata.defaultSeverity.toUpperCase(Locale.US));
            rule.setName(metadata.title);
            rule.setTags(metadata.tags);
            rule.setStatus(RuleStatus.valueOf(metadata.status.toUpperCase(Locale.US)));

            if (metadata.remediation != null) {
                // metadata.remediation is null for template rules
                rule.setDebtRemediationFunction(metadata.remediation.remediationFunction(rule.debtRemediationFunctions()));
                rule.setGapDescription(metadata.remediation.linearDesc);
            }
        }
    }

    private static class RuleMetadata {
        String title;
        String status;
        @Nullable
        Remediation remediation;

        String[] tags;
        String defaultSeverity;
    }

    private static class Remediation {
        String func;
        String constantCost;
        String linearDesc;
        String linearOffset;
        String linearFactor;

        private DebtRemediationFunction remediationFunction(RulesDefinition.DebtRemediationFunctions drf) {
            if (func.startsWith("Constant")) {
                return drf.constantPerIssue(constantCost.replace("mn", "min"));
            }
            if ("Linear".equals(func)) {
                return drf.linear(linearFactor.replace("mn", "min"));
            }
            return drf.linearWithOffset(linearFactor.replace("mn", "min"), linearOffset.replace("mn", "min"));
        }
    }

}
