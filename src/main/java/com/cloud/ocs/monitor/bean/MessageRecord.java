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
	private Timestamp receivedTime;
	private Timestamp cfstartTime;
	private Timestamp endTime;
	private Integer waitingTime;
	private Integer cfprocessTime;
	private Integer totalProcessTime;

	// Constructors

	/** default constructor */
	public MessageRecord() {
	}

	/** full constructor */
	public MessageRecord(String sessionId, Integer requestType,
			Integer requestNum, String routeIp, String vmip,
			Timestamp receivedTime, Timestamp cfstartTime, Timestamp endTime,
			Integer waitingTime, Integer cfprocessTime, Integer totalProcessTime) {
		this.sessionId = sessionId;
		this.requestType = requestType;
		this.requestNum = requestNum;
		this.routeIp = routeIp;
		this.vmip = vmip;
		this.receivedTime = receivedTime;
		this.cfstartTime = cfstartTime;
		this.endTime = endTime;
		this.waitingTime = waitingTime;
		this.cfprocessTime = cfprocessTime;
		this.totalProcessTime = totalProcessTime;
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

	@Column(name = "ReceivedTime", length = 19)
	public Timestamp getReceivedTime() {
		return this.receivedTime;
	}

	public void setReceivedTime(Timestamp receivedTime) {
		this.receivedTime = receivedTime;
	}

	@Column(name = "CFStartTime", length = 19)
	public Timestamp getCfstartTime() {
		return this.cfstartTime;
	}

	public void setCfstartTime(Timestamp cfstartTime) {
		this.cfstartTime = cfstartTime;
	}

	@Column(name = "EndTime", length = 19)
	public Timestamp getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	@Column(name = "WaitingTime")
	public Integer getWaitingTime() {
		return this.waitingTime;
	}

	public void setWaitingTime(Integer waitingTime) {
		this.waitingTime = waitingTime;
	}

	@Column(name = "CFProcessTime")
	public Integer getCfprocessTime() {
		return this.cfprocessTime;
	}

	public void setCfprocessTime(Integer cfprocessTime) {
		this.cfprocessTime = cfprocessTime;
	}

	@Column(name = "TotalProcessTime")
	public Integer getTotalProcessTime() {
		return this.totalProcessTime;
	}

	public void setTotalProcessTime(Integer totalProcessTime) {
		this.totalProcessTime = totalProcessTime;
	}
}