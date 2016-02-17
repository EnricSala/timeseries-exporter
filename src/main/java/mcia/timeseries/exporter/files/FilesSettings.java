package mcia.timeseries.exporter.files;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(FilesConfiguration.PROFILE)
@Data
public class FilesSettings {

	private String outputDirectory;

}
