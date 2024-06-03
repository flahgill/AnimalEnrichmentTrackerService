package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentActivityModel;

/**
 * ViewEnrichmentActivityResult object class.
 */
public class ViewEnrichmentActivityResult {
    private final EnrichmentActivityModel enrichmentActivity;

    /**
     * creates ViewEnrichmentActivityResult object containing the retrieved EnrichmentActivity.
     *
     * @param enrichmentActivity the EnrichmentActivityModel as a result of retrieving from DDB
     */
    private ViewEnrichmentActivityResult(EnrichmentActivityModel enrichmentActivity) {
        this.enrichmentActivity = enrichmentActivity;
    }

    public EnrichmentActivityModel getEnrichmentActivity() {
        return enrichmentActivity;
    }

    @Override
    public String toString() {
        return "ViewEnrichmentActivityResult{" +
                "enrichmentActivity=" + enrichmentActivity +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private EnrichmentActivityModel enrichmentActivity;

        public Builder withEnrichmentActivity(EnrichmentActivityModel enrichmentActivity) {
            this.enrichmentActivity = enrichmentActivity;
            return this;
        }

        public ViewEnrichmentActivityResult build() {
            return new ViewEnrichmentActivityResult(enrichmentActivity);
        }
    }
}
