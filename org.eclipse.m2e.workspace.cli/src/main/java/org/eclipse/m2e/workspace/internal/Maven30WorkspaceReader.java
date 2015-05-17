/*******************************************************************************
 * Copyright (c) 2008 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.m2e.workspace.internal;

import java.io.File;
import java.util.List;

import org.codehaus.plexus.component.annotations.Component;

import org.sonatype.aether.repository.WorkspaceReader;
import org.sonatype.aether.repository.WorkspaceRepository;

import org.eclipse.m2e.workspace.WorkspaceState2;


/**
 * Enables workspace resolution in Maven 3.0.x
 */
@Component(role = WorkspaceReader.class, hint = "ide")
public final class Maven30WorkspaceReader implements WorkspaceReader {

  private WorkspaceRepository workspaceRepository;

  public Maven30WorkspaceReader() {
    this.workspaceRepository = new WorkspaceRepository("ide", getClass());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode(); // no state
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Maven30WorkspaceReader;
  }

  public WorkspaceRepository getRepository() {
    return workspaceRepository;
  }

  public File findArtifact(org.sonatype.aether.artifact.Artifact artifact) {
    return WorkspaceState2.getInstance().findArtifact(artifact.getGroupId(), artifact.getArtifactId(),
        artifact.getExtension(), artifact.getClassifier(), artifact.getBaseVersion());
  }

  public List<String> findVersions(org.sonatype.aether.artifact.Artifact artifact) {
    return WorkspaceState2.getInstance().findVersions(artifact.getGroupId(), artifact.getArtifactId());
  }

}
