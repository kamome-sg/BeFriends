package com.github.kamomesg.befriends;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import java.nio.charset.StandardCharsets;
import java.util.*;

public final class BeFriends extends JavaPlugin implements Listener, CommandExecutor, TabCompleter {

    final NamespacedKey KEY = new NamespacedKey(this, "friends");
    final StringArrayItemTagType DATA_TYPE = new StringArrayItemTagType(StandardCharsets.UTF_8);

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("befriends").setExecutor(this);
        getCommand("befriends").setTabCompleter(this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "コンソールからコマンドを実行することはできません");
            return true;
        }

        Player player = (Player) sender;
        PersistentDataContainer friendsData = player.getPersistentDataContainer();

        if (command.getName().equalsIgnoreCase("befriends")) {
            // /befriends
            if (args.length == 0) {
                if (friendsData.has(KEY, DATA_TYPE) && !Arrays.asList(friendsData.get(KEY, DATA_TYPE)).isEmpty()) {
                    player.sendMessage(player.getName() + " のフレンド:");
                    player.sendMessage(String.join(", ", friendsData.get(KEY, DATA_TYPE)));
                } else {
                    player.sendMessage(player.getName() + " にはフレンドがいません");
                }
                return true;
            }

            // /befriends remove
            else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("remove")) {
                    if (friendsData.has(KEY, DATA_TYPE) && !Arrays.asList(friendsData.get(KEY, DATA_TYPE)).isEmpty()) {
                        friendsData.remove(KEY);
                        player.sendMessage(ChatColor.GREEN + player.getName() + " のフレンドを削除しました");
                    } else {
                        player.sendMessage(ChatColor.RED + player.getName() + " にはフレンドがいません");
                    }
                    return true;
                }
            }

            // /befriends <add/remove> <entity name OR group>
            else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("add")) {
                    try {
                        //プレイヤーのfriendsDataからfriendsSet作成
                        Set<String> friendsSet = new HashSet<>();
                        if (friendsData.has(KEY, DATA_TYPE))
                            Collections.addAll(friendsSet, friendsData.get(KEY, DATA_TYPE));
                        //friendsSetにentityTypesを追加 friendsDataに保存
                        EntityType[] entityTypes = EntityTypeGroup.getEntityTypes(args[1].toUpperCase());
                        if (!containsCompletely(friendsSet, getEntityNames(entityTypes))) {
                            Collections.addAll(friendsSet, getEntityNames(entityTypes));
                        } else {
                            player.sendMessage(ChatColor.RED + player.getName() + " と " + String.join(", ", getEntityNames(entityTypes)) + " は既にフレンドです");
                            return true;
                        }
                        friendsData.set(KEY, DATA_TYPE, friendsSet.toArray(new String[friendsSet.size()]));
                        player.sendMessage(ChatColor.GREEN + player.getName() + " と " + String.join(", ", getEntityNames(entityTypes)) + " がフレンドになりました");
                    } catch (IllegalArgumentException e) {
                        player.sendMessage(ChatColor.RED + args[1] + " は正しいエンティティ名、もしくはグループ名ではありません");
                    }
                    return true;
                }

                else if (args[0].equalsIgnoreCase("remove")) {
                    try {
                        //プレイヤーのfriendsDataからfriendsSet作成
                        Set<String> friendsSet = new HashSet<>();
                        if (friendsData.has(KEY, DATA_TYPE))
                            Collections.addAll(friendsSet, friendsData.get(KEY, DATA_TYPE));
                        //friendsSetからentityTypesを削除 friendsDataに保存
                        EntityType[] entityTypes = EntityTypeGroup.getEntityTypes(args[1].toUpperCase());
                        if (containsPartially(friendsSet, getEntityNames(entityTypes))) {
                            removeArraysFromSet(friendsSet, getEntityNames(entityTypes));
                        } else {
                            player.sendMessage(ChatColor.RED + player.getName() + " と " + String.join(", ", getEntityNames(entityTypes)) + " はフレンドではありません");
                            return true;
                        }
                        friendsData.set(KEY, DATA_TYPE, friendsSet.toArray(new String[friendsSet.size()]));
                        player.sendMessage(ChatColor.GREEN + player.getName() + " と " + String.join(", ", getEntityNames(entityTypes)) + " はフレンドではなくなりました");
                    } catch (IllegalArgumentException e) {
                        player.sendMessage(ChatColor.RED + args[1] + " は正しいエンティティ名、もしくはグループ名ではありません");
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        Set<String> commands = new HashSet<>();

        if (args.length == 1) {
            Collections.addAll(commands,"add", "remove");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add")) {
                for (EntityType entityType : EntityTypeGroup.ALL.entityTypes) {
                    commands.add(entityType.toString().toLowerCase());
                }
                for (EntityTypeGroup entityTypeGroup : EntityTypeGroup.values()) {
                    commands.add(entityTypeGroup.toString().toLowerCase());
                }
            }
            else if (args[0].equalsIgnoreCase("remove") && sender instanceof Player) {
                Player player = (Player) sender;
                PersistentDataContainer friendsData = player.getPersistentDataContainer();
                if (friendsData.has(KEY, DATA_TYPE))
                    Collections.addAll(commands, friendsData.get(KEY, DATA_TYPE));
                    toLowerCase(commands);
                for (EntityTypeGroup entityTypeGroup : EntityTypeGroup.values()) {
                    commands.add(entityTypeGroup.toString().toLowerCase());
                }
            }
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        PersistentDataContainer friendsData = e.getPlayer().getPersistentDataContainer();
        if (!friendsData.has(KEY, DATA_TYPE))
            friendsData.set(KEY, DATA_TYPE, new String[0]);
    }

    @EventHandler
    public void onTarget(EntityTargetEvent e) {
        if (e.getTarget() instanceof Player) {
            //親しき仲にも礼儀ありって言うよね
            if (e.getReason() == EntityTargetEvent.TargetReason.CLOSEST_PLAYER ||
                e.getReason() == EntityTargetEvent.TargetReason.TARGET_ATTACKED_NEARBY_ENTITY) {
                Player player = (Player) e.getTarget();
                EntityType entity = e.getEntityType();
                PersistentDataContainer friendsData = player.getPersistentDataContainer();
                Set<String> friendsSet = new HashSet<>();
                if (friendsData.has(KEY, DATA_TYPE)) {
                    Collections.addAll(friendsSet, friendsData.get(KEY, DATA_TYPE));
                } else {
                    return;
                }
                if (friendsSet.contains(entity.toString().toLowerCase()))
                    e.setCancelled(true);
            }
        }
    }

    <T> Boolean containsCompletely(Set<T> set, T[] array) {
        for (T t : array) {
            if (!set.contains(t))
                return false;
        }
        return true;
    }

    <T> Boolean containsPartially(Set<T> set, T[] array) {
        for (T t : array) {
            if (set.contains(t))
                return true;
        }
        return false;
    }

    <T> void removeArraysFromSet(Set<T> set, T[] array) {
        for (T t : array) {
            set.remove(t);
        }
    }

    String[] getEntityNames(EntityType[] entityTypes) {
        Set<String> entityList = new LinkedHashSet<>();
        for (EntityType entityType : entityTypes) {
            entityList.add(entityType.toString().toLowerCase());
        }
        return entityList.toArray(new String[entityList.size()]);
    }

    void toLowerCase(Set<String> set) {
        Set<String> lowerSet = new HashSet<>();
        for (String string : set) {
            lowerSet.add(string.toLowerCase());
        }
        set.clear();
        set.addAll(lowerSet);
    }
}
