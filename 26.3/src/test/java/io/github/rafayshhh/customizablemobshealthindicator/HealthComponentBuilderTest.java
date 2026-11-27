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
		config.showAbsorption = true;
	}

	@Test
	void testSmallMobProducesExactHearts() {
		// Chicken: 4 HP (2 hearts) -> must show exactly 2 hearts, not 10 containers!
		HealthInfo chicken = HealthInfo.of(4.0f, 4.0f, 0.0f);
		List<Component> chickenRows = HealthComponentBuilder.buildRows(chicken, HealthPreset.HEARTS, config);
		assertEquals(1, chickenRows.size(), "Chicken should produce 1 row");
		assertEquals(2, chickenRows.get(0).getString().length(), "Chicken should have exactly 2 hearts, not 10");

		// Cow / Sheep / Pig: 10 HP (5 hearts) -> must show exactly 5 hearts, not 10 containers!
		HealthInfo cow = HealthInfo.of(10.0f, 10.0f, 0.0f);
		List<Component> cowRows = HealthComponentBuilder.buildRows(cow, HealthPreset.HEARTS, config);
		assertEquals(1, cowRows.size(), "Cow should produce 1 row");
		assertEquals(5, cowRows.get(0).getString().length(), "Cow should have exactly 5 hearts, not 10");

		// Spider: 16 HP (8 hearts) -> must show exactly 8 hearts, not 10 containers!
		HealthInfo spider = HealthInfo.of(16.0f, 16.0f, 0.0f);
		List<Component> spiderRows = HealthComponentBuilder.buildRows(spider, HealthPreset.HEARTS, config);
		assertEquals(1, spiderRows.size(), "Spider should produce 1 row");
		assertEquals(8, spiderRows.get(0).getString().length(), "Spider should have exactly 8 hearts, not 10");
	}

	@Test
	void testInjuredMobShowsCorrectContainers() {
		// Injured Cow: 6 HP remaining out of 10 max HP (5 total containers)
		// Should show 3 full hearts + 2 empty containers = 5 total glyphs (NOT 10!)
		HealthInfo injuredCow = HealthInfo.of(6.0f, 10.0f, 0.0f);
		List<Component> rows = HealthComponentBuilder.buildRows(injuredCow, HealthPreset.HEARTS, config);

		assertEquals(1, rows.size(), "Injured cow should produce 1 row");
		String row0 = rows.get(0).getString();
		assertEquals(5, row0.length(), "Injured cow should have exactly 5 total containers, not 10");
		assertTrue(row0.startsWith(HealthComponentBuilder.SPRITE_FULL + HealthComponentBuilder.SPRITE_FULL + HealthComponentBuilder.SPRITE_FULL),
				"Row should start with 3 full hearts");
		assertTrue(row0.endsWith(HealthComponentBuilder.SPRITE_CONTAINER + HealthComponentBuilder.SPRITE_CONTAINER),
				"Row should end with 2 empty containers");
	}

	@Test
	void testStandardMobProducesTenHearts() {
		// Zombie / Skeleton: 20 HP (10 hearts)
		HealthInfo zombie = HealthInfo.of(20.0f, 20.0f, 0.0f);
		List<Component> rows = HealthComponentBuilder.buildRows(zombie, HealthPreset.HEARTS, config);

		assertEquals(1, rows.size(), "Zombie should produce 1 row");
		assertEquals(10, rows.get(0).getString().length(), "Zombie should have 10 hearts");
	}

	@Test
	void testHighHealthProducesAllRowsWithoutCap() {
		// Enderman / Hoglin: 40 HP (20 red hearts) -> 2 rows of 10 hearts
		HealthInfo enderman = HealthInfo.of(40.0f, 40.0f, 0.0f);
		List<Component> endermanRows = HealthComponentBuilder.buildRows(enderman, HealthPreset.HEARTS, config);
		assertEquals(2, endermanRows.size(), "40 HP should produce 2 rows of 10 hearts");
		assertEquals(10, endermanRows.get(0).getString().length());
		assertEquals(10, endermanRows.get(1).getString().length());

		// Iron Golem / Ravager: 100 HP (50 red hearts) -> all 5 rows of 10 hearts without capping!
		HealthInfo golem = HealthInfo.of(100.0f, 100.0f, 0.0f);
		List<Component> golemRows = HealthComponentBuilder.buildRows(golem, HealthPreset.HEARTS, config);
		assertEquals(5, golemRows.size(), "Iron Golem (100 HP) should produce all 5 rows of 10 hearts without capping");
		for (int i = 0; i < 5; i++) {
			assertEquals(10, golemRows.get(i).getString().length(), "Row " + i + " should have 10 hearts");
		}
	}

	@Test
	void testBossHealthProducesAllRowsWithoutCap() {
		// Wither: 300 HP (150 red hearts) -> all 15 rows of 10 hearts without capping!
		HealthInfo wither = HealthInfo.of(300.0f, 300.0f, 0.0f);
		List<Component> witherRows = HealthComponentBuilder.buildRows(wither, HealthPreset.HEARTS, config);
		assertEquals(15, witherRows.size(), "Wither (300 HP) should produce all 15 rows without capping");
		for (int i = 0; i < 15; i++) {
			assertEquals(10, witherRows.get(i).getString().length(), "Row " + i + " should have 10 hearts");
		}
	}

	@Test
	void testAbsorptionHeartsProduceSecondRowNaturally() {
		// 20 HP (10 red) + 4 absorption (2 golden hearts)
		HealthInfo info = HealthInfo.of(20.0f, 20.0f, 4.0f);
		List<Component> rows = HealthComponentBuilder.buildRows(info, HealthPreset.HEARTS, config);

		assertEquals(2, rows.size(), "10 red + 2 golden hearts should produce exactly 2 rows");
		assertEquals(10, rows.get(0).getString().length(), "Row 0 should contain 10 red hearts");
		assertEquals(2, rows.get(1).getString().length(), "Row 1 should contain exactly 2 golden hearts without padding spacers");
		assertTrue(rows.get(1).getString().startsWith(HealthComponentBuilder.SPRITE_ABS_FULL + HealthComponentBuilder.SPRITE_ABS_FULL),
				"Row 1 should begin with 2 golden absorption hearts");
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
