package com.cloud.ocs.monitor.bean;
// default package

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * MessageRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "message_record", catalog = "cloud_ocs_monitor")
public class MessageRecord implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	private Long id;
	private String sessionId;
	private Integer requestType;
	private Integer requestNum;
	private String routeIp;
	private String vmip;
	private Timestamp startTime;
	private Timestamp endTime;
	private Integer processTime;

	// Constructors

	/** default constructor */
	public MessageRecord() {
	}

	/** full constructor */
	public MessageRecord(String sessionId, Integer requestType,
			Integer requestNum, String routeIp, String vmip,
			Timestamp startTime, Timestamp endTime, Integer processTime) {
		this.sessionId = sessionId;
		this.requestType = requestType;
		this.requestNum = requestNum;
		this.routeIp = routeIp;
		this.vmip = vmip;
		this.startTime = startTime;
		this.endTime = endTime;
		this.processTime = processTime;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "SessionID", length = 32)
	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Column(name = "RequestType")
	public Integer getRequestType() {
		return this.requestType;
	}

	public void setRequestType(Integer requestType) {
		this.requestType = requestType;
	}

	@Column(name = "RequestNum")
	public Integer getRequestNum() {
		return this.requestNum;
	}

	public void setRequestNum(Integer requestNum) {
		this.requestNum = requestNum;
	}

	@Column(name = "RouteIP", length = 32)
	public String getRouteIp() {
		return this.routeIp;
	}

	public void setRouteIp(String routeIp) {
		this.routeIp = routeIp;
	}

	@Column(name = "VMIP", length = 32)
	public String getVmip() {
		return this.vmip;
	}

	public void setVmip(String vmip) {
		this.vmip = vmip;
	}

	@Column(name = "StartTime", length = 19)
	public Timestamp getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	@Column(name = "EndTime", length = 19)
	public Timestamp getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	@Column(name = "ProcessTime")
	public Integer getProcessTime() {
		return this.processTime;
	}

	public void setProcessTime(Integer processTime) {
		this.processTime = processTime;
	}

}