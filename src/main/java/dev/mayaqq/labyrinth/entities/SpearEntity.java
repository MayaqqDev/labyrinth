package dev.mayaqq.labyrinth.entities;

import dev.mayaqq.labyrinth.registry.EntityRegistry;
import dev.mayaqq.labyrinth.registry.ItemRegistry;
import dev.mayaqq.labyrinth.registry.materials.CustomMaterials;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import net.minecraft.block.Blocks;
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
        put(CustomMaterials.IRON, ItemRegistry.IRON_SPEAR);
        put(CustomMaterials.GOLD, ItemRegistry.GOLDEN_SPEAR);
        put(CustomMaterials.DIAMOND, ItemRegistry.DIAMOND_SPEAR);
        put(CustomMaterials.NETHERITE, ItemRegistry.NETHERITE_SPEAR);
    }};

    public SpearEntity(World world, LivingEntity owner, ItemStack stack, ToolMaterial material, int slot) {
        super(EntityRegistry.SPEAR, owner, world);
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

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ENCHANTED, false);
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

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return this.dealtDamage ? null : super.getEntityCollision(currentPosition, nextPosition);
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        float f = materialToItem.get(material).getMaxDamage();
        if (entity instanceof LivingEntity livingEntity) {
            f += EnchantmentHelper.getAttackDamage(this.spearStack, livingEntity.getGroup());
        }

        Entity entity2 = this.getOwner();
        DamageSource damageSource = this.getDamageSources().trident(this, (entity2 == null ? this : entity2));
        this.dealtDamage = true;
        SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_HIT;
        if (entity.damage(damageSource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity2 = (LivingEntity)entity;
                if (entity2 instanceof LivingEntity) {
                    EnchantmentHelper.onUserDamaged(livingEntity2, entity2);
                    EnchantmentHelper.onTargetDamaged((LivingEntity)entity2, livingEntity2);
                }

                this.onHit(livingEntity2);
            }
        }

        this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));
        float g = 1.0F;

        this.playSound(soundEvent, g, 1.0F);
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
        if (nbt.contains("Spear", 10)) {
            this.spearStack = ItemStack.fromNbt(nbt.getCompound("Spear"));
        }

        this.dealtDamage = nbt.getBoolean("DealtDamage");
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("Spear", this.spearStack.writeNbt(new NbtCompound()));
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