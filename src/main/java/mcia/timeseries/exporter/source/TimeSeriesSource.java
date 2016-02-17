package mcia.timeseries.exporter.source;

import mcia.timeseries.exporter.domain.TimePoint;
import rx.Observable;

public interface TimeSeriesSource {

	public Observable<String> findAllSeries();

	public Observable<TimePoint> findAllPoints(String serieId);

}
