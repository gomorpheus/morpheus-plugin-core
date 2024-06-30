/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.core;

import io.reactivex.rxjava3.core.Single;

import java.util.List;

/**
 * Context methods for controlling usage records for ComputeServers, Instances, etc in Morpheus
 */
public interface MorpheusUsageService {

	/**
	 * Request that new usage records with status 'running' should be created for the given servers. Any existing
	 * usage records with status 'stopped' will be closed off (their endDate set to now) for the servers specified.
	 * Any containers associated with the servers will also have 'running' usage records created.
	 * If an existing usage record with status 'running' already exists and does not have an endDate, then a new
	 * record will NOT be created.
	 * @param serverIds servers to start
	 * @return success
	 */
	Single<Boolean> startServerUsage(List<Long> serverIds);

	/**
	 * Request that new usage records with status 'stopped' should be created for the given servers. Any existing
	 * usage records with status 'running' will be closed off (their endDate set to now) for the servers specified.
	 * Any containers associated with the servers will also have 'stopped' usage records created.
	 * If an existing usage record with status 'stopped' already exists and does not have an endDate, then a new
	 * record will NOT be created.
	 * @param serverIds servers to stop
	 * @return success
	 */
	Single<Boolean> stopServerUsage(List<Long> serverIds);

	/**
	 * Request that usage records be restarted for the given servers. This is often desired to recalculate pricing.
	 * If it is detected that restarting the usage records will result in identical usage records, then
	 * no action is performed. Otherwise, any existing usage records that do not have
	 * an endDate will be closed off (their endDate set to now) for the servers specified and new usage records with the
	 * same status will be recreated.
	 * Any containers associated with the servers are also restarted.
	 * @param serverIds servers to restart
	 * @return success
	 */
	Single<Boolean> restartServerUsage(List<Long> serverIds);

	/**
	 * Request that usage records be restarted for the given snapshots. This is often desired to recalculate pricing.
	 * If it is detected that restarting the usage records will result in identical usage records, then
	 * no action is performed. Otherwise, any existing usage records that do not have
	 * an endDate will be closed off (their endDate set to now) for the snapshots specified and new usage records with the
	 * same status will be recreated.
	 * @param snapshotIds snapshots to restart
	 * @return success
	 */
	Single<Boolean> restartSnapshotUsage(List<Long> snapshotIds);
}
