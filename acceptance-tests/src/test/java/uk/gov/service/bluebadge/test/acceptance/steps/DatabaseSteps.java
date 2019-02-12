package uk.gov.service.bluebadge.test.acceptance.steps;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import uk.gov.service.bluebadge.test.acceptance.util.DbUtils;

public class DatabaseSteps {

  private final DbUtils dbUtils;

  public DatabaseSteps() {

    String dbHost = System.getProperty("dbHost");
    if (StringUtils.isBlank(dbHost)) {
      dbHost = "localhost";
    }
    Map<String, Object> settings = new HashMap<>();
    settings.put("username", "developer");
    settings.put(" ***REMOVED***);
    settings.put(
        "url", "jdbc:postgresql://" + dbHost + ":5432/bb_dev?currentSchema=applicationmanagement");
    settings.put("driverClassName", "org.postgresql.Driver");

    dbUtils = new DbUtils(settings);
  }

  @Before("@CreateLocalCouncilAndAuthorityWithPaymentsEnable")
  public void executeInsertApplicationsDBScript() throws SQLException {
    dbUtils.runScript("scripts/create_local_council_and_authority_with_payments_enable.sql");
  }

  @After("@CreateLocalCouncilAndAuthorityWithPaymentsEnable")
  public void executeDeleteApplicationsDBScript() throws SQLException {
    dbUtils.runScript("scripts/delete_local_council_and_authority_with_payments_enable.sql");
  }

  void runScript(String scriptPath) throws SQLException {
    dbUtils.runScript(scriptPath);
  }

  String queryForString(String query) {
    return dbUtils.readValue(query, String.class);
  }
}
