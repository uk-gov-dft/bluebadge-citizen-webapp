package uk.gov.dft.bluebadge.webapp.citizen.controllers.thdialect;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import org.thymeleaf.spring5.util.DetailedError;

public final class CitizenApplicationThymeleafUtils {

  public List<DetailedError> sortDetailedErrors(
      final List<DetailedError> unordered, final List<String> order) {
    if (order != null && !order.isEmpty()) {
      List<DetailedError> ordered = Lists.newArrayList(unordered);
      Collections.sort(ordered, new DetailedErrorComparator(order));
      return ordered;
    } else {
      return unordered;
    }
  }
}
