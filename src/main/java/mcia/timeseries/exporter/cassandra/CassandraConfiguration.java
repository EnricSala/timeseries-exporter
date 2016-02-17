package mcia.timeseries.exporter.cassandra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;

import lombok.extern.slf4j.Slf4j;
import mcia.timeseries.exporter.source.CassandraTimeSeriesSource;
import mcia.timeseries.exporter.source.TimeSeriesSource;

@Configuration
@Profile(CassandraConfiguration.PROFILE)
@EnableConfigurationProperties(CassandraSettings.class)
@Slf4j
public class CassandraConfiguration extends AbstractCassandraConfiguration {

	public static final String PROFILE = "cassandra";

	private @Autowired CassandraSettings settings;

	@Override
	protected String getContactPoints() {
		return settings.getContactPoints();
	}

	@Override
	protected int getPort() {
		return settings.getPort();
	}

	@Override
	protected String getKeyspaceName() {
		return settings.getKeyspace();
	}

	@Override
	protected PoolingOptions getPoolingOptions() {
		PoolingOptions opts = new PoolingOptions();
		opts.setMaxConnectionsPerHost(HostDistance.LOCAL, 4);
		opts.setCoreConnectionsPerHost(HostDistance.LOCAL, 2);
		return super.getPoolingOptions();
	}

	@Override
	public SchemaAction getSchemaAction() {
		return SchemaAction.NONE;
	}

	@Bean
	public TimeSeriesSource cassandraTimeSeriesSource(CassandraOperations cassandra, CassandraSettings settings) {
		log.info("Using TimeSeriesSource: Cassandra, with {}", settings);
		return new CassandraTimeSeriesSource(cassandra, settings);
	}

}
