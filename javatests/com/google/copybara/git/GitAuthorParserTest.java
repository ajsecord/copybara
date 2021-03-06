/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.copybara.git;

import static com.google.common.truth.Truth.assertThat;

import com.google.copybara.Author;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class GitAuthorParserTest {

  @Rule
  public final ExpectedException expectedException = ExpectedException.none();

  @Test
  public void testParseComplete() throws Exception {
    checkAuthorFormat("Foo Bar <foo@bar.com>", "Foo Bar", "foo@bar.com");
  }

  @Test
  public void testParseEmptyEmail() throws Exception {
    checkAuthorFormat("Foo Bar <>", "Foo Bar", "");
  }

  @Test
  public void testParseWrongFormat() throws Exception {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage(
        "Invalid author 'Foo Bar'. Must be in the form of 'Name <email>'");
    GitAuthorParser.parse("Foo Bar");
  }

  private void checkAuthorFormat(String gitAuthor, String expectedName, String expectedEmail) {
    Author author = GitAuthorParser.parse(gitAuthor);
    assertThat(author.getName()).isEqualTo(expectedName);
    assertThat(author.getEmail()).isEqualTo(expectedEmail);
  }
}
