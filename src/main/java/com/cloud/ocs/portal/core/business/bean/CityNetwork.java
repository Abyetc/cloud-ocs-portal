package com.cloud.ocs.portal.core.business.bean;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CityNetwork entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "city_network", catalog = "cloud_ocs_portal")
public class CityNetwork implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer cityId;
	private String zoneId;
	private String networkId;
	private String networkOfferingId;
	private String networkName;
	private String publicIp;
	private Integer servicePort;
	private String vlan;
	private Integer networkState;
	private String realmName;
	private Timestamp created;

	// Constructors

	/** default constructor */
	public CityNetwork() {
	}

	/** minimal constructor */
	public CityNetwork(Integer cityId, String zoneId, String networkId,
			String networkOfferingId, String networkName, Integer networkState,
			Timestamp created) {
		this.cityId = cityId;
		this.zoneId = zoneId;
		this.networkId = networkId;
		this.networkOfferingId = networkOfferingId;
		this.networkName = networkName;
		this.networkState = networkState;
		this.created = created;
	}

	/** full constructor */
	public CityNetwork(Integer cityId, String zoneId, String networkId,
			String networkOfferingId, String networkName, String publicIp,
			Integer servicePort, String vlan, Integer networkState,
			String realmName, Timestamp created) {
		this.cityId = cityId;
		this.zoneId = zoneId;
		this.networkId = networkId;
		this.networkOfferingId = networkOfferingId;
		this.networkName = networkName;
		this.publicIp = publicIp;
		this.servicePort = servicePort;
		this.vlan = vlan;
		this.networkState = networkState;
		this.realmName = realmName;
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

	@Column(name = "city_id", nullable = false)
	public Integer getCityId() {
		return this.cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	@Column(name = "zone_id", nullable = false, length = 40)
	public String getZoneId() {
		return this.zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	@Column(name = "network_id", nullable = false, length = 40)
	public String getNetworkId() {
		return this.networkId;
	}

	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}

	@Column(name = "network_offering_id", nullable = false, length = 40)
	public String getNetworkOfferingId() {
		return this.networkOfferingId;
	}

	public void setNetworkOfferingId(String networkOfferingId) {
		this.networkOfferingId = networkOfferingId;
	}

	@Column(name = "network_name", nullable = false, length = 100)
	public String getNetworkName() {
		return this.networkName;
	}

	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}

	@Column(name = "public_ip", length = 40)
	public String getPublicIp() {
		return this.publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	@Column(name = "service_port")
	public Integer getServicePort() {
		return this.servicePort;
	}

	public void setServicePort(Integer servicePort) {
		this.servicePort = servicePort;
	}

	@Column(name = "vlan", length = 10)
	public String getVlan() {
		return this.vlan;
	}

	public void setVlan(String vlan) {
		this.vlan = vlan;
	}

	@Column(name = "network_state", nullable = false)
	public Integer getNetworkState() {
		return this.networkState;
	}

	public void setNetworkState(Integer networkState) {
		this.networkState = networkState;
	}

	@Column(name = "realm_name", length = 50)
	public String getRealmName() {
		return this.realmName;
	}

	public void setRealmName(String realmName) {
		this.realmName = realmName;
	}

	@Column(name = "created", nullable = false, length = 19)
	public Timestamp getCreated() {
		return this.created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

}