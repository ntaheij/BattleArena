package mc.alk.arena.serializers;

import java.io.File;

/**
 * @deprecated As of BattleArena v3.10.0.0
 * Instead, use {@link mc.alk.v1r9.serializers.BaseConfig}
 */
public class BaseConfig extends mc.alk.v1r9.serializers.BaseConfig {

	public BaseConfig(File file) {
		this.setConfig(file);
	}
}
