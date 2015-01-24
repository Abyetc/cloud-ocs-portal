package com.cloud.ocs.portal.core.business.bean;
// default package

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * OcsVm entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ocs_vm", catalog = "cloud_ocs_portal")
public class OcsVm implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String vmId;
	private String networkId;
	private String publicIpId;
	private String publicIp;
	private String privateIp;
	private Timestamp created;

	// Constructors

	/** default constructor */
	public OcsVm() {
	}

	/** minimal constructor */
	public OcsVm(String vmId, String networkId, String publicIpId,
			String publicIp, String privateIp) {
		this.vmId = vmId;
		this.networkId = networkId;
		this.publicIpId = publicIpId;
		this.publicIp = publicIp;
		this.privateIp = privateIp;
	}

	/** full constructor */
	public OcsVm(String vmId, String networkId, String publicIpId,
			String publicIp, String privateIp, Timestamp created) {
		this.vmId = vmId;
		this.networkId = networkId;
		this.publicIpId = publicIpId;
		this.publicIp = publicIp;
		this.privateIp = privateIp;
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

	@Column(name = "created", length = 19)
	public Timestamp getCreated() {
		return this.created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

}