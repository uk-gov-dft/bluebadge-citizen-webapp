package uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn;

import lombok.Data;

import java.io.Serializable;

@Data
public class EnterCodeForm implements Serializable {
  String postcode;
  String code;
}
