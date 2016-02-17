package mcia.timeseries.exporter.sink;

import mcia.timeseries.exporter.domain.TimePoint;
import rx.Observable;

public interface TimeSeriesSink {

	public void export(String serieId, Observable<TimePoint> points);

}
