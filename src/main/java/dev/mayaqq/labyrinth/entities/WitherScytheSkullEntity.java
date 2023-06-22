package dev.mayaqq.labyrinth.entities;

import dev.mayaqq.labyrinth.registry.EntityRegistry;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class WitherScytheSkullEntity extends ExplosiveProjectileEntity implements PolymerEntity {
    private static final TrackedData<Boolean> CHARGED;

    public WitherScytheSkullEntity(EntityType<? extends WitherScytheSkullEntity> entityType, World world) {
        super(entityType, world);
    }

    public WitherScytheSkullEntity(World world, LivingEntity owner, double directionX, double directionY, double directionZ) {
        super(EntityRegistry.WITHER_SCYTHE_SKULL, owner, directionX, directionY, directionZ, world);
    }

    protected float getDrag() {
        return this.isCharged() ? 0.73F : super.getDrag();
    }

    public boolean isOnFire() {
        return false;
    }

    public float getEffectiveExplosionResistance(Explosion explosion, BlockView world, BlockPos pos, BlockState blockState, FluidState fluidState, float max) {
        return this.isCharged() && WitherEntity.canDestroy(blockState) ? Math.min(0.8F, max) : max;
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (!this.getWorld().isClient) {
            Entity entity = entityHitResult.getEntity();
            Entity entity2 = this.getOwner();
            boolean bl;
            LivingEntity livingEntity;
            if (entity2 instanceof LivingEntity) {
                livingEntity = (LivingEntity)entity2;
                bl = entity.damage(this.getDamageSources().playerAttack((PlayerEntity) getOwner()), 8.0F);
                if (bl) {
                    if (entity.isAlive()) {
                        this.applyDamageEffects(livingEntity, entity);
                    } else {
                        livingEntity.heal(5.0F);
                    }
                }
            } else {
                bl = entity.damage(this.getDamageSources().magic(), 5.0F);
            }

            if (bl && entity instanceof LivingEntity) {
                livingEntity = (LivingEntity)entity;
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 1), this.getOwner());
            }

        }
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 1.0F, false, World.ExplosionSourceType.NONE);
            this.discard();
        }

    }

    public boolean canHit() {
        return false;
    }

    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    protected void initDataTracker() {
        this.dataTracker.startTracking(CHARGED, false);
    }

    public boolean isCharged() {
        return (Boolean)this.dataTracker.get(CHARGED);
    }

    public void setCharged(boolean charged) {
        this.dataTracker.set(CHARGED, charged);
    }

    protected boolean isBurning() {
        return false;
    }

    static {
        CHARGED = DataTracker.registerData(WitherSkullEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    @Override
    public EntityType<?> getPolymerEntityType(ServerPlayerEntity player) {
        return EntityType.WITHER_SKULL;
    }
}
