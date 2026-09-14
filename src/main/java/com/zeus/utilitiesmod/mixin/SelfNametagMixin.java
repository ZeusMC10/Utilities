package com.zeus.utilitiesmod.mixin;

import com.zeus.utilitiesmod.UtilitiesClient;
import com.zeus.utilitiesmod.feature.Feature;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class SelfNametagMixin {

	@Inject(
			method = "shouldShowName",
			at = @At("RETURN"),
			cancellable = true
	)
	private void utilities$showSelfNametag(
			LivingEntity entity,
			double distanceToCameraSq,
			CallbackInfoReturnable<Boolean> cir
	) {
		if (cir.getReturnValue()) {
			return;
		}

		Minecraft minecraft = Minecraft.getInstance();
		LocalPlayer player = minecraft.player;

		if (player == null) {
			return;
		}

		if (entity != player) {
			return;
		}

		Feature selfNametag =
				UtilitiesClient.FEATURE_MANAGER.getFeature("Self-Nametag");

		if (selfNametag != null && selfNametag.isEnabled()) {
			cir.setReturnValue(true);
		}
	}
}