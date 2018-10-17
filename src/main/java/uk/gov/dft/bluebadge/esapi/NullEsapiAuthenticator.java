package uk.gov.dft.bluebadge.esapi;

import org.owasp.esapi.User;
import org.owasp.esapi.errors.AuthenticationException;
import org.owasp.esapi.errors.EncryptionException;
import org.owasp.esapi.reference.AbstractAuthenticator;

import java.util.Set;

/**
 * Added mostly to keep esapi quiet when it starts up.
 */
public class NullEsapiAuthenticator extends AbstractAuthenticator {
  @Override
  public boolean verifyPassword(User user, String password) {
    return false;
  }

  @Override
  public User createUser(String accountName, String password1, String password2) throws AuthenticationException {
    return null;
  }

  @Override
  public String generateStrongPassword() {
    return null;
  }

  @Override
  public String generateStrongPassword(User user, String oldPassword) {
    return null;
  }

  @Override
  public void changePassword(User user, String currentPassword, String newPassword, String newPassword2) throws AuthenticationException {

  }

  @Override
  public User getUser(long accountId) {
    return null;
  }

  @Override
  public User getUser(String accountName) {
    return null;
  }

  @Override
  public Set getUserNames() {
    return null;
  }

  @Override
  public String hashPassword(String password, String accountName) throws EncryptionException {
    return null;
  }

  @Override
  public void removeUser(String accountName) throws AuthenticationException {

  }

  @Override
  public void verifyAccountNameStrength(String accountName) throws AuthenticationException {

  }

  @Override
  public void verifyPasswordStrength(String oldPassword, String newPassword, User user) throws AuthenticationException {

  }
}
