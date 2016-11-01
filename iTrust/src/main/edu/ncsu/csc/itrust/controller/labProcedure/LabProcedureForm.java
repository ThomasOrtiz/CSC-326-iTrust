package edu.ncsu.csc.itrust.controller.labProcedure;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import edu.ncsu.csc.itrust.model.labProcedure.LabProcedure;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedure.LabProcedureStatus;

@ManagedBean(name = "lab_procedure_form")
@ViewScoped
public class LabProcedureForm {
	private LabProcedureController controller;
	private LabProcedure labProcedure;

	public LabProcedure getLabProcedure() {
		return labProcedure;
	}
	
	/**
	 * @return HTTPRequest in FacesContext, null if no request is found
	 */
	public HttpServletRequest getHttpServletRequest() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		if (ctx == null) {
			return null;
		}
		return ctx.getExternalContext().getRequest() instanceof HttpServletRequest ? (HttpServletRequest) ctx.getExternalContext().getRequest() : null;
	}

	/**
	 * @return Office Visit of the selected patient in the HCP session
	 */
	public LabProcedure getSelectedLabProcedure(){
		String id = "";
		HttpServletRequest req = getHttpServletRequest();
		
		if (req == null) {
			return null;
		}
		
		id = req.getParameter("id");
		
		if (id == null) {
			return null;
		}
	
		return controller.get(id);
	}

	public LabProcedureForm() {
		this(null);
	}
	
	public LabProcedureForm(LabProcedureController ovc) {
		try {
			controller = (ovc == null) ? new LabProcedureController() : ovc;
			labProcedure = getSelectedLabProcedure();
			if (labProcedure == null) {
				labProcedure = new LabProcedure();
				// TODO: initialize office visit id and hcp id
			}
		} catch (Exception e) {
			FacesMessage throwMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Lab Procedure Controller Error",
					"Lab Procedure Controller Error");
			FacesContext.getCurrentInstance().addMessage(null, throwMsg);
		}
	}
	
	/**
	 * Called when user clicks on the submit button in officeVisitInfo.xhtml. Takes data from form
	 * and sends to OfficeVisitMySQLLoader.java for storage and validation
	 */
	public void submitReassignment() {		
		controller.edit(labProcedure);
	}
	
	public boolean isLabProcedureCreated() {
		Long labProcedureID = labProcedure.getLabProcedureID();
		return labProcedureID != null && labProcedureID > 0;
	}
	
	public boolean isReassignable(String idStr) {
		try {
			Long.parseLong(idStr);
		} catch (NumberFormatException e) {
			return false;
		}

		LabProcedure proc = controller.get(idStr);
		
		LabProcedureStatus status = proc.getStatus();
		
		return status == LabProcedureStatus.IN_TRANSIT ||
				status == LabProcedureStatus.PENDING ||
				status == LabProcedureStatus.RECEIVED;
	}
	
	public boolean isRemovable(String idStr) {
		try {
			Long.parseLong(idStr);
		} catch (NumberFormatException e) {
			return false;
		}

		LabProcedure proc = controller.get(idStr);
		
		LabProcedureStatus status = proc.getStatus();
		
		return status == LabProcedureStatus.IN_TRANSIT ||
				status == LabProcedureStatus.RECEIVED;
	}
}
