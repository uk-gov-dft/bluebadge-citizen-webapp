package uk.gov.dft.bluebadge.webapp.citizen.controllers.thdialect;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

public class CitizenApplicationDialect extends AbstractDialect implements IExpressionObjectDialect {

  private final IExpressionObjectFactory localAuthorityExpressionFactory =
      new CitizenApplicationExpressionFactory();

  public CitizenApplicationDialect() {
    super("ca");
  }

  @Override
  public IExpressionObjectFactory getExpressionObjectFactory() {
    return localAuthorityExpressionFactory;
  }
}
