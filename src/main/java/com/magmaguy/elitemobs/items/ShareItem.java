package com.magmaguy.elitemobs.items;

import com.magmaguy.elitemobs.api.EliteMobsItemDetector;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShareItem {

    public static void showOnChat(Player player) {
        if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            player.sendMessage("请将要分享的博格装备放在右手");
            return;
        }
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (!itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName() || !itemStack.getItemMeta().hasLore()) {
            player.sendMessage("你只能分享有名字的博格装备！");
            return;
        }

        String name = itemStack.getItemMeta().getDisplayName();

        TextComponent interactiveMessage = new TextComponent("<" + player.getDisplayName() + ">" + " §f[" + name + "§f]");
        setItemHoverEvent(interactiveMessage, itemStack);

        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers())
            onlinePlayer.spigot().sendMessage(interactiveMessage);
    }

    public static void setItemHoverEvent(TextComponent textComponent, ItemStack itemStack) {
        if (!EliteMobsItemDetector.isEliteMobsItem(itemStack))
            return;
        String stringList = itemStack.getItemMeta().getDisplayName();
        if (itemStack.getItemMeta().hasLore())
            for (String loreString : itemStack.getItemMeta().getLore())
                stringList += "\n" + loreString;
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(stringList).create()));
    }

}
