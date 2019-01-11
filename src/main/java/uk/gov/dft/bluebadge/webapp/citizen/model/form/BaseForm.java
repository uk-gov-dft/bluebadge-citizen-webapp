package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import com.google.common.collect.Lists;
import java.util.List;

/** Base class of all Form Request */
public interface BaseForm {

  /**
   * @return the list of field names in the order we should display them to the user in the error
   *     summary section. It can return empty list in case it is not applicable (forms with 1 field)
   *     or it does not matter.
   */
  default List<String> getFieldOrder() {
    return Lists.newArrayList();
  };
}
