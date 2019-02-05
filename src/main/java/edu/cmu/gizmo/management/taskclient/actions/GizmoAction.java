package edu.cmu.gizmo.management.taskclient.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author majed alzayer
 *
 * This class is  an abstract Struts Action class that provides the Struts
 * Actions that extends it an {@link HttpServletRequest} and
 * an {@link HttpServletResponse} objects. The rationale behind
 * creating this class is to reduce the effort needed to create
 * a Struts Action class.
 */
public abstract class GizmoAction extends ActionSupport 
	implements ServletRequestAware,ServletResponseAware {
	

	/**
	 * This is the HttpServletRequest
	 */
	private HttpServletRequest servletRequest;
	
	
	/**
	 * This is the HttpServletResponse
	 */
	private HttpServletResponse servletResponse;
	
	
	/**
	 * 
	 * @return the servletRequest instance member
	 */
	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.struts2.interceptor.ServletRequestAware#
	 * setServletRequest(javax.servlet.http.HttpServletRequest)
	 */
	public void setServletRequest(HttpServletRequest request) {
		this.servletRequest = request;
	}
	
	
	/**
	 * @return servletResponse instance member
	 */
	public HttpServletResponse getServletResopnse() {
		return servletResponse;
	}
	
	
	/* (non-Javadoc)
	 * @see org.apache.struts2.interceptor.ServletResponseAware#
	 * setServletResponse(javax.servlet.http.HttpServletResponse)
	 * 
	 */
	public void setServletResponse(HttpServletResponse response) {
		this.servletResponse = response;
	}
	
	
	

}
