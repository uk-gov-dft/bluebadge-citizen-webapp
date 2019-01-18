package uk.gov.dft.bluebadge.webapp.citizen.controllers.thdialect;

import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang.math.NumberUtils;
import org.thymeleaf.spring5.util.DetailedError;

public class DetailedErrorComparator implements Comparator<DetailedError> {
  private List<String> order;

  public DetailedErrorComparator(List<String> order) {
    this.order = order;
  }

  @Override
  public int compare(DetailedError fe1, DetailedError fe2) {
    String field1 = fe1.getFieldName();
    String field2 = fe2.getFieldName();

    int field1Index = order.indexOf(field1);
    int field2Index = order.indexOf(field2);

    return NumberUtils.compare(field1Index, field2Index);
  }
}
