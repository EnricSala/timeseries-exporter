package mcia.timeseries.exporter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mcia.timeseries.exporter.sink.TimeSeriesSink;
import mcia.timeseries.exporter.source.TimeSeriesSource;

@Component
@Slf4j
public class ApplicationEntryPoint implements ApplicationRunner {

	private @Autowired ConfigurableApplicationContext context;
	private @Autowired TimeSeriesSource source;
	private @Autowired TimeSeriesSink sink;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		source
			.findAllSeries()
			.doAfterTerminate(this::shutdown)
			.subscribe(id -> sink.export(id, source.findAllPoints(id)));
	}

	private void shutdown() {
		log.info("Shutting down...");
		context.close();
	}

}
