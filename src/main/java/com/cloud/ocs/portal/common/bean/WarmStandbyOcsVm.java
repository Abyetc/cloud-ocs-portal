package com.cloud.ocs.portal.common.bean;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * WarmStandbyOcsVm entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "warm_standby_ocs_vm", catalog = "cloud_ocs_portal")
public class WarmStandbyOcsVm implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String vmId;
	private String networkId;
	private String publicIpId;
	private String publicIp;
	private String privateIp;
	private Integer state;
	private String hostId;
	private String hostName;
	private Timestamp created;

	// Constructors

	/** default constructor */
	public WarmStandbyOcsVm() {
	}

	/** minimal constructor */
	public WarmStandbyOcsVm(String vmId, String networkId, String publicIpId,
			String publicIp, String privateIp, Integer state) {
		this.vmId = vmId;
		this.networkId = networkId;
		this.publicIpId = publicIpId;
		this.publicIp = publicIp;
		this.privateIp = privateIp;
		this.state = state;
	}

	/** full constructor */
	public WarmStandbyOcsVm(String vmId, String networkId, String publicIpId,
			String publicIp, String privateIp, Integer state, String hostId,
			String hostName, Timestamp created) {
		this.vmId = vmId;
		this.networkId = networkId;
		this.publicIpId = publicIpId;
		this.publicIp = publicIp;
		this.privateIp = privateIp;
		this.state = state;
		this.hostId = hostId;
		this.hostName = hostName;
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

	@Column(name = "vm_id", nullable = false, length = 40)
	public String getVmId() {
		return this.vmId;
	}

	public void setVmId(String vmId) {
		this.vmId = vmId;
	}

	@Column(name = "network_id", nullable = false, length = 40)
	public String getNetworkId() {
		return this.networkId;
	}

	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}

	@Column(name = "public_ip_id", nullable = false, length = 40)
	public String getPublicIpId() {
		return this.publicIpId;
	}

	public void setPublicIpId(String publicIpId) {
		this.publicIpId = publicIpId;
	}

	@Column(name = "public_ip", nullable = false, length = 40)
	public String getPublicIp() {
		return this.publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	@Column(name = "private_ip", nullable = false, length = 40)
	public String getPrivateIp() {
		return this.privateIp;
	}

	public void setPrivateIp(String privateIp) {
		this.privateIp = privateIp;
	}

	@Column(name = "state", nullable = false)
	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Column(name = "host_id", length = 40)
	public String getHostId() {
		return this.hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	@Column(name = "host_name", length = 100)
	public String getHostName() {
		return this.hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	@Column(name = "created", length = 19)
	public Timestamp getCreated() {
		return this.created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

}