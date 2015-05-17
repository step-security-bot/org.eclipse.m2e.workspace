/*******************************************************************************
 * Copyright (c) 2014 Takari, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.m2e.workspace;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;


/**
 * @since 0.1
 */
public class MutableWorkspaceState extends WorkspaceState2 {

  public MutableWorkspaceState() {
    super(new HashMap<String, String>());
  }

  public void putPom(File pom, String groupId, String artifactId, String version) {
    String key = groupId + ":" + artifactId + ":pom::" + version;
    state.put(key, pom.getAbsolutePath());
  }

  public void putArtifact(File artifact, String groupId, String artifactId, String extension, String classifier,
      String version) {
    if(classifier == null) {
      classifier = ""; // TODO not pretty
    }
    String key = groupId + ":" + artifactId + ":" + extension + ":" + classifier + ":" + version;
    state.put(key, artifact.getAbsolutePath());
  }

  public void store(File file) throws IOException {
    File dir = file.getParentFile();
    if(!dir.isDirectory() && !dir.mkdirs()) {
      throw new IOException("Could not create directory " + dir);
    }
    try (OutputStream os = new BufferedOutputStream(new FileOutputStream(file))) {
      store(os);
    }
  }

  /**
   * @since 0.2
   */
  public void store(OutputStream os) throws IOException {
    asProperties().store(os, null);
  }
}
