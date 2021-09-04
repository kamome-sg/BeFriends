package com.github.kamomesg.befriends;

import org.bukkit.entity.EntityType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum EntityTypeGroup {
    FRIENDLY_OVERWORLD(
            EntityType.BAT,
            EntityType.CAT,
            EntityType.CHICKEN,
            EntityType.COD,
            EntityType.COW,
            EntityType.DONKEY,
            EntityType.FOX,
            EntityType.HORSE,
            EntityType.MUSHROOM_COW,
            EntityType.MULE,
            EntityType.OCELOT,
            EntityType.PARROT,
            EntityType.PIG,
            EntityType.PUFFERFISH,
            EntityType.RABBIT,
            EntityType.SALMON,
            EntityType.SHEEP,
            EntityType.SKELETON_HORSE,
            EntityType.SNOWMAN,
            EntityType.SQUID,
            EntityType.TROPICAL_FISH,
            EntityType.TURTLE,
            EntityType.VILLAGER,
            EntityType.WANDERING_TRADER,
            EntityType.ZOMBIE_HORSE
    ),
    FRIENDLY_NETHER(
            EntityType.STRIDER
    ),
    FRIENDLY_THE_END(),
    HOSTILE_OVERWORLD(
            EntityType.CREEPER,
            EntityType.DROWNED,
            EntityType.ELDER_GUARDIAN,
            EntityType.EVOKER,
            EntityType.GUARDIAN,
            EntityType.HUSK,
            EntityType.PHANTOM,
            EntityType.PILLAGER,
            EntityType.RAVAGER,
            EntityType.SILVERFISH,
            EntityType.SLIME,
            EntityType.STRAY,
            EntityType.VEX,
            EntityType.VINDICATOR,
            EntityType.WITCH,
            EntityType.ZOMBIE,
            EntityType.ZOMBIE_VILLAGER
    ),
    HOSTILE_NETHER(
            EntityType.BLAZE,
            EntityType.GHAST,
            EntityType.HOGLIN,
            EntityType.MAGMA_CUBE,
            EntityType.PIGLIN_BRUTE,
            EntityType.SKELETON,
            EntityType.WITHER_SKELETON,
            EntityType.ZOGLIN
    ),
    HOSTILE_THE_END(
            EntityType.SHULKER
    ),
    NEUTRAL_OVERWORLD(
            EntityType.BEE,
            EntityType.CAVE_SPIDER,
            EntityType.DOLPHIN,
            EntityType.ENDERMAN,
            EntityType.IRON_GOLEM,
            EntityType.LLAMA,
            EntityType.PANDA,
            EntityType.POLAR_BEAR,
            EntityType.SPIDER,
            EntityType.TRADER_LLAMA,
            EntityType.WOLF
    ),
    NEUTRAL_NETHER(
            EntityType.ENDERMAN,
            EntityType.PIGLIN,
            EntityType.ZOMBIFIED_PIGLIN
    ),
    NEUTRAL_THE_END(
            EntityType.ENDERMAN
    ),
    FRIENDLY(
            EntityTypeGroup.FRIENDLY_OVERWORLD,
            EntityTypeGroup.FRIENDLY_NETHER,
            EntityTypeGroup.FRIENDLY_THE_END
    ),
    HOSTILE(
            EntityTypeGroup.HOSTILE_OVERWORLD,
            EntityTypeGroup.HOSTILE_NETHER,
            EntityTypeGroup.HOSTILE_THE_END
    ),
    NEUTRAL(
            EntityTypeGroup.NEUTRAL_OVERWORLD,
            EntityTypeGroup.NEUTRAL_NETHER,
            EntityTypeGroup.NEUTRAL_THE_END
    ),
    OVERWORLD(
            FRIENDLY_OVERWORLD,
            HOSTILE_OVERWORLD,
            NEUTRAL_OVERWORLD
    ),
    NETHER(
            FRIENDLY_NETHER,
            HOSTILE_NETHER,
            NEUTRAL_NETHER
    ),
    THE_END(
            FRIENDLY_THE_END,
            HOSTILE_THE_END,
            NEUTRAL_THE_END
    ),
    UNDEAD(
            EntityType.DROWNED,
            EntityType.HUSK,
            EntityType.PHANTOM,
            EntityType.SKELETON,
            EntityType.SKELETON_HORSE,
            EntityType.STRAY,
            EntityType.WITHER,
            EntityType.WITHER_SKELETON,
            EntityType.ZOGLIN,
            EntityType.ZOMBIE,
            EntityType.ZOMBIE_HORSE,
            EntityType.ZOMBIE_VILLAGER,
            EntityType.ZOMBIFIED_PIGLIN
    ),
    AQUATIC(
            EntityType.DOLPHIN,
            EntityType.SQUID,
            EntityType.GUARDIAN,
            EntityType.ELDER_GUARDIAN,
            EntityType.TURTLE,
            EntityType.COD,
            EntityType.SALMON,
            EntityType.PUFFERFISH,
            EntityType.TROPICAL_FISH
    ),
    ARTHROPOD(
            EntityType.BEE,
            EntityType.CAVE_SPIDER,
            EntityType.ENDERMITE,
            EntityType.SILVERFISH,
            EntityType.SPIDER
    ),
    ILLAGER(
            EntityType.PILLAGER,
            EntityType.ILLUSIONER,
            EntityType.EVOKER,
            EntityType.VINDICATOR,
            EntityType.RAVAGER
    ),
    BOSS(
            EntityType.ENDER_DRAGON, //ぶっちゃけこいつにはタゲられると思う
            EntityType.WITHER
    ),
    ALL(
            OVERWORLD,
            NETHER,
            THE_END,
            UNDEAD,
            AQUATIC,
            ARTHROPOD,
            ILLAGER,
            BOSS
    );

    final EntityType[] entityTypes;

    EntityTypeGroup() {
        this.entityTypes = new EntityType[]{};
    }

    EntityTypeGroup(EntityType... entityTypes) {
        Set<EntityType> entityTypeSet = new HashSet<>();
        Collections.addAll(entityTypeSet, entityTypes);
        this.entityTypes = entityTypeSet.toArray(new EntityType[entityTypeSet.size()]);
    }

    EntityTypeGroup(EntityTypeGroup... entityTypeGroups) {
        Set<EntityType> entityTypeSet = new HashSet<>();
        for (EntityTypeGroup entityTypeGroup : entityTypeGroups) {
            Collections.addAll(entityTypeSet, entityTypeGroup.entityTypes);
        }
        this.entityTypes = entityTypeSet.toArray(new EntityType[entityTypeSet.size()]);
    }

    public static EntityType[] getEntityTypes(String string) throws IllegalArgumentException {
        try {
            return new EntityType[]{EntityType.valueOf(string)};
        } catch (IllegalArgumentException e) {
            return valueOf(string).entityTypes;
        }
    }
}
