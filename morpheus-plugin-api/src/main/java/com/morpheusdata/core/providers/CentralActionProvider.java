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

package com.morpheusdata.core.providers;

import com.morpheusdata.response.ServiceResponse;

import java.util.Map;

/**
 * Handles a Morpheus Central Service push action dispatched to this plugin.
 * <p>
 * Central delivers a push-action envelope whose top-level {@code type} is
 * {@code "plugin-provider-push-action"}. Core selects the target provider by
 * matching the envelope's nested {@code payload.code} against this provider's
 * inherited {@link #getCode()}; the matched provider's {@link #handle(Map)} is
 * then invoked with the raw payload. There is no selection or acknowledgement
 * method on this interface — selection relies entirely on {@link #getCode()}.
 * <p>
 * <b>Eligibility:</b> any registered {@code CentralActionProvider} is eligible
 * to be selected, regardless of whether it was loaded from a verified (public)
 * or a private/unverified plugin manager. Private plugins work without a
 * verified-only filter.
 * <p>
 * <b>Acknowledgement:</b> a provider does <b>not</b> acknowledge the action and
 * cannot influence acking. Core's {@code CentralServicesActionService} owns all
 * ack behavior — it decides what ack (if any) is sent back to Central based on
 * the {@link ServiceResponse} returned here. Returning from {@link #handle(Map)}
 * does not itself send an ack.
 * <p>
 * <b>Transaction (warning):</b> {@link #handle(Map)} runs <b>inside the
 * consumer's message transaction</b> and does not open its own (there is no
 * {@code withNewTransaction} around plugin dispatch). Persistence performed
 * within {@link #handle(Map)} is therefore <b>not independently committed</b>: a
 * failure or rollback in the enclosing message transaction can roll back the
 * author's writes. Do not assume an independent commit.
 * <p>
 * <b>Recommendation:</b> provide a unique, namespaced {@link #getCode()} (for
 * example {@code <pluginCode>.<action>}) so it does not collide with another
 * provider and reliably matches the dispatched envelope's {@code payload.code}.
 * A missing or mismatched {@code getCode()} means this handler is never invoked
 * and core acks {@code unknown_type}. This naming guidance is a recommendation
 * only and is not enforced.
 *
 * @author Morpheus Data
 * @since 1.5.0
 */
public interface CentralActionProvider extends PluginProvider {

	/**
	 * Handle a single Central-dispatched push action.
	 * <p>
	 * The handler receives only the raw {@code payload} {@link Map} carried by
	 * the envelope — it does not receive the action {@code id}, {@code headers},
	 * or {@code ackRequired}. This mirrors the built-in
	 * {@code CSAlertNotificationActionHandlerService.handle(Map payload)}.
	 * <p>
	 * The handler does not ack; core owns acking (see the interface-level
	 * documentation). To send ack data back to Central, return a
	 * {@link ServiceResponse} whose {@code data} is Map-shaped: core coerces a
	 * Map-like {@code data} to the {@code Map} carried on the ack (Map-like →
	 * {@code Map}, otherwise {@code null}), matching how the built-in handlers
	 * pass {@code data as Map}. Non-Map {@code data} is not guaranteed to
	 * propagate to the ack payload.
	 *
	 * @param payload the raw push-action payload {@link Map} (including
	 *                {@code payload.code}); never the action id, headers, or
	 *                ackRequired
	 * @return a {@link ServiceResponse} indicating success or failure; a
	 *         Map-shaped {@code data} is coerced to the ack data returned to
	 *         Central, non-Map {@code data} is not guaranteed to propagate
	 */
	ServiceResponse handle(Map payload);
}
