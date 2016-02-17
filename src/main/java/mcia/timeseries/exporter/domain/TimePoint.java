package mcia.timeseries.exporter.domain;

public class TimePoint {

	public long time;
	public double value;

	public TimePoint() {
	}

	public TimePoint(long time, double value) {
		this.time = time;
		this.value = value;
	}

}
