/*******************************************************************************
 * Copyright (c) 2008 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.m2e.workspace;

import java.io.File;
import java.util.List;
import java.util.Properties;

import org.apache.maven.artifact.Artifact;


/**
 * @since 0.1
 * @deprecated use {@link WorkspaceState2}
 */
public class WorkspaceState {
  public static final String SYSPROP_STATEFILE_LOCATION = WorkspaceState2.SYSPROP_STATEFILE_LOCATION;

  public static Properties getState() {
    return WorkspaceState2.getInstance().asProperties();
  }

  public static boolean resolveArtifact(Artifact artifact) {
    return WorkspaceState2.getInstance().resolveArtifact(artifact);
  }

  public static File findArtifact(String groupId, String artifactId, String type, String classifier,
      String baseVersion) {
    return WorkspaceState2.getInstance().findArtifact(groupId, artifactId, type, classifier, baseVersion);
  }

  public static List<String> findVersions(String groupId, String artifactId) {
    return WorkspaceState2.getInstance().findVersions(groupId, artifactId);
  }

}
