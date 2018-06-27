package grupo4.com.core.modelJSON;

public class LogAgente {
	
	private String timeReported;
	private String fromHost;
	private String fromHostIp;
	private String programName;
	private String sysLogSeverityText;
	
	public String getTimeReported() {
		return timeReported;
	}
	public void setTimeReported(String timeReported) {
		this.timeReported = timeReported;
	}
	public String getFromHost() {
		return fromHost;
	}
	public void setFromHost(String fromHost) {
		this.fromHost = fromHost;
	}
	public String getFromHostIp() {
		return fromHostIp;
	}
	public void setFromHostIp(String fromHostIp) {
		this.fromHostIp = fromHostIp;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getSysLogSeverityText() {
		return sysLogSeverityText;
	}
	public void setSysLogSeverityText(String sysLogSeverityText) {
		this.sysLogSeverityText = sysLogSeverityText;
	}
}
