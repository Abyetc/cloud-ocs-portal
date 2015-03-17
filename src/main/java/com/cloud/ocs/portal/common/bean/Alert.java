package com.cloud.ocs.portal.common.bean;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Alert entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "alert", catalog = "cloud_ocs_portal")
public class Alert implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer alertMonitorPointId;
	private Integer source;
	private Integer firstLevelType;
	private Integer secondLevelType;
	private String description;
	private Timestamp created;

	// Constructors

	/** default constructor */
	public Alert() {
	}

	/** minimal constructor */
	public Alert(Integer alertMonitorPointId, Integer source,
			Integer firstLevelType, Integer secondLevelType, Timestamp created) {
		this.alertMonitorPointId = alertMonitorPointId;
		this.source = source;
		this.firstLevelType = firstLevelType;
		this.secondLevelType = secondLevelType;
		this.created = created;
	}

	/** full constructor */
	public Alert(Integer alertMonitorPointId, Integer source,
			Integer firstLevelType, Integer secondLevelType,
			String description, Timestamp created) {
		this.alertMonitorPointId = alertMonitorPointId;
		this.source = source;
		this.firstLevelType = firstLevelType;
		this.secondLevelType = secondLevelType;
		this.description = description;
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

	@Column(name = "alert_monitor_point_id", nullable = false)
	public Integer getAlertMonitorPointId() {
		return this.alertMonitorPointId;
	}

	public void setAlertMonitorPointId(Integer alertMonitorPointId) {
		this.alertMonitorPointId = alertMonitorPointId;
	}

	@Column(name = "source", nullable = false)
	public Integer getSource() {
		return this.source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	@Column(name = "first_level_type", nullable = false)
	public Integer getFirstLevelType() {
		return this.firstLevelType;
	}

	public void setFirstLevelType(Integer firstLevelType) {
		this.firstLevelType = firstLevelType;
	}

	@Column(name = "second_level_type", nullable = false)
	public Integer getSecondLevelType() {
		return this.secondLevelType;
	}

	public void setSecondLevelType(Integer secondLevelType) {
		this.secondLevelType = secondLevelType;
	}

	@Column(name = "description", length = 500)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "created", nullable = false, length = 19)
	public Timestamp getCreated() {
		return this.created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

}