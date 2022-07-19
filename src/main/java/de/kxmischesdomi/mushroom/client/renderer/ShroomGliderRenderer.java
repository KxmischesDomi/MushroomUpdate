package de.kxmischesdomi.mushroom.client.renderer;

import de.kxmischesdomi.mushroom.client.model.ShroomGliderModel;
import de.kxmischesdomi.mushroom.item.ShroomGlider;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ShroomGliderRenderer extends GeoArmorRenderer<ShroomGlider> {

	public ShroomGliderRenderer() {
		super(new ShroomGliderModel());
		this.headBone = "shroom_hat";
		this.bodyBone = null;
		this.rightArmBone = null;
		this.leftArmBone = null;
		this.rightLegBone = null;
		this.leftLegBone = null;
		this.rightBootBone = null;
		this.leftBootBone = null;
	}

}
