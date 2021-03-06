package com.google.copybara.folder;

import com.google.common.base.Preconditions;
import com.google.copybara.Origin.Reference;
import com.google.copybara.RepoException;
import java.nio.file.Path;
import javax.annotation.Nullable;

/**
 * A reference for folder origins.
 */
public class FolderReference implements Reference {

  final Path path;
  private final long timestamp;
  private final String labelName;

  FolderReference(Path path, long timestamp, String labelName) {
    this.path = Preconditions.checkNotNull(path);
    this.timestamp = timestamp;
    this.labelName = Preconditions.checkNotNull(labelName);
  }

  @Override
  public String asString() {
    return path.toString();
  }

  @Nullable
  @Override
  public Long readTimestamp() throws RepoException {
    return timestamp;
  }

  @Override
  public String getLabelName() {
    return labelName;
  }
}
