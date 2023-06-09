package dev.mayaqq.labyrinth.entities;

import dev.mayaqq.labyrinth.registry.ItemRegistry;
import dev.mayaqq.labyrinth.registry.materials.CustomMaterials;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.VirtualEntityUtils;
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment;
import eu.pb4.polymer.virtualentity.api.elements.BlockDisplayElement;
import eu.pb4.polymer.virtualentity.api.elements.DisplayElement;
import eu.pb4.polymer.virtualentity.api.elements.InteractionElement;
import eu.pb4.polymer.virtualentity.api.tracker.EntityTrackedData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public abstract class BaseProjectileEntity extends PersistentProjectileEntity implements PolymerEntity, Ownable {
    private static final TrackedData<Boolean> ENCHANTED;
    ItemStack projectileStack;
    private boolean dealtDamage;
    private final ToolMaterial material;
    private final TrackedData<ItemStack> ITEM;
    private final int slot;
    protected final ElementHolder holder = new ElementHolder() {
        @Override
        protected void notifyElementsOfPositionUpdate(Vec3d newPos, Vec3d delta) {
        }

        @Override
        protected void startWatchingExtraPackets(ServerPlayNetworkHandler player, Consumer<Packet<ClientPlayPacketListener>> packetConsumer) {
            packetConsumer.accept(new EntityPassengersSetS2CPacket(BaseProjectileEntity.this));
        }
    };
    private final EntityAttachment attachment;

    protected final DisplayElement mainDisplayElement = this.createMainDisplayElement();
    protected final InteractionElement interactionElement = InteractionElement.redirect(this);
    protected final InteractionElement interactionElement2 = InteractionElement.redirect(this);


    protected abstract DisplayElement createMainDisplayElement();

    protected Item materialToItem(ToolMaterial material) {
        HashMap<ToolMaterial, Item> materialToItem = new HashMap<>() {{
            // Placeholder
        }};
        return materialToItem.get(material);
    }

    protected BaseProjectileEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world, ItemStack stack, ToolMaterial material, int slot) {
        super(entityType, world);
        this.interactionElement.setSize(1f, 0.5f);
        this.interactionElement2.setSize(1f, -0.5f);

        this.holder.addElement(this.mainDisplayElement);
        this.holder.addElement(this.interactionElement);
        this.holder.addElement(this.interactionElement2);

        this.mainDisplayElement.setInterpolationDuration(entityType.getTrackTickInterval());

        this.mainDisplayElement.setTranslation(new org.joml.Vector3f(-0.5f, -0.5f, -0.5f));
        VirtualEntityUtils.addVirtualPassenger(this, this.mainDisplayElement.getEntityId());
        VirtualEntityUtils.addVirtualPassenger(this, this.interactionElement.getEntityId());
        VirtualEntityUtils.addVirtualPassenger(this, this.interactionElement2.getEntityId());
        this.attachment = new EntityAttachment(this.holder, this, false);
        this.projectileStack = new ItemStack(materialToItem(material));
        this.dataTracker.set(ENCHANTED, stack.hasGlint());
        this.material = material;
        this.ITEM = DataTracker.registerData(BaseProjectileEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
        this.dataTracker.set(ENCHANTED, stack.hasGlint());
        this.slot = slot;
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        super.tick();
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return this.dealtDamage ? null : super.getEntityCollision(currentPosition, nextPosition);
    }

    @Override
    public void setPosition(double x, double y, double z) {
        super.setPosition(x, y, z);
    }

    @Override
    public EntityType<?> getPolymerEntityType(ServerPlayerEntity player) {
        return EntityType.ARMOR_STAND;
    }

    @Override
    public void onEntityPacketSent(Consumer<Packet<?>> consumer, Packet<?> packet) {
        PolymerEntity.super.onEntityPacketSent(consumer, packet);
    }


    @Override
    public void modifyRawTrackedData(List<DataTracker.SerializedEntry<?>> data, ServerPlayerEntity player, boolean initial) {
        data.clear();
        if (initial) {
            data.add(DataTracker.SerializedEntry.of(EntityTrackedData.NO_GRAVITY, true));
            data.add(DataTracker.SerializedEntry.of(ArmorStandEntity.ARMOR_STAND_FLAGS, (byte) ArmorStandEntity.MARKER_FLAG));
            data.add(DataTracker.SerializedEntry.of(EntityTrackedData.SILENT, true));
            data.add(DataTracker.SerializedEntry.of(EntityTrackedData.FLAGS, (byte) (1 << EntityTrackedData.INVISIBLE_FLAG_INDEX)));
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ENCHANTED, false);
    }

    @Override
    protected ItemStack asItemStack() {
        return this.projectileStack.copy();
    }

    @Override
    public boolean canUsePortals() {
        return true;
    }
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        float f = materialToItem(material).getMaxDamage();
        if (entity instanceof LivingEntity livingEntity) {
            f += EnchantmentHelper.getAttackDamage(this.projectileStack, livingEntity.getGroup());
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
            player.getInventory().setStack(this.slot, this.projectileStack);
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
        if (nbt.contains("Item", 10)) {
            this.projectileStack = ItemStack.fromNbt(nbt.getCompound("Item"));
        }

        this.dealtDamage = nbt.getBoolean("DealtDamage");
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("Item", this.projectileStack.writeNbt(new NbtCompound()));
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

}
