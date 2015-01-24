package com.cloud.ocs.monitor.bean;
// default package

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SessionRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "session_record", catalog = "cloud_ocs_monitor")
public class SessionRecord implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	private String sessionId;
	private Integer seqNo;
	private Integer sessionState;
	private Timestamp startTime;
	private Timestamp endTime;
	private String routeIp;

	// Constructors

	/** default constructor */
	public SessionRecord() {
	}

	/** minimal constructor */
	public SessionRecord(String sessionId) {
		this.sessionId = sessionId;
	}

	/** full constructor */
	public SessionRecord(String sessionId, Integer seqNo, Integer sessionState,
			Timestamp startTime, Timestamp endTime, String routeIp) {
		this.sessionId = sessionId;
		this.seqNo = seqNo;
		this.sessionState = sessionState;
		this.startTime = startTime;
		this.endTime = endTime;
		this.routeIp = routeIp;
	}

	// Property accessors
	@Id
	@Column(name = "SessionID", unique = true, nullable = false, length = 32)
	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Column(name = "SeqNO")
	public Integer getSeqNo() {
		return this.seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	@Column(name = "SessionState")
	public Integer getSessionState() {
		return this.sessionState;
	}

	public void setSessionState(Integer sessionState) {
		this.sessionState = sessionState;
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

	@Column(name = "RouteIP", length = 32)
	public String getRouteIp() {
		return this.routeIp;
	}

	public void setRouteIp(String routeIp) {
		this.routeIp = routeIp;
	}

}