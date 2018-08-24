package uk.gov.dft.bluebadge.webapp.citizen.config;

import java.util.Set;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import nz.net.ultraq.thymeleaf.decorators.strategies.GroupingStrategy;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.dialect.SpringStandardDialect;
import org.thymeleaf.spring5.processor.SpringInputCheckboxFieldTagProcessor;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import uk.gov.dft.bluebadge.webapp.citizen.thymeleaf.processor.CustomSpringInputCheckboxFieldTagProcessor;

@Configuration
public class TemplateEngineAndResolverConfig {
  @Bean
  public SpringTemplateEngine springTemplateEngine(SpringResourceTemplateResolver templateResolver)
      throws BeansException {
    SpringTemplateEngine result = new SpringTemplateEngine();
    result.setDialect(new CustomSpringStandardDialect());
    result.setTemplateResolver(templateResolver);
    result.setEnableSpringELCompiler(true);
    result.addDialect(new LayoutDialect(new GroupingStrategy()));

    return result;
  }

  class CustomSpringStandardDialect extends SpringStandardDialect {
    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
      Set<IProcessor> processors = super.getProcessors(dialectPrefix);
      processors.removeIf(p -> p instanceof SpringInputCheckboxFieldTagProcessor);
      processors.add(new CustomSpringInputCheckboxFieldTagProcessor(dialectPrefix));
      return processors;
    }
  }
}
