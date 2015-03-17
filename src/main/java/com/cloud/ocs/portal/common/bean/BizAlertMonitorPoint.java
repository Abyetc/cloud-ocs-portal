package com.cloud.ocs.portal.common.bean;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BizAlertMonitorPoint entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "biz_alert_monitor_point", catalog = "cloud_ocs_portal")
public class BizAlertMonitorPoint implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String monitorObjectId;
	private Integer monitorObjectType;
	private Double messageProcessTimeThresholdValue;
	private Integer activeFlexibleResource;
	private Double resourceLoadThresholdValue;
	private String alertPrincipalId;
	private Timestamp created;

	// Constructors

	/** default constructor */
	public BizAlertMonitorPoint() {
	}

	/** minimal constructor */
	public BizAlertMonitorPoint(String monitorObjectId,
			Integer monitorObjectType, Double messageProcessTimeThresholdValue,
			Integer activeFlexibleResource, Timestamp created) {
		this.monitorObjectId = monitorObjectId;
		this.monitorObjectType = monitorObjectType;
		this.messageProcessTimeThresholdValue = messageProcessTimeThresholdValue;
		this.activeFlexibleResource = activeFlexibleResource;
		this.created = created;
	}

	/** full constructor */
	public BizAlertMonitorPoint(String monitorObjectId,
			Integer monitorObjectType, Double messageProcessTimeThresholdValue,
			Integer activeFlexibleResource, Double resourceLoadThresholdValue,
			String alertPrincipalId, Timestamp created) {
		this.monitorObjectId = monitorObjectId;
		this.monitorObjectType = monitorObjectType;
		this.messageProcessTimeThresholdValue = messageProcessTimeThresholdValue;
		this.activeFlexibleResource = activeFlexibleResource;
		this.resourceLoadThresholdValue = resourceLoadThresholdValue;
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

	@Column(name = "message_process_time_threshold_value", nullable = false, precision = 10, scale = 5)
	public Double getMessageProcessTimeThresholdValue() {
		return this.messageProcessTimeThresholdValue;
	}

	public void setMessageProcessTimeThresholdValue(
			Double messageProcessTimeThresholdValue) {
		this.messageProcessTimeThresholdValue = messageProcessTimeThresholdValue;
	}

	@Column(name = "active_flexible_resource", nullable = false)
	public Integer getActiveFlexibleResource() {
		return this.activeFlexibleResource;
	}

	public void setActiveFlexibleResource(Integer activeFlexibleResource) {
		this.activeFlexibleResource = activeFlexibleResource;
	}

	@Column(name = "resource_load_threshold_value", precision = 10, scale = 5)
	public Double getResourceLoadThresholdValue() {
		return this.resourceLoadThresholdValue;
	}

	public void setResourceLoadThresholdValue(Double resourceLoadThresholdValue) {
		this.resourceLoadThresholdValue = resourceLoadThresholdValue;
	}

	@Column(name = "alert_principal_id", length = 20)
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