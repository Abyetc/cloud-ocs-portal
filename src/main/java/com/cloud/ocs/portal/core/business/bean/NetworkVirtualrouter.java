package com.cloud.ocs.portal.core.business.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * NetworkVirtualrouter entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "network_virtualrouter", catalog = "cloud_ocs_portal")
public class NetworkVirtualrouter implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private Integer id;
	private String networkId;
	private String virtualrouterId;
	private Integer virtualrouterState;

	// Constructors

	/** default constructor */
	public NetworkVirtualrouter() {
	}

	/** full constructor */
	public NetworkVirtualrouter(String networkId, String virtualrouterId,
			Integer virtualrouterState) {
		this.networkId = networkId;
		this.virtualrouterId = virtualrouterId;
		this.virtualrouterState = virtualrouterState;
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

	@Column(name = "virtualrouter_id", nullable = false, length = 40)
	public String getVirtualrouterId() {
		return this.virtualrouterId;
	}

	public void setVirtualrouterId(String virtualrouterId) {
		this.virtualrouterId = virtualrouterId;
	}

	@Column(name = "virtualrouter_state", nullable = false)
	public Integer getVirtualrouterState() {
		return this.virtualrouterState;
	}

	public void setVirtualrouterState(Integer virtualrouterState) {
		this.virtualrouterState = virtualrouterState;
	}

}