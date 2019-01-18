package uk.gov.dft.bluebadge.webapp.citizen.controllers.thdialect;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

public class CitizenApplicationExpressionFactory implements IExpressionObjectFactory {

  private static final String LA_EVALUATION_VARIABLE_NAME = "ca";

  private static final Set<String> ALL_EXPRESSION_OBJECT_NAMES =
      Collections.unmodifiableSet(new HashSet<>(Arrays.asList(LA_EVALUATION_VARIABLE_NAME)));

  public CitizenApplicationExpressionFactory() {
    super();
  }

  @Override
  public Set<String> getAllExpressionObjectNames() {
    return ALL_EXPRESSION_OBJECT_NAMES;
  }

  @Override
  public Object buildObject(IExpressionContext context, String expressionObjectName) {
    if (LA_EVALUATION_VARIABLE_NAME.equals(expressionObjectName)) {
      return new CitizenApplicationThymeleafUtils();
    }
    return null;
  }

  @Override
  public boolean isCacheable(String expressionObjectName) {
    return true;
  }
}
