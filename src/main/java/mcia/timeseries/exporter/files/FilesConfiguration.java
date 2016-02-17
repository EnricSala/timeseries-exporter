package mcia.timeseries.exporter.files;

import java.io.File;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;
import mcia.timeseries.exporter.sink.FilesTimeSeriesSink;
import mcia.timeseries.exporter.sink.TimeSeriesSink;

@Configuration
@Profile(FilesConfiguration.PROFILE)
@EnableConfigurationProperties(FilesSettings.class)
@Slf4j
public class FilesConfiguration {

	public static final String PROFILE = "files";

	@Bean
	public TimeSeriesSink filesTimeSeriesSink(FilesSettings settings) {
		log.info("Using TimeSeriesSink: Files, with {}", settings);
		File outputDir = new File(settings.getOutputDirectory());
		Assert.isTrue(outputDir.isDirectory(), "Output directory " + outputDir + " does not exist");
		return new FilesTimeSeriesSink(outputDir);
	}

}
