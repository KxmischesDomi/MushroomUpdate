package de.kxmischesdomi.mushroom.api;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public interface GlowColorable {

	default boolean hasGlowColor() {
		return getGlowColor() != 0x000000;
	}

	void setGlowColor(int glowColor);
	int getGlowColor();

}
