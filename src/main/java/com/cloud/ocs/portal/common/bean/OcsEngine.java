package com.cloud.ocs.portal.common.bean;
// default package

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * OcsEngine entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ocs_engine", catalog = "cloud_ocs_portal")
public class OcsEngine implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String vmId;
	private Integer ocsEngineState;
	private Timestamp lastStartDate;

	// Constructors

	/** default constructor */
	public OcsEngine() {
	}

	/** minimal constructor */
	public OcsEngine(String vmId, Integer ocsEngineState) {
		this.vmId = vmId;
		this.ocsEngineState = ocsEngineState;
	}

	/** full constructor */
	public OcsEngine(String vmId, Integer ocsEngineState,
			Timestamp lastStartDate) {
		this.vmId = vmId;
		this.ocsEngineState = ocsEngineState;
		this.lastStartDate = lastStartDate;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "vm_id", nullable = false, length = 40)
	public String getVmId() {
		return this.vmId;
	}

	public void setVmId(String vmId) {
		this.vmId = vmId;
	}

	@Column(name = "ocs_engine_state", nullable = false)
	public Integer getOcsEngineState() {
		return this.ocsEngineState;
	}

	public void setOcsEngineState(Integer ocsEngineState) {
		this.ocsEngineState = ocsEngineState;
	}

	@Column(name = "last_start_date", length = 19)
	public Timestamp getLastStartDate() {
		return this.lastStartDate;
	}

	public void setLastStartDate(Timestamp lastStartDate) {
		this.lastStartDate = lastStartDate;
	}

}