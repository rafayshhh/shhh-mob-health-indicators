package io.github.rafayshhh.customizablemobshealthindicator;

import io.github.rafayshhh.customizablemobshealthindicator.client.config.CustomizableHealthConfig;
import io.github.rafayshhh.customizablemobshealthindicator.client.config.HealthPreset;
import io.github.rafayshhh.customizablemobshealthindicator.client.health.HealthComponentBuilder;
import io.github.rafayshhh.customizablemobshealthindicator.client.health.HealthInfo;
import net.minecraft.network.chat.Component;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HealthComponentBuilderTest {

	private CustomizableHealthConfig config;

	@BeforeEach
	void setUp() {
		config = new CustomizableHealthConfig();
		config.maxHeartRows = 3;
		config.showAbsorption = true;
	}

	@Test
	void testStandardHealthProducesSingleRow() {
		HealthInfo info = HealthInfo.of(20.0f, 20.0f, 0.0f);
		List<Component> rows = HealthComponentBuilder.buildRows(info, HealthPreset.HEARTS, config);

		assertEquals(1, rows.size(), "Standard 20 HP should produce exactly 1 row");
		String row0 = rows.get(0).getString();
		// Should contain 10 hearts
		assertEquals(10, row0.length(), "Row 0 should contain exactly 10 heart glyphs");
		assertFalse(row0.contains(HealthComponentBuilder.SPRITE_SPACER), "Full 10-heart row should not have spacers");
	}

	@Test
	void testAbsorptionHeartsProduceSecondRowStacked() {
		// 20 HP (10 red) + 4 absorption (2 golden hearts)
		HealthInfo info = HealthInfo.of(20.0f, 20.0f, 4.0f);
		List<Component> rows = HealthComponentBuilder.buildRows(info, HealthPreset.HEARTS, config);

		assertEquals(2, rows.size(), "10 red + 2 golden hearts should produce exactly 2 rows");
		
		String row0 = rows.get(0).getString();
		assertEquals(10, row0.length(), "Row 0 should contain 10 red hearts");

		String row1 = rows.get(1).getString();
		assertEquals(10, row1.length(), "Row 1 should be padded to 10 characters for left alignment");
		assertTrue(row1.startsWith(HealthComponentBuilder.SPRITE_ABS_FULL + HealthComponentBuilder.SPRITE_ABS_FULL),
				"Row 1 should begin with 2 golden absorption hearts");
		assertTrue(row1.contains(HealthComponentBuilder.SPRITE_SPACER),
				"Row 1 should contain invisible spacers for remaining slots");
	}

	@Test
	void testHighHealthProducesMultipleRowsWithoutMultiplier() {
		// 40 HP (20 red hearts, e.g. Enderman / Hoglin)
		HealthInfo info = HealthInfo.of(40.0f, 40.0f, 0.0f);
		List<Component> rows = HealthComponentBuilder.buildRows(info, HealthPreset.HEARTS, config);

		assertEquals(2, rows.size(), "40 HP should produce 2 rows of 10 hearts");
		assertEquals(10, rows.get(0).getString().length());
		assertEquals(10, rows.get(1).getString().length());
		assertFalse(rows.get(0).getString().contains("x2"), "Should never contain 'x2' multiplier");
		assertFalse(rows.get(1).getString().contains("x2"), "Should never contain 'x2' multiplier");
	}

	@Test
	void testThreeRowsForFiftyHealth() {
		// 50 HP (25 red hearts) with 60 max HP (30 containers, e.g. Warden/Wither/Iron Golem)
		HealthInfo info = HealthInfo.of(50.0f, 60.0f, 0.0f);
		List<Component> rows = HealthComponentBuilder.buildRows(info, HealthPreset.HEARTS, config);

		assertEquals(3, rows.size(), "30 max containers should produce 3 rows of 10");
		assertEquals(10, rows.get(0).getString().length(), "Row 0 should have 10 hearts");
		assertEquals(10, rows.get(1).getString().length(), "Row 1 should have 10 hearts");
		assertEquals(10, rows.get(2).getString().length(), "Row 2 should have 10 hearts");
	}

	@Test
	void testMaxHeartRowsCapping() {
		config.maxHeartRows = 2;
		// 60 HP (30 hearts) with maxHeartRows capped at 2
		HealthInfo info = HealthInfo.of(60.0f, 60.0f, 0.0f);
		List<Component> rows = HealthComponentBuilder.buildRows(info, HealthPreset.HEARTS, config);

		assertEquals(2, rows.size(), "Should be capped at maxHeartRows = 2");
	}

	@Test
	void testOtherPresetsProduceSingleRow() {
		HealthInfo info = HealthInfo.of(34.0f, 40.0f, 4.0f);

		List<Component> compact = HealthComponentBuilder.buildRows(info, HealthPreset.COMPACT, config);
		assertEquals(1, compact.size(), "Compact preset should produce 1 row");

		List<Component> bar = HealthComponentBuilder.buildRows(info, HealthPreset.BAR, config);
		assertEquals(1, bar.size(), "Bar preset should produce 1 row");

		List<Component> numeric = HealthComponentBuilder.buildRows(info, HealthPreset.NUMERIC, config);
		assertEquals(1, numeric.size(), "Numeric preset should produce 1 row");

		List<Component> lifesteal = HealthComponentBuilder.buildRows(info, HealthPreset.LIFESTEAL, config);
		assertEquals(1, lifesteal.size(), "Lifesteal preset should produce 1 row");
	}
}
