package helpers;

import org.mindrot.jbcrypt.BCrypt;

public class HashHelper {

	/**
	* 
	* Create an encrypted password from a clear string.
	* 
	* @param clearString
	*            the clear string
	* @return an encrypted password of the clear string
	* @throws AppException
	*             APP Exception, from NoSuchAlgorithmException
	*/
	public static String createPassword(String clearString)
			throws IllegalArgumentException {

		if (clearString == null) {
			throw new IllegalArgumentException("empty.password");
		}
		return BCrypt.hashpw(clearString, BCrypt.gensalt());
	}

	/**
	* 
	* Method to check if entered user password is the same as the one that is
	* stored (encrypted) in the database.
	* 
	* @param candidate
	*            the clear text
	* @param encryptedPassword
	*            the encrypted password string to check.
	* @return true if the candidate matches, false otherwise.
	*/
	public static boolean checkPassword(String candidate,String encryptedPassword) {
		if (candidate == null) {
			return false;
		}

		if (encryptedPassword == null) {
			return false;
		}
		return BCrypt.checkpw(candidate, encryptedPassword);

	}
}