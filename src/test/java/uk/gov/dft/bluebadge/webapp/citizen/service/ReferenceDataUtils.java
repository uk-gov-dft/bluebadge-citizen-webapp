package uk.gov.dft.bluebadge.webapp.citizen.service;

import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.ReferenceData;

public class ReferenceDataUtils {
  public static final ReferenceData buildReferenceData(String groupShortCode, int i) {
    return new ReferenceData()
        .description("description" + 1)
        .displayOrder(i)
        .groupDescription("groupDescription" + i)
        .groupShortCode(groupShortCode)
        .shortCode("shortCode" + i)
        .subgroupDescription("subGroupDescription" + i)
        .subgroupShortCode("subGroupShortCode" + i);
  }
}
