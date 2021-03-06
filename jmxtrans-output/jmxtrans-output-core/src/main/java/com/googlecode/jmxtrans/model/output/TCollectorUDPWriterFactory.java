/**
 * The MIT License
 * Copyright (c) 2010 JmxTrans team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.googlecode.jmxtrans.model.output;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.googlecode.jmxtrans.exceptions.LifecycleException;
import com.googlecode.jmxtrans.model.OutputWriter;
import com.googlecode.jmxtrans.model.OutputWriterFactory;
import com.googlecode.jmxtrans.model.output.support.ResultTransformerOutputWriter;
import com.googlecode.jmxtrans.model.output.support.UdpOutputWriterBuilder;
import com.googlecode.jmxtrans.model.output.support.opentsdb.OpenTSDBMessageFormatter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;

import static com.google.common.base.MoreObjects.firstNonNull;

@ThreadSafe
@EqualsAndHashCode
@ToString
public class TCollectorUDPWriterFactory implements OutputWriterFactory {

	@Nonnull private final boolean booleanAsNumber;
	@Nonnull private final InetSocketAddress server;
	@Nonnull private final OpenTSDBMessageFormatter messageFormatter;

	@JsonCreator
	public TCollectorUDPWriterFactory(
			@JsonProperty("typeNames") ImmutableList<String> typeNames,
			@JsonProperty("booleanAsNumber") boolean booleanAsNumber,
			@JsonProperty("host") String host,
			@JsonProperty("port") Integer port,
			@JsonProperty("tags") Map<String, String> tags,
			@JsonProperty("tagName") String tagName,
			@JsonProperty("mergeTypeNamesTags") Boolean mergeTypeNamesTags,
			@JsonProperty("metricNamingExpression") String metricNamingExpression,
			@JsonProperty("addHostnameTag") Boolean addHostnameTag) throws LifecycleException, UnknownHostException {

		this.booleanAsNumber = booleanAsNumber;
		this.server = new InetSocketAddress(
				firstNonNull(host, "localhost"),
				firstNonNull(port, 3030));

		ImmutableMap<String, String> immutableTags =
				tags == null ? ImmutableMap.<String, String>of() : ImmutableMap.copyOf(tags);

		messageFormatter = new OpenTSDBMessageFormatter(typeNames, immutableTags, tagName,
				metricNamingExpression, mergeTypeNamesTags,
				addHostnameTag ? InetAddress.getLocalHost().getHostName() : null);
	}
	@Override
	public OutputWriter create() {
		return ResultTransformerOutputWriter.booleanToNumber(
				booleanAsNumber,
				UdpOutputWriterBuilder.builder(
						server,
						new TCollectorUDPWriter2(messageFormatter))
						.build());
	}
}
