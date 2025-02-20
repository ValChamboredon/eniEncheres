package fr.eni.eniEncheres.configuration;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class DateTimeConfig implements WebMvcConfigurer {

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new Converter<Timestamp, LocalDateTime>() {
			@Override
			public LocalDateTime convert(Timestamp source) {
				return source != null ? source.toLocalDateTime() : null;
			}
		});
		
		registry.addConverter(new Converter<LocalDateTime, Timestamp>() {
			@Override
			public Timestamp convert(LocalDateTime source) {
				return source != null ? Timestamp.valueOf(source) : null;
			}
		});
	}
}