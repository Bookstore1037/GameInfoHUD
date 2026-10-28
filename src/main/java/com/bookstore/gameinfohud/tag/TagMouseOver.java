package com.bookstore.gameinfohud.tag;

import com.bookstore.gameinfohud.tag.registry.TagRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public abstract class TagMouseOver extends Tag {

    @Override
    public String getCategory() {
        return "mouseover";
    }

    public static class Name extends TagMouseOver {

        @Override
        public String getValue() {
            HitResult hit = minecraft.hitResult;
            if (hit == null) return "";

            if (hit.getType() == HitResult.Type.ENTITY) {
                return ((EntityHitResult) hit).getEntity().getDisplayName().getString();
            } else if (hit.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHit = (BlockHitResult) hit;
                BlockPos pos = blockHit.getBlockPos();
                BlockState state = world.getBlockState(pos);
                Block block = state.getBlock();
                if (!state.isAir()) {
                    ItemStack pickBlock = block.getCloneItemStack(state, hit, world, pos, player);
                    if (pickBlock != null && !pickBlock.isEmpty()) {
                        return pickBlock.getHoverName().getString();
                    }
                    return block.getName().getString();
                }
            }
            return "";
        }
    }

    public static class UniqueName extends TagMouseOver {

        @Override
        public String getValue() {
            HitResult hit = minecraft.hitResult;
            if (hit == null) return "";

            if (hit.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult) hit).getEntity();
                ResourceLocation key = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
                return key != null ? key.toString() : "";
            } else if (hit.getType() == HitResult.Type.BLOCK) {
                Block block = world.getBlockState(((BlockHitResult) hit).getBlockPos()).getBlock();
                return String.valueOf(BuiltInRegistries.BLOCK.getKey(block));
            }
            return "";
        }
    }

    public static class Id extends TagMouseOver {

        @Override
        public String getValue() {
            HitResult hit = minecraft.hitResult;
            if (hit == null) return "0";

            if (hit.getType() == HitResult.Type.ENTITY) {
                return String.valueOf(((EntityHitResult) hit).getEntity().getId());
            } else if (hit.getType() == HitResult.Type.BLOCK) {
                Block block = world.getBlockState(((BlockHitResult) hit).getBlockPos()).getBlock();
                return String.valueOf(BuiltInRegistries.BLOCK.getId(block));
            }
            return "0";
        }
    }

    public static class Metadata extends TagMouseOver {

        @Override
        public String getValue() {
            return "0";
        }
    }

    public static class PowerWeak extends TagMouseOver {

        @Override
        public String getValue() {
            HitResult hit = minecraft.hitResult;
            if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = ((BlockHitResult) hit).getBlockPos();
                int power = -1;
                for (Direction side : Direction.values()) {
                    power = Math.max(power, world.getSignal(pos, side));
                    if (power >= 15) break;
                }
                return String.valueOf(power);
            }
            return "-1";
        }
    }

    public static class PowerStrong extends TagMouseOver {

        @Override
        public String getValue() {
            HitResult hit = minecraft.hitResult;
            if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = ((BlockHitResult) hit).getBlockPos();
                int power = -1;
                for (Direction side : Direction.values()) {
                    power = Math.max(power, world.getDirectSignal(pos, side));
                    if (power >= 15) break;
                }
                return String.valueOf(power);
            }
            return "-1";
        }
    }

    public static class PowerInput extends TagMouseOver {

        @Override
        public String getValue() {
            HitResult hit = minecraft.hitResult;
            if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = ((BlockHitResult) hit).getBlockPos();
                int power = -1;
                for (Direction side : Direction.values()) {
                    power = Math.max(power, world.getSignal(pos, side));
                    if (power >= 15) break;
                }
                return String.valueOf(power);
            }
            return "-1";
        }
    }

    public static void register() {
        TagRegistry.INSTANCE.register(new Name().setName("mouseovername"));
        TagRegistry.INSTANCE.register(new UniqueName().setName("mouseoveruniquename"));
        TagRegistry.INSTANCE.register(new Id().setName("mouseoverid"));
        TagRegistry.INSTANCE.register(new Metadata().setName("mouseovermetadata"));
        TagRegistry.INSTANCE.register(new PowerWeak().setName("mouseoverpowerweak"));
        TagRegistry.INSTANCE.register(new PowerStrong().setName("mouseoverpowerstrong"));
        TagRegistry.INSTANCE.register(new PowerInput().setName("mouseoverpowerinput"));
    }
}
