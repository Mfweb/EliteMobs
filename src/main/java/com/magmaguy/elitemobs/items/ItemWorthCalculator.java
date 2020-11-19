package com.magmaguy.elitemobs.items;

import com.magmaguy.elitemobs.adventurersguild.GuildRank;
import com.magmaguy.elitemobs.config.EconomySettingsConfig;
import com.magmaguy.elitemobs.config.enchantments.EnchantmentsConfig;
import com.magmaguy.elitemobs.items.customenchantments.CustomEnchantment;
import com.magmaguy.elitemobs.items.potioneffects.ElitePotionEffect;
import com.magmaguy.elitemobs.items.potioneffects.ElitePotionEffectContainer;
import com.magmaguy.elitemobs.utils.Round;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.magmaguy.elitemobs.config.EconomySettingsConfig.resaleValue;

public class ItemWorthCalculator {

    public static double determineItemWorth(ItemStack itemStack, Player player) {

        double prestigeMultiplier;
        if (player == null)
            prestigeMultiplier = 1;
        else
            prestigeMultiplier = GuildRank.currencyBonusMultiplier(GuildRank.getGuildPrestigeRank(player));

        return 1 + Round.twoDecimalPlaces(
                (EconomySettingsConfig.getMaterialWorth(itemStack.getType()) +
                        getAllEnchantmentValues(itemStack) +
                        getAllPotionEffectValues(itemStack.getItemMeta())) *
                        prestigeMultiplier);

    }

    public static double determineResaleWorth(ItemStack itemStack, Player player) {

        double resaleWorth = Round.twoDecimalPlaces(determineItemWorth(itemStack, player) * (resaleValue / 100));

        return resaleWorth;

    }

    private static double getAllEnchantmentValues(ItemStack itemStack) {
        double value = 0;
        for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
            if (EliteEnchantments.isPotentialEliteEnchantment(enchantment)) {
                if (ItemTagger.getEnchantment(itemStack.getItemMeta(), enchantment.getKey()) != 0) {
                    value += EnchantmentsConfig.getEnchantment(enchantment).getValue() * ItemTagger.getEnchantment(itemStack.getItemMeta(), enchantment.getKey());
                    continue;
                }
            } else
                value += EnchantmentsConfig.getEnchantment(enchantment).getValue() * itemStack.getEnchantments().get(enchantment);
        }
        for (CustomEnchantment customEnchantment : CustomEnchantment.getCustomEnchantments())
            value += customEnchantment.getEnchantmentsConfigFields().getValue() * ItemTagger.getEnchantment(itemStack.getItemMeta(), customEnchantment.key);
        return value;
    }

    private static double getAllPotionEffectValues(ItemMeta itemMeta) {
        double value = 0;
        for (ElitePotionEffect elitePotionEffect : ElitePotionEffectContainer.getElitePotionEffects(itemMeta, ElitePotionEffect.ApplicationMethod.CONTINUOUS))
            value += (elitePotionEffect.getValue() * (elitePotionEffect.getPotionEffect().getAmplifier() + 1));
        for (ElitePotionEffect elitePotionEffect : ElitePotionEffectContainer.getElitePotionEffects(itemMeta, ElitePotionEffect.ApplicationMethod.ONHIT))
            value += (elitePotionEffect.getValue() * (elitePotionEffect.getPotionEffect().getAmplifier() + 1));
        return value;
    }

}
