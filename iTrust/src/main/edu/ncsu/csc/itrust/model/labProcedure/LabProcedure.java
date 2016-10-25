package edu.ncsu.csc.itrust.model.labProcedure;

import java.sql.Timestamp;
import java.util.Arrays;

public class LabProcedure {

	private Long labProcedureID;
	private Long labTechnicianID;
	private Long officeVisitID;
	private Integer priority;
	private boolean isRestricted;
	private LabProcedureStatus status;
	private String commentary;
	private String results;
	private Timestamp updatedDate;

	/** Enum for String representation of lab procedure status */
	public enum LabProcedureStatus {
		PENDING(1L, "Pending"), IN_TRANSIT(2L, "In transit"), RECEIVED(3L, "Received"), TESTING(4L,
				"Testing"), COMPLETED(5L, "Completed");

		private String name;
		private long id;

		LabProcedureStatus(long id, String name) {
			this.id = id;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public long getID() {
			return id;
		}

		/**
		 * Returns status enum with given ID.
		 * 
		 * @param id
		 *            The ID of the status
		 * @return Status with the given ID
		 */
		private static LabProcedureStatus getStatusByID(long id) {
			return Arrays.asList(values()).stream().filter(p -> p.getID() == id).findFirst().get();
		}

		/**
		 * Returns status enum with given name.
		 * 
		 * @param name
		 *            The name of the status to return
		 * @return Status with the given name
		 */
		private static LabProcedureStatus getStatusByName(String name) {
			return Arrays.asList(values()).stream().filter(p -> p.getName().equals(name)).findFirst().get();
		}
	}

	public LabProcedure() {
	}

	public Long getLabProcedureID() {
		return labProcedureID;
	}

	public void setLabProcedureID(Long labProcedureID) {
		this.labProcedureID = labProcedureID;
	}

	public Long getLabTechnicianID() {
		return labTechnicianID;
	}

	public void setLabTechnicianID(Long labTechnicianID) {
		this.labTechnicianID = labTechnicianID;
	}

	public Long getOfficeVisitID() {
		return officeVisitID;
	}

	public void setOfficeVisitID(Long officeVisitID) {
		this.officeVisitID = officeVisitID;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public boolean isRestricted() {
		return isRestricted;
	}

	public void setIsRestricted(boolean isRestricted) {
		this.isRestricted = isRestricted;
	}

	public LabProcedureStatus getStatus() {
		return status;
	}

	public void setStatus(long id) {
		this.status = LabProcedureStatus.getStatusByID(id);
	}

	public void setStatus(String name) {
		this.status = LabProcedureStatus.getStatusByName(name);
	}

	public String getCommentary() {
		return commentary;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

}
