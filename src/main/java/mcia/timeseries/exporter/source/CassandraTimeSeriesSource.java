package mcia.timeseries.exporter.source;

import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

import lombok.extern.slf4j.Slf4j;
import mcia.timeseries.exporter.cassandra.CassandraSettings;
import mcia.timeseries.exporter.domain.TimePoint;
import rx.Observable;

@Slf4j
public class CassandraTimeSeriesSource implements TimeSeriesSource {

	private final CassandraOperations cassandra;
	private final CassandraSettings settings;

	public CassandraTimeSeriesSource(CassandraOperations cassandra, CassandraSettings settings) {
		this.cassandra = cassandra;
		this.settings = settings;
	}

	@Override
	public Observable<String> findAllSeries() {
		log.debug("Calling findAllSeries()");
		Select select = QueryBuilder
				.select(settings.getColumn().getId())
				.distinct()
				.from(settings.getTable());
		return Observable
				.just(select)
				.doOnNext(s -> log.debug("Running async select: {}", s))
				.map(cassandra::queryAsynchronously)
				.flatMap(Observable::from)
				.flatMapIterable(list -> list)
				.map(row -> row.getString(settings.getColumn().getId()));
	}

	@Override
	public Observable<TimePoint> findAllPoints(String serieId) {
		log.debug("Calling findAllPoints({})", serieId);
		Select select = QueryBuilder
				.select(settings.getColumn().getTime(), settings.getColumn().getValue())
				.from(settings.getTable())
				.where(QueryBuilder.eq(settings.getColumn().getId(), serieId))
				.limit(Integer.MAX_VALUE);
		return Observable
				.just(select)
				.doOnNext(s -> log.debug("Running async select: {}", s))
				.map(cassandra::queryAsynchronously)
				.flatMap(Observable::from)
				.flatMapIterable(list -> list)
				.map(this::toTimePoint)
				.filter(point -> point != null);
	}

	private TimePoint toTimePoint(Row row) {
		try {
			double value = Double.parseDouble(row.getString(settings.getColumn().getValue()));
			long time = row.getDate(settings.getColumn().getTime()).getTime();
			return new TimePoint(time, value);
		} catch (NumberFormatException nfe) {
			return null;
		}
	}

}
