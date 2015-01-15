package com.cloud.ocs.portal.core.business.bean;
// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * VmForwardingPort entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "vm_forwarding_port", catalog = "cloud_ocs_portal")
public class VmForwardingPort implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String vmId;
	private String networkId;
	private String publicIpId;
	private String publicIp;
	private Integer publicPort;
	private Integer privatePort;

	// Constructors

	/** default constructor */
	public VmForwardingPort() {
	}

	/** full constructor */
	public VmForwardingPort(String vmId, String networkId, String publicIpId,
			String publicIp, Integer publicPort, Integer privatePort) {
		this.vmId = vmId;
		this.networkId = networkId;
		this.publicIpId = publicIpId;
		this.publicIp = publicIp;
		this.publicPort = publicPort;
		this.privatePort = privatePort;
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

	@Column(name = "public_port", nullable = false)
	public Integer getPublicPort() {
		return this.publicPort;
	}

	public void setPublicPort(Integer publicPort) {
		this.publicPort = publicPort;
	}

	@Column(name = "private_port", nullable = false)
	public Integer getPrivatePort() {
		return this.privatePort;
	}

	public void setPrivatePort(Integer privatePort) {
		this.privatePort = privatePort;
	}

}