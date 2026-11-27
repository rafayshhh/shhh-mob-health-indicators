package io.github.rafayshhh.customizablemobshealthindicator.client.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.rafayshhh.customizablemobshealthindicator.client.gui.CustomizableHealthConfigScreen;

public class ModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return CustomizableHealthConfigScreen::new;
	}
}
