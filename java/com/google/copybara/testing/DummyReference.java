// Copyright 2016 Google Inc. All Rights Reserved.
package com.google.copybara.testing;

import com.google.common.base.Preconditions;
import com.google.copybara.Change;
import com.google.copybara.Origin;
import com.google.copybara.RepoException;

import org.joda.time.DateTime;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import javax.annotation.Nullable;

/**
 * A reference of a change used for testing. This can be used with a {@link DummyOrigin} instance or
 * without an actual {@link Origin} implementation.
 */
public class DummyReference implements Origin.ReferenceFiles<DummyOrigin> {

  private final String reference;
  private final String message;
  private final String author;
  private final Path changesBase;
  private final Long timestamp;

  public DummyReference(String reference) {
    this(reference, "DummyReference message", "DummyReference author",
        Paths.get("/DummyReference", reference), /*timestamp=*/null);
  }

  DummyReference(
      String reference, String message, String author, Path changesBase,
      @Nullable Long timestamp) {
    this.reference = Preconditions.checkNotNull(reference);
    this.message = Preconditions.checkNotNull(message);
    this.author = Preconditions.checkNotNull(author);
    this.changesBase = Preconditions.checkNotNull(changesBase);
    this.timestamp = timestamp;
  }

  @Override
  public void checkout(final Path workdir) throws RepoException {
    try {
      Files.walkFileTree(changesBase, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
            throws IOException {
          Path destination = workdir.resolve(changesBase.relativize(file).toString());
          Files.createDirectories(destination.getParent());
          Files.write(destination, Files.readAllBytes(file));
          return FileVisitResult.CONTINUE;
        }
      });
    } catch (IOException e) {
      throw new RepoException("Error copying files", e);
    }
  }

  /**
   * Returns an instance equivalent to this one but with the timestamp set to the specified value.
   */
  public DummyReference withTimestamp(long newTimestamp) {
    return new DummyReference(
        this.reference, this.message, this.author, this.changesBase, newTimestamp);
  }

  @Nullable
  @Override
  public Long readTimestamp() throws RepoException {
    return timestamp;
  }

  @Override
  public String asString() {
    return reference;
  }

  @Override
  public String getLabelName() {
    return DummyOrigin.LABEL_NAME;
  }

  Change<DummyOrigin> toChange() {
    return new Change<>(this, author, message, new DateTime(timestamp));
  }

}
