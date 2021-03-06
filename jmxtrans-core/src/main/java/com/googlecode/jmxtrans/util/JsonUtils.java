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
package com.googlecode.jmxtrans.util;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.googlecode.jmxtrans.model.JmxProcess;

public class JsonUtils {

	@Nonnull private final ObjectMapper mapper;

	@Inject
	public JsonUtils(
			@Nonnull ObjectMapper mapper,
			@Nonnull PlaceholderResolverJsonNodeFactory placeholderResolverJsonNodeFactory) {
		this.mapper = mapper;
		// configuring mapper here is dead ugly, but I do not yet understand how ObjectMapperModule works to do it properly
		this.mapper.setNodeFactory(placeholderResolverJsonNodeFactory);
	}

	/**
	 * Uses jackson to load json configuration from a File into a full object
	 * tree representation of that json.
	 */
	public JmxProcess parseProcess(File file) throws IOException {
		JsonNode jsonNode = mapper.readTree(file);
		JmxProcess jmx = mapper.treeToValue(jsonNode, JmxProcess.class);
		jmx.setName(file.getName());
		return jmx;
	}
}
