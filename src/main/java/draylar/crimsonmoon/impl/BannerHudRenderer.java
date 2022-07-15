package draylar.crimsonmoon.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import static draylar.crimsonmoon.CrimsonMoonClient.*;

/**
 * Responsible for rendering the Crimson Moon drop-down banner, which appears when {@link draylar.crimsonmoon.CrimsonMoonClient#hasBanner} is {@code true}.
 */
public class BannerHudRenderer implements HudRenderCallback {

    @Override
    public void onHudRender(MatrixStack matrices, float tickDelta) {
        if(hasBanner) {
            float lerpedTicks = MathHelper.lerp(tickDelta, bannerTicks - 1, bannerTicks);

            matrices.push();
            matrices.translate(0, -100, 0);

            // Move the banner down in the first second, and back up in the last second.
            if(bannerTicks < 20) {
                matrices.translate(0, 100 * (-Math.cos(Math.PI * (lerpedTicks / 20f)) + 1) / 2, 0); // 0 -> 1
            } else if(bannerTicks > 180) {
                matrices.translate(0, 100 - 100 * ((lerpedTicks - 180) / 20f), 0); // 1 -> 0
            } else {
                matrices.translate(0, 100, 0);
            }

            // Render underlying banner texture
            float width = MinecraftClient.getInstance().getWindow().getScaledWidth() / 2f;
            matrices.push();
            matrices.scale(2, 2, 2);
            matrices.translate(width / 2 - 78 / 2, 1, 0);
            RenderSystem.setShaderTexture(0, BANNER_TEXTURE);
            DrawableHelper.drawTexture(matrices, 0, 0, 0, 0, 78, 20, 78, 20);
            matrices.pop();

            // Render text on top of the banner
            MutableText crimson_moon = Text.literal("Crimson Moon").setStyle(Style.EMPTY.withBold(true));
            MutableText text = Text.literal("The grounds tremble...").setStyle(Style.EMPTY.withBold(true));
            int titleWidth = MinecraftClient.getInstance().textRenderer.getWidth(crimson_moon);
            int hintWidth = MinecraftClient.getInstance().textRenderer.getWidth(text);
            MinecraftClient.getInstance().textRenderer.draw(matrices, crimson_moon, width - titleWidth / 2f, 10, 0x5F0713);
            MinecraftClient.getInstance().textRenderer.draw(matrices, text, width - hintWidth / 2f, 20, 0x75160c);

            matrices.pop();
        } else {
            bannerTicks = 0;
        }
    }
}
