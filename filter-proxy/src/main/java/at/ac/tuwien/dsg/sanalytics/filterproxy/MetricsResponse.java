package at.ac.tuwien.dsg.sanalytics.filterproxy;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;

import io.prometheus.client.Metrics;
import io.prometheus.client.Metrics.Metric;
import io.prometheus.client.Metrics.MetricFamily;
import io.prometheus.client.Metrics.MetricFamily.Builder;

public class MetricsResponse {

	public static MetricsResponse createEmpty() {
		return new MetricsResponse();
	}

	private HashMap<String, MetricFamily> mmap = new HashMap<>();
	
	private MetricsResponse() {
		//empty
	}
	
	
	public MetricsResponse(InputStream input, String slice) {
		this(input, slice, new String[0]);
	}
	
	public MetricsResponse(InputStream input, String slice, String[] omitLabels) {
		//TODO implement omitLabels functionality
		MetricFamily mf;
		try {
			while((mf = Metrics.MetricFamily.parseDelimitedFrom(input)) != null) {
				List<Metric> metrics = mf.getMetricList();
				
				List<Metric> filteredMetrics = metrics.stream()
						.filter(m -> hasSliceLabel(m, slice))
						.collect(Collectors.toList());
				
				if(filteredMetrics.size() > 0) {
					Builder mb = mf.toBuilder();
					mmap.put(mf.getName(), mb.clearMetric().addAllMetric(filteredMetrics).build());
				}
			}
		} catch (IOException e) {
			// TODO IOExceptions here should rather be thrown
			//further so the proxyServlet consumes the input quietly
			//and closes the connection
			e.printStackTrace();
		}
	}


	Map<String, MetricFamily> getMetricsFamilies() {
		return mmap;
	}

	private boolean hasSliceLabel(Metric m, String slice) {
		return slice == null || m.getLabelList().stream()
				.anyMatch(l -> l.getName().equals(Options.FILTER_CONTAINER_LABEL_NAME) && l.getValue().equals(slice));
	}

	public MetricsResponse mergeWith(MetricsResponse other) {
		MetricsResponse mr = new MetricsResponse();
		for(MetricFamily mf : this.mmap.values())
			mr.mmap.put(mf.getName(), mf.toBuilder().build());
		
		for(MetricFamily mf : other.mmap.values()) {
			String mfName = mf.getName();
			MetricFamily existingMF = mr.mmap.get(mfName);
			if(existingMF != null) {
				existingMF = existingMF.toBuilder().addAllMetric(mf.getMetricList()).build();
				mr.mmap.put(mfName, existingMF);
			} else {
				mr.mmap.put(mfName, mf);
			}
		}
		return mr;
	}

	public void writeDelimitedTo(ServletOutputStream os) throws IOException {
		for(MetricFamily mf : getMetricsFamilies().values()) {
			mf.writeDelimitedTo(os);
		}
	}
}
