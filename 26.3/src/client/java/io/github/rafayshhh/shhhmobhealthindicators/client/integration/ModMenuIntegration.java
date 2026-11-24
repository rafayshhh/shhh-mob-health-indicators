package io.github.rafayshhh.shhhmobhealthindicators.client.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.rafayshhh.shhhmobhealthindicators.client.gui.ShhhHealthConfigScreen;

public class ModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return ShhhHealthConfigScreen::new;
	}
}
