package org.onap.sdnc.apps.ms.gra;

import org.onap.aaf.cadi.filter.CadiFilter;
import org.onap.ccsdk.apps.filters.ContentTypeFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class FilterConfiguration {

    private static final Logger log = LoggerFactory.getLogger(FilterConfiguration.class);

	@Bean
	@Order(1)
	public FilterRegistrationBean<ContentTypeFilter> contentTypeFilter() {
		ContentTypeFilter filter = new ContentTypeFilter();

		FilterRegistrationBean<ContentTypeFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(filter);
		registrationBean.addUrlPatterns("/*");
		return registrationBean;
	}

	@Bean
	@Order(2)
	public FilterRegistrationBean<CadiFilter> cadiFilter() {
		CadiFilter filter = new CadiFilter();

		FilterRegistrationBean<CadiFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(filter);
		if ("none".equals(System.getProperty("cadi_prop_files", "none"))) {
            log.info("cadi_prop_files undefined, AAF CADI disabled");
			registrationBean.addUrlPatterns("/xxxx/*");
		} else {
			registrationBean.addUrlPatterns("/*");
			registrationBean.addInitParameter("cadi_prop_files", System.getProperty("cadi_prop_files"));
		}

		return registrationBean;
	}

}
