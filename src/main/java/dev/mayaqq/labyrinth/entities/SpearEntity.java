package dev.mayaqq.labyrinth.entities;

import dev.mayaqq.labyrinth.registry.LabyrinthEntities;
import dev.mayaqq.labyrinth.registry.LabyrinthItems;
import dev.mayaqq.labyrinth.registry.materials.CustomMaterials;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class SpearEntity extends PersistentProjectileEntity implements PolymerEntity {
    private static final TrackedData<Boolean> ENCHANTED;
    private ItemStack spearStack;
    private boolean dealtDamage;
    private final ToolMaterial material;
    private final TrackedData<ItemStack> ITEM;
    private final int slot;
    private HashMap<ToolMaterial, Item> materialToItem = new HashMap<>() {{
        put(CustomMaterials.IRON, LabyrinthItems.IRON_SPEAR);
        put(CustomMaterials.GOLD, LabyrinthItems.GOLDEN_SPEAR);
        put(CustomMaterials.DIAMOND, LabyrinthItems.DIAMOND_SPEAR);
        put(CustomMaterials.NETHERITE, LabyrinthItems.NETHERITE_SPEAR);
    }};

    public SpearEntity(World world, LivingEntity owner, ItemStack stack, ToolMaterial material, int slot) {
        super(LabyrinthEntities.SPEAR, 0, 0, 0, world, stack, stack);
        this.spearStack = new ItemStack(materialToItem.get(material));
        this.spearStack = stack.copy();
        this.dataTracker.set(ENCHANTED, stack.hasGlint());
        this.material = material;
        this.ITEM = DataTracker.registerData(SpearEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
        this.slot = slot;
    }

    public SpearEntity(EntityType<SpearEntity> entityType, World world, ToolMaterial material, int slot) {
        super(entityType, world);
        this.spearStack = new ItemStack(materialToItem.get(material));
        this.material = material;
        this.ITEM = DataTracker.registerData(SpearEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
        this.slot = slot;
    }

    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ENCHANTED, false);
    }

    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        super.tick();
    }

    protected ItemStack asItemStack() {
        return this.spearStack.copy();
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(materialToItem.get(material));
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return this.dealtDamage ? null : super.getEntityCollision(currentPosition, nextPosition);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        ServerWorld serverWorld;
        Entity entity = entityHitResult.getEntity();
        float f = material.getAttackDamage();
        Entity entity2 = this.getOwner();
        DamageSource damageSource = this.getDamageSources().trident(this, entity2 == null ? this : entity2);
        World world = this.getWorld();
        if (world instanceof ServerWorld) {
            serverWorld = (ServerWorld) world;
            f = EnchantmentHelper.getDamage(serverWorld, this.getWeaponStack(), entity, damageSource, f);
        }
        this.dealtDamage = true;
        if (entity.damage(damageSource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            world = this.getWorld();
            if (world instanceof ServerWorld) {
                serverWorld = (ServerWorld) world;
                EnchantmentHelper.onTargetDamaged(serverWorld, entity, damageSource, this.getWeaponStack());
            }
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                this.knockback(livingEntity, damageSource);
                this.onHit(livingEntity);
            }
        }
        this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));
    }

    protected boolean tryPickup(PlayerEntity player) {
        if (player.getInventory().getStack(this.slot).getItem() == Items.AIR && this.isOwner(player)) {
            player.getInventory().setStack(this.slot, this.spearStack);
            return true;
        } else {
            return super.tryPickup(player) || this.isNoClip() && this.isOwner(player) && player.getInventory().insertStack(this.asItemStack());
        }
    }

    protected SoundEvent getHitSound() {
        return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
    }

    public void onPlayerCollision(PlayerEntity player) {
        if (this.isOwner(player) || this.getOwner() == null) {
            super.onPlayerCollision(player);
        }

    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dealtDamage = nbt.getBoolean("DealtDamage");
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("DealtDamage", this.dealtDamage);
    }

    public void age() {
        if (this.pickupType != PickupPermission.ALLOWED) {
            super.age();
        }
    }

    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    static {
        ENCHANTED = DataTracker.registerData(SpearEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    @Override
    public EntityType<?> getPolymerEntityType(ServerPlayerEntity player) {
        return EntityType.SPECTRAL_ARROW;
    }
}