package com.cloud.ocs.monitor.bean;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ThroughputCapacityRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "throughput_capacity_record", catalog = "cloud_ocs_monitor")
public class ThroughputRecord implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	private Long rid;
	private String routeIp;
	private String vmip;
	private Integer receiveMsgCount;
	private Integer finishedMsgCount;
	private Integer recordInterval;
	private Timestamp recordTime;

	// Constructors

	/** default constructor */
	public ThroughputRecord() {
	}

	/** full constructor */
	public ThroughputRecord(String routeIp, String vmip,
			Integer receiveMsgCount, Integer finishedMsgCount,
			Integer recordInterval, Timestamp recordTime) {
		this.routeIp = routeIp;
		this.vmip = vmip;
		this.receiveMsgCount = receiveMsgCount;
		this.finishedMsgCount = finishedMsgCount;
		this.recordInterval = recordInterval;
		this.recordTime = recordTime;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Rid", unique = true, nullable = false)
	public Long getRid() {
		return this.rid;
	}

	public void setRid(Long rid) {
		this.rid = rid;
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

	@Column(name = "ReceiveMsgCount")
	public Integer getReceiveMsgCount() {
		return this.receiveMsgCount;
	}

	public void setReceiveMsgCount(Integer receiveMsgCount) {
		this.receiveMsgCount = receiveMsgCount;
	}

	@Column(name = "FinishedMsgCount")
	public Integer getFinishedMsgCount() {
		return this.finishedMsgCount;
	}

	public void setFinishedMsgCount(Integer finishedMsgCount) {
		this.finishedMsgCount = finishedMsgCount;
	}

	@Column(name = "RecordInterval")
	public Integer getRecordInterval() {
		return this.recordInterval;
	}

	public void setRecordInterval(Integer recordInterval) {
		this.recordInterval = recordInterval;
	}

	@Column(name = "RecordTime", length = 19)
	public Timestamp getRecordTime() {
		return this.recordTime;
	}

	public void setRecordTime(Timestamp recordTime) {
		this.recordTime = recordTime;
	}

}