package com.cloud.ocs.portal.core.business.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * NetworkVm entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "network_vm", catalog = "cloud_ocs_portal")
public class NetworkVm implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String networkId;
	private String vmId;
	private Integer vmState;

	// Constructors

	/** default constructor */
	public NetworkVm() {
	}

	/** full constructor */
	public NetworkVm(String networkId, String vmId, Integer vmState) {
		this.networkId = networkId;
		this.vmId = vmId;
		this.vmState = vmState;
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

	@Column(name = "network_id", nullable = false, length = 40)
	public String getNetworkId() {
		return this.networkId;
	}

	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}

	@Column(name = "vm_id", nullable = false, length = 40)
	public String getVmId() {
		return this.vmId;
	}

	public void setVmId(String vmId) {
		this.vmId = vmId;
	}

	@Column(name = "vm_state", nullable = false)
	public Integer getVmState() {
		return this.vmState;
	}

	public void setVmState(Integer vmState) {
		this.vmState = vmState;
	}

}