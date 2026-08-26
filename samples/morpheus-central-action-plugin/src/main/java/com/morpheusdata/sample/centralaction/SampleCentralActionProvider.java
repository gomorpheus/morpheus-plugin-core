package com.morpheusdata.sample.centralaction;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.Plugin;
import com.morpheusdata.core.providers.CentralActionProvider;
import com.morpheusdata.response.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal reference {@link CentralActionProvider}. It handles a single Morpheus Central Service
 * push action and echoes one field of the payload back to Central as the {@code ok}-ack data.
 * <p>
 * <b>Envelope / routing.</b> Central sends a push action whose top-level
 * {@code type == "plugin-provider-push-action"}. Core resolves the target provider by matching the
 * envelope's nested {@code payload.code} against {@link #getCode()} — here
 * {@code "sample-central-action.echo"}. There is no {@code isPluginProvider} flag and no selection
 * method on the interface.
 * <p>
 * <b>Code naming (Q8).</b> {@link #getCode()} follows the recommended
 * {@code <pluginCode>.<action>} convention ({@code sample-central-action} + {@code echo}) so it is
 * globally unique and will not collide with a built-in action type or another plugin. This is a
 * documented recommendation only; core does not enforce it.
 * <p>
 * <b>Ack.</b> This provider does <b>not</b> ack. It returns a {@link ServiceResponse}; core's
 * {@code CentralServicesActionService} owns all ack behavior. A Map-shaped
 * {@link ServiceResponse#getData()} is coerced by core to the {@code ok} ack's {@code data};
 * a {@code !success} response or a thrown exception becomes a {@code handler_failed} ack.
 * <p>
 * <b>Transaction.</b> {@link #handle(Map)} runs inside the consumer's message transaction and does
 * not open its own. Any persistence performed here is not independently committed — a rollback in
 * the surrounding message transaction can roll back the handler's writes.
 */
public class SampleCentralActionProvider implements CentralActionProvider {

	private static final Logger log = LoggerFactory.getLogger(SampleCentralActionProvider.class);

	protected MorpheusContext morpheusContext;
	protected Plugin plugin;

	public SampleCentralActionProvider(Plugin plugin, MorpheusContext morpheusContext) {
		this.plugin = plugin;
		this.morpheusContext = morpheusContext;
	}

	/**
	 * Handle a single Central-dispatched push action.
	 * <p>
	 * The handler receives only the raw {@code payload} {@link Map} (including {@code payload.code}).
	 * It performs one trivial, observable action — it logs the payload and echoes the incoming
	 * {@code message} field back — and returns a successful {@link ServiceResponse} whose Map
	 * {@code data} demonstrates the {@code ok}-ack data round-trip. It does not ack (core owns
	 * acking).
	 *
	 * @param payload the raw push-action payload Map
	 * @return a successful {@link ServiceResponse} carrying {@code {"echoed": <message>}}
	 */
	@Override
	public ServiceResponse handle(Map payload) {
		Object message = payload != null ? payload.get("message") : null;
		log.info("SampleCentralActionProvider handling push action code={} message={}",
				payload != null ? payload.get("code") : null, message);

		Map<String, Object> data = new LinkedHashMap<>();
		data.put("echoed", message);

		// Return only — do NOT ack here. Core coerces this Map data onto the ok ack.
		return ServiceResponse.success(data);
	}

	@Override
	public MorpheusContext getMorpheus() {
		return this.morpheusContext;
	}

	@Override
	public Plugin getPlugin() {
		return this.plugin;
	}

	/**
	 * Namespaced provider code, following the recommended {@code <pluginCode>.<action>} convention.
	 * Central routes a {@code plugin-provider-push-action} to this provider when the envelope's
	 * {@code payload.code} equals this value.
	 *
	 * @return {@code "sample-central-action.echo"}
	 */
	@Override
	public String getCode() {
		return "sample-central-action.echo";
	}

	@Override
	public String getName() {
		return "Sample Central Action (Echo)";
	}
}
