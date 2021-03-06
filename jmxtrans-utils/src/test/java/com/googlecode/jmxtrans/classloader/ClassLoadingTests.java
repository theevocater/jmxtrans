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
package com.googlecode.jmxtrans.classloader;

import com.googlecode.jmxtrans.test.IntegrationTest;
import com.kaching.platform.testing.AllowLocalFileAccess;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

@Category(IntegrationTest.class)
@AllowLocalFileAccess(paths = "*")
public class ClassLoadingTests {

	@Test(expected = ClassNotFoundException.class)
	public void classCannotBeAccessedIfJarIsNotLoaded() throws ClassNotFoundException {
		Class.forName("dummy.Dummy");
	}

	/**
	 * This test modify the class loader. This makes it hard to isolate, so let's just run it manually.
	 */
	@Test
	@Ignore("Manual test")
	public void loadedJarCanBeAccessed() throws ClassNotFoundException, MalformedURLException, FileNotFoundException, URISyntaxException {
		File jarFile = new File(ClassLoadingTests.class.getResource("/dummy.jar").toURI());

		new ClassLoaderEnricher().add(jarFile);
		Class.forName("dummy.Dummy");
	}

	@Test(expected = FileNotFoundException.class)
	public void loadingNonExistingFileThrowsException() throws MalformedURLException, FileNotFoundException {
		new ClassLoaderEnricher().add(new File("/nonexising"));
	}

}
