/*
 * UserDBAccess.java Jul 12, 2012 1.0
 */
package edu.cmu.gizmo.management.dataaccess;

import java.util.ArrayList;


/**
 * The Interface UserDBAccess.
 */
public interface UserDBAccess {
	
	/**
	 * verifies the password.
	 *
	 * @param password password to be verified
	 * @return status of the password checking
	 */
	public boolean correctPassword(String password);

	/**
	 * register a new user.
	 *
	 * @param password the password
	 * @param email email address of the user [Mandatory field]
	 * @param firstName first name of the user [Optional field]
	 * @param lastName last name of the user [Optional field]
	 * @return status of the operation
	 * @pre <ul>
	 * <li>The user should not exist. No same email address.
	 * </ul>
	 * @post The user will be assigned an ID by the system
	 */
	public boolean addUser(String password, String email, String firstName,
			String lastName);

	/**
	 * Make CoBot 3 say something through its speech synthesizer.
	 *
	 * @param oldPassword current password set
	 * @param newPassword new password changed to
	 * @return status of the operation
	 * @pre <ul>
	 * <li>The user should exist with a password.
	 * </ul>
	 * @post The user has a new password
	 * @rationale This command is needed to update the password of the existing
	 * user.
	 */
	public boolean updatePassword(String oldPassword, String newPassword);

	/**
	 * Checks whether the user has admin previlege or not.
	 *
	 * @param Id the id of the user
	 * @return status of the operation
	 */
	public boolean isAdmin(String Id);

	/**
	 * Checks whether the user is active or not.
	 *
	 * @param Id the id of the user
	 * @return status of the operation
	 * @pre <ul>
	 * <li>The user should exist with a password.
	 * </ul>
	 * @post The user has a new password
	 * @rationale This command is needed to update the password of the existing
	 * user.
	 */
	public boolean isActive(String Id);

	/**
	 * Add a role to the user.
	 *
	 * @param id id of the user
	 * @param role admin or regular user
	 * @return status of the operation
	 * @pre <ul>
	 * <li>The user should exist.
	 * </ul>
	 * @post The user has a new role
	 */
	public boolean addRole(String id, String role);

	/**
	 * function used by the admin to check who are logged into the management
	 * server.
	 *
	 * @return a list of user id
	 */
	public ArrayList<Integer> activeUsers();

	/**
	 * Check the status of the user.
	 *
	 * @param id the id
	 * @return status of the user
	 * @pre <ul>
	 * <li>The user should exist with a password.
	 * </ul>
	 */
	public boolean isLocked(int id);

}
