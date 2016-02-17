package mcia.timeseries.exporter.domain;

public class TimeSeries {

	public String id;

	public long[] time;
	public double[] value;

	public TimeSeries() {
		this("undefined");
	}

	public TimeSeries(String id) {
		this.id = id;
	}

	public TimeSeries(int size) {
		this("undefined", size);
	}

	public TimeSeries(String id, int size) {
		this.id = id;
		this.time = new long[size];
		this.value = new double[size];
	}

	public TimeSeries(long[] time, double[] value) {
		this("undefined", time, value);
	}

	public TimeSeries(String id, long[] time, double[] value) {
		this.id = id;
		this.time = time;
		this.value = value;
	}

}
