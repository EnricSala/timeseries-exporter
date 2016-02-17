package mcia.timeseries.exporter.sink;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;
import mcia.timeseries.exporter.domain.TimePoint;
import rx.Observable;

@Slf4j
public class FilesTimeSeriesSink implements TimeSeriesSink {

	private static final String EXTENSION = ".csv";
	private static final int BUFFER_POINTS = 500;

	private final File outputDirectory;

	public FilesTimeSeriesSink(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	@Override
	public void export(String serieId, Observable<TimePoint> points) {
		String filename = toFileName(serieId);
		Path filePath = Paths.get(outputDirectory.getAbsolutePath(), filename);

		log.debug("Exporting {} to file: {}", serieId, filePath);

		BufferedWriter writer = openTimeSeriesFile(serieId, filePath);

		points
			.buffer(BUFFER_POINTS)
			.doAfterTerminate(() -> close(writer))
			.subscribe(list -> write(writer, list), 
			err -> log.error("Error exporting " + serieId, err),
			() -> log.info("Finished exporting {} to: {}", serieId, filePath));
	}

	private String toFileName(String serieId) {
		String filename = serieId.replaceAll("[^a-zA-Z0-9 \\._-]", "");
		Assert.isTrue(!filename.isEmpty(), "Serie id " + serieId + "cannot be converted to filename");
		return filename + EXTENSION;
	}

	private BufferedWriter openTimeSeriesFile(String id, Path filePath) {
		try {
			BufferedWriter writer = Files.newBufferedWriter(filePath);
			writer.write(id + "\n");
			return writer;
		} catch (IOException ioe) {
			log.warn("Could not create file " + filePath, ioe);
			throw new RuntimeException("Could not create file " + filePath, ioe);
		}
	}

	private void close(Writer writer) {
		try {
			writer.close();
		} catch (Exception ioe) {
			log.warn("Could not close writer");
			throw new RuntimeException("Could not close writer", ioe);
		}
	}

	private void write(BufferedWriter writer, List<TimePoint> list) {
		list.forEach(point -> write(writer, point));
	}

	private void write(BufferedWriter writer, TimePoint point) {
		try {
			writer.write(String.format("%d,%f\n", point.time, point.value));
		} catch (IOException ioe) {
			throw new RuntimeException("Error writing point", ioe);
		}
	}

}
