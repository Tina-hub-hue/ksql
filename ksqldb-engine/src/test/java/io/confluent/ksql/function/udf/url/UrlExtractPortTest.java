/*
 * Copyright 2018 Confluent Inc.
 *
 * Licensed under the Confluent Community License (the "License"); you may not use
 * this file except in compliance with the License.  You may obtain a copy of the
 * License at
 *
 * http://www.confluent.io/confluent-community-license
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

package io.confluent.ksql.function.udf.url;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThrows;

import io.confluent.ksql.util.KsqlException;
import org.junit.Before;
import org.junit.Test;

public class UrlExtractPortTest {

  private UrlExtractPort extractUdf;

  @Before
  public void setUp() {
    extractUdf = new UrlExtractPort();
  }

  @Test
  public void shouldExtractPortIfPresent() {
    assertThat(
            extractUdf.extractPort("https://docs.confluent.io:8080/current/ksql/docs/syntax-reference.html#scalar-functions"),
            equalTo(8080));
  }

  @Test
  public void shouldReturnNullIfNoPort() {
    assertThat(
            extractUdf.extractPort("https://docs.confluent.io/current/ksql/docs/syntax-reference.html#scalar-functions"),
            equalTo(null));
  }

  @Test
  public void shouldThrowExceptionForMalformedURL() {
    // When:
    final KsqlException e = assertThrows(
        KsqlException.class,
        () -> extractUdf.extractPort("http://257.1/bogus/[url")
    );

    // Then:
    assertThat(e.getMessage(), containsString("URL input has invalid syntax: http://257.1/bogus/[url"));
  }
}
