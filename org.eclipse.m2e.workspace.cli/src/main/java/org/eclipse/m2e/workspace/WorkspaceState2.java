/*******************************************************************************
 * Copyright (c) 2008 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.m2e.workspace;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.maven.artifact.Artifact;


/**
 * @since 0.4
 */
public class WorkspaceState2 {
  public static final String SYSPROP_STATEFILE_LOCATION = "m2e.workspace.state";

  protected final Map<String, String> state;

  protected WorkspaceState2(Map<String, String> state) {
    if(state == null) {
      throw new NullPointerException();
    }
    this.state = state;
  }

  public Map<String, String> getState() {
    return state;
  }

  public Properties asProperties() {
    Properties properties = new Properties();
    for(Map.Entry<String, String> entry : state.entrySet()) {
      properties.put(entry.getKey(), entry.getValue());
    }
    return properties;
  }

  public boolean resolveArtifact(Artifact artifact) {
    String extension = artifact.getArtifactHandler().getExtension();
    File file = findArtifact(artifact.getGroupId(), artifact.getArtifactId(), extension, artifact.getClassifier(),
        artifact.getBaseVersion());

    if(file == null) {
      return false;
    }

    artifact.setFile(file);
    artifact.setResolved(true);
    return true;
  }

  public File findArtifact(String groupId, String artifactId, String type, String classifier, String baseVersion) {
    Map<String, String> state = getState();
    if(state.isEmpty()) {
      return null;
    }

    if(classifier == null) {
      classifier = "";
    }

    String key = groupId + ':' + artifactId + ':' + type + ':' + classifier + ':' + baseVersion;
    String value = state.get(key);

    if(value == null || value.length() == 0) {
      return null;
    }

    File file = new File(value);
    if(!file.exists()) {
      return null;
    }

    return file;
  }

  public List<String> findVersions(String groupId, String artifactId) {
    Map<String, String> state = getState();
    if(state.isEmpty()) {
      return Collections.emptyList();
    }

    String prefix = groupId + ':' + artifactId + ':';

    Set<String> versions = new LinkedHashSet<String>();
    for(Object obj : state.keySet()) {
      String key = (String) obj;
      if(key.startsWith(prefix)) {
        versions.add(key.substring(key.lastIndexOf(':') + 1));
      }
    }

    return new ArrayList<String>(versions);
  }

  //
  // default state
  //

  public static WorkspaceState2 load() {
    Map<String, String> state = new HashMap<>();

    String locations = System.getProperty(SYSPROP_STATEFILE_LOCATION);
    if(locations != null) {
      for(String location : locations.split(File.pathSeparator)) {
        load(state, location);
      }
    }

    return new WorkspaceState2(Collections.unmodifiableMap(state));
  }

  private static void load(Map<String, String> state, String location) {
    try (InputStream is = new FileInputStream(location)) {
      Properties properties = new Properties();
      properties.load(is);
      for(String key : properties.stringPropertyNames()) {
        state.put(key, properties.getProperty(key));
      }
    } catch(IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  private static WorkspaceState2 instance;

  public static synchronized WorkspaceState2 getInstance() {
    if(instance == null) {
      instance = load();
    }
    return instance;
  }
}
