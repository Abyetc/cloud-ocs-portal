package com.cloud.ocs.portal.core.business.bean;
// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * OcsVmForwardingPort entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ocs_vm_forwarding_port", catalog = "cloud_ocs_portal")
public class OcsVmForwardingPort implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String vmId;
	private String networkId;
	private String publicIpId;
	private String publicIp;
	private Integer monitorPublicPort;
	private Integer monitorPrivatePort;
	private Integer sshPublicPort;
	private Integer sshPrivatePort;

	// Constructors

	/** default constructor */
	public OcsVmForwardingPort() {
	}

	/** full constructor */
	public OcsVmForwardingPort(String vmId, String networkId,
			String publicIpId, String publicIp, Integer monitorPublicPort,
			Integer monitorPrivatePort, Integer sshPublicPort,
			Integer sshPrivatePort) {
		this.vmId = vmId;
		this.networkId = networkId;
		this.publicIpId = publicIpId;
		this.publicIp = publicIp;
		this.monitorPublicPort = monitorPublicPort;
		this.monitorPrivatePort = monitorPrivatePort;
		this.sshPublicPort = sshPublicPort;
		this.sshPrivatePort = sshPrivatePort;
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

	@Column(name = "monitor_public_port", nullable = false)
	public Integer getMonitorPublicPort() {
		return this.monitorPublicPort;
	}

	public void setMonitorPublicPort(Integer monitorPublicPort) {
		this.monitorPublicPort = monitorPublicPort;
	}

	@Column(name = "monitor_private_port", nullable = false)
	public Integer getMonitorPrivatePort() {
		return this.monitorPrivatePort;
	}

	public void setMonitorPrivatePort(Integer monitorPrivatePort) {
		this.monitorPrivatePort = monitorPrivatePort;
	}

	@Column(name = "ssh_public_port", nullable = false)
	public Integer getSshPublicPort() {
		return this.sshPublicPort;
	}

	public void setSshPublicPort(Integer sshPublicPort) {
		this.sshPublicPort = sshPublicPort;
	}

	@Column(name = "ssh_private_port", nullable = false)
	public Integer getSshPrivatePort() {
		return this.sshPrivatePort;
	}

	public void setSshPrivatePort(Integer sshPrivatePort) {
		this.sshPrivatePort = sshPrivatePort;
	}

}