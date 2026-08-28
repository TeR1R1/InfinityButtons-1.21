package net.larsmans.infinitybuttons.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class DiamondSparkleParticle extends TextureSheetParticle {
    protected DiamondSparkleParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z);
        this.friction = 0.96f;
        this.quadSize *= 0.75f;
        this.hasPhysics = false;
        this.pickSprite(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        float fadeInAge = getLifetime() * 0.2f;
        float fadeOutAge = getLifetime() * 0.6f;
        if (age <= fadeInAge) {
            setAlpha(age / fadeInAge);
        } else if (age >= fadeOutAge) {
            setAlpha(1 - (age - fadeOutAge) / fadeOutAge);
        } else {
            setAlpha(1);
        }
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 0xF000F0;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Factory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            DiamondSparkleParticle particle = new DiamondSparkleParticle(level, x, y, z, this.sprites);
            particle.setLifetime(level.random.nextInt(30) + 10);
            particle.setAlpha(0);
            return particle;
        }
    }
}
