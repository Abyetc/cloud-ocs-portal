package com.cloud.ocs.portal.common.bean;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * OcsHost entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ocs_host", catalog = "cloud_ocs_portal")
public class OcsHost implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String zoneId;
	private String podId;
	private String clusterId;
	private String hostId;
	private String hostName;
	private String ipAddress;
	private Integer state;
	private Timestamp created;

	// Constructors

	/** default constructor */
	public OcsHost() {
	}

	/** minimal constructor */
	public OcsHost(String zoneId, String podId, String clusterId,
			String hostId, String hostName, String ipAddress, Integer state) {
		this.zoneId = zoneId;
		this.podId = podId;
		this.clusterId = clusterId;
		this.hostId = hostId;
		this.hostName = hostName;
		this.ipAddress = ipAddress;
		this.state = state;
	}

	/** full constructor */
	public OcsHost(String zoneId, String podId, String clusterId,
			String hostId, String hostName, String ipAddress, Integer state,
			Timestamp created) {
		this.zoneId = zoneId;
		this.podId = podId;
		this.clusterId = clusterId;
		this.hostId = hostId;
		this.hostName = hostName;
		this.ipAddress = ipAddress;
		this.state = state;
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

	@Column(name = "zone_id", nullable = false, length = 40)
	public String getZoneId() {
		return this.zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	@Column(name = "pod_id", nullable = false, length = 40)
	public String getPodId() {
		return this.podId;
	}

	public void setPodId(String podId) {
		this.podId = podId;
	}

	@Column(name = "cluster_id", nullable = false, length = 40)
	public String getClusterId() {
		return this.clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	@Column(name = "host_id", nullable = false, length = 40)
	public String getHostId() {
		return this.hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	@Column(name = "host_name", nullable = false, length = 100)
	public String getHostName() {
		return this.hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	@Column(name = "ip_address", nullable = false, length = 40)
	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Column(name = "state", nullable = false)
	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Column(name = "created", length = 19)
	public Timestamp getCreated() {
		return this.created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

}