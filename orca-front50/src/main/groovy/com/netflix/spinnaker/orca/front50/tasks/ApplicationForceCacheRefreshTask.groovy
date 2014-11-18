/*
 * Copyright 2014 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.orca.front50.tasks

import com.netflix.spinnaker.orca.DefaultTaskResult
import com.netflix.spinnaker.orca.PipelineStatus
import com.netflix.spinnaker.orca.Task
import com.netflix.spinnaker.orca.TaskResult
import com.netflix.spinnaker.orca.oort.OortService
import com.netflix.spinnaker.orca.pipeline.model.ImmutableStage
import org.springframework.beans.factory.annotation.Autowired

class ApplicationForceCacheRefreshTask implements Task {
  static final String REFRESH_TYPE = "Front50Applications"

  @Autowired
  OortService oort

  @Override
  TaskResult execute(ImmutableStage stage) {
    def account = stage.context."account"

    if (account) {
      oort.forceCacheUpdate(REFRESH_TYPE, [account: account])
      new DefaultTaskResult(PipelineStatus.SUCCEEDED)
    } else {
      new DefaultTaskResult(PipelineStatus.FAILED, ["application.refresh.failure.reason": "no credentials found"])
    }
  }
}
