package com.netease.qa.log.meta;

public class NginxAccess {

	private int nid;
	private int logSourceId;
	private String url;
	private long startTime;
	private int totalCount;
	private int requestTimeTotal;
	private int requestTimeMax;
	private int upstreamResponseTimeTotal;
	private int upstreamResponseTimeMax;
	private int okCount;
	private int error4Count;
	private int error5Count;
	private int byteTotal;
	private int requestTime90;
	private int requestTime99;
	private int upstreamResponseTime90;
	private int upstreamResponseTime99;
	
	public int getNid() {
		return nid;
	}
	public void setNid(int nid) {
		this.nid = nid;
	}
	public int getLogSourceId() {
		return logSourceId;
	}
	public void setLogSourceId(int logSourceId) {
		this.logSourceId = logSourceId;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getRequestTimeTotal() {
		return requestTimeTotal;
	}
	public void setRequestTimeTotal(int requestTimeTotal) {
		this.requestTimeTotal = requestTimeTotal;
	}
	public int getRequestTimeMax() {
		return requestTimeMax;
	}
	public void setRequestTimeMax(int requestTimeMax) {
		this.requestTimeMax = requestTimeMax;
	}
	public int getUpstreamResponseTimeTotal() {
		return upstreamResponseTimeTotal;
	}
	public void setUpstreamResponseTimeTotal(int upstreamResponseTimeTotal) {
		this.upstreamResponseTimeTotal = upstreamResponseTimeTotal;
	}
	public int getUpstreamResponseTimeMax() {
		return upstreamResponseTimeMax;
	}
	public void setUpstreamResponseTimeMax(int upstreamResponseTimeMax) {
		this.upstreamResponseTimeMax = upstreamResponseTimeMax;
	}
	public int getOkCount() {
		return okCount;
	}
	public void setOkCount(int okCount) {
		this.okCount = okCount;
	}
	public int getError4Count() {
		return error4Count;
	}
	public void setError4Count(int error4Count) {
		this.error4Count = error4Count;
	}
	public int getError5Count() {
		return error5Count;
	}
	public void setError5Count(int error5Count) {
		this.error5Count = error5Count;
	}
	public int getByteTotal() {
		return byteTotal;
	}
	public void setByteTotal(int byteTotal) {
		this.byteTotal = byteTotal;
	}
	public int getRequestTime90() {
		return requestTime90;
	}
	public void setRequestTime90(int requestTime90) {
		this.requestTime90 = requestTime90;
	}
	public int getRequestTime99() {
		return requestTime99;
	}
	public void setRequestTime99(int requestTime99) {
		this.requestTime99 = requestTime99;
	}
	public int getUpstreamResponseTime90() {
		return upstreamResponseTime90;
	}
	public void setUpstreamResponseTime90(int upstreamResponseTime90) {
		this.upstreamResponseTime90 = upstreamResponseTime90;
	}
	public int getUpstreamResponseTime99() {
		return upstreamResponseTime99;
	}
	public void setUpstreamResponseTime99(int upstreamResponseTime99) {
		this.upstreamResponseTime99 = upstreamResponseTime99;
	}
	@Override
	public String toString() {
		return "NginxRecord [logSourceId=" + logSourceId + ", startTime=" + startTime + ", url=" + url
				+ ", totalCount=" + totalCount + ", requestTimeTotal=" + requestTimeTotal + ", requestTimeMax="
				+ requestTimeMax + ", upstreamResponseTimeTotal=" + upstreamResponseTimeTotal
				+ ", upstreamResponseTimeMax=" + upstreamResponseTimeMax + ", okCount=" + okCount + ", error4Count="
				+ error4Count + ", error5Count=" + error5Count + ", byteTotal=" + byteTotal + ", requestTime90="
				+ requestTime90 + ", requestTime99=" + requestTime99 + ", upstreamResponseTime90="
				+ upstreamResponseTime90 + ", upstreamResponseTime99=" + upstreamResponseTime99 + "]";
	}
}
