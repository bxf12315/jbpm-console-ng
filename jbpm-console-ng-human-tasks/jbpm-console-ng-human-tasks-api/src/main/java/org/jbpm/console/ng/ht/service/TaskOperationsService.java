/*
 * Copyright 2014 JBoss by Red Hat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.console.ng.ht.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jboss.errai.bus.server.annotations.Remote;
import org.jbpm.console.ng.ht.model.TaskAssignmentSummay;
import org.jbpm.console.ng.ht.model.TaskSummary;


/**
 *
 * @author salaboy
 */
@Remote
public interface TaskOperationsService{
  
  public long addQuickTask(
                         final String taskName,
                         int priority,
                         Date dueDate, final List<String> users, List<String> groups, String identity, boolean start, boolean claim);
  
  public void updateTask(long taskId, int priority, List<String> taskDescription, Date dueDate);
  
  TaskSummary getTaskDetails(long taskId);
  
  long saveContent(long taskId, Map<String, Object> values);
  
  boolean existInDatabase(long taskId);
  
  TaskAssignmentSummay getTaskAssignmentDetails(long taskId); 
  
}
