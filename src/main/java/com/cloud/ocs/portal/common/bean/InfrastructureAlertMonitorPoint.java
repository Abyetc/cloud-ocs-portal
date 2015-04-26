package com.cloud.ocs.portal.common.bean;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
* InfrastructureAlertMonitorPoint entity. @author MyEclipse Persistence Tools
*/
@Entity
@Table(name = "infrastructure_alert_monitor_point", catalog = "cloud_ocs_portal")
public class InfrastructureAlertMonitorPoint implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String monitorObjectId;
	private Integer monitorObjectType;
	private Double loadThresholdValue;
	private String alertPrincipalId;
	private Timestamp created;

	// Constructors

	/** default constructor */
	public InfrastructureAlertMonitorPoint() {
	}

	/** full constructor */
	public InfrastructureAlertMonitorPoint(String monitorObjectId,
			Integer monitorObjectType, Double loadThresholdValue,
			String alertPrincipalId, Timestamp created) {
		this.monitorObjectId = monitorObjectId;
		this.monitorObjectType = monitorObjectType;
		this.loadThresholdValue = loadThresholdValue;
		this.alertPrincipalId = alertPrincipalId;
		this.created = created;
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

	@Column(name = "monitor_object_id", nullable = false, length = 40)
	public String getMonitorObjectId() {
		return this.monitorObjectId;
	}

	public void setMonitorObjectId(String monitorObjectId) {
		this.monitorObjectId = monitorObjectId;
	}

	@Column(name = "monitor_object_type", nullable = false)
	public Integer getMonitorObjectType() {
		return this.monitorObjectType;
	}

	public void setMonitorObjectType(Integer monitorObjectType) {
		this.monitorObjectType = monitorObjectType;
	}

	@Column(name = "load_threshold_value", nullable = false, precision = 10, scale = 5)
	public Double getLoadThresholdValue() {
		return this.loadThresholdValue;
	}

	public void setLoadThresholdValue(Double loadThresholdValue) {
		this.loadThresholdValue = loadThresholdValue;
	}

	@Column(name = "alert_principal_id", nullable = false, length = 20)
	public String getAlertPrincipalId() {
		return this.alertPrincipalId;
	}

	public void setAlertPrincipalId(String alertPrincipalId) {
		this.alertPrincipalId = alertPrincipalId;
	}

	@Column(name = "created", nullable = false, length = 19)
	public Timestamp getCreated() {
		return this.created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

}
