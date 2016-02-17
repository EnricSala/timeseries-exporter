package mcia.timeseries.exporter.cassandra;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(CassandraConfiguration.PROFILE)
@Data
public class CassandraSettings {

	@NotEmpty(message = "Cassandra CONTACT_POINTS cannot be empty")
	private String contactPoints;

	@Min(value = 1024, message = "Cassandra PORT must be greater than 1024")
	@Max(value = 65535, message = "Cassandra PORT must be lower than 65535")
	private int port;

	@NotEmpty(message = "Cassandra KEYSPACE cannot be empty")
	private String keyspace;

	@NotEmpty(message = "Cassandra TABLE cannot be empty")
	private String table;

	@NotNull
	private Column column;

	@Data
	public static class Column {

		@NotEmpty
		private String id;

		@NotEmpty
		private String time;

		@NotEmpty
		private String value;

	}

}
