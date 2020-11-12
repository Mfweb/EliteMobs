package com.magmaguy.elitemobs.quests;

import com.magmaguy.elitemobs.adventurersguild.GuildRank;
import com.magmaguy.elitemobs.config.EconomySettingsConfig;
import com.magmaguy.elitemobs.config.menus.premade.QuestMenuConfig;
import com.magmaguy.elitemobs.economy.EconomyHandler;
import com.magmaguy.elitemobs.playerdata.PlayerData;

import org.bukkit.entity.Player;

import java.io.Serializable;

public class QuestReward implements Serializable {

    enum RewardType {
        MONETARY,
        ITEM,
        MIXED
    }

    private RewardType rewardType;
    private double questReward;

    public QuestReward(int questTier, double questDifficulty) {
        setRewardType();
        setQuestReward(questTier, questDifficulty);
    }

    private void setRewardType() {
        this.rewardType = RewardType.MONETARY;
    }

    private RewardType getRewardType() {
        return this.rewardType;
    }

    public double getQuestReward() {
        return this.questReward;
    }

    private void setQuestReward(int questTier, double questDifficulty) {
        this.questReward = questDifficulty;
    }

    public String getRewardMessage(Player player) {
        return (this.questReward * GuildRank.currencyBonusMultiplier(GuildRank.getGuildPrestigeRank(player))) + " " + EconomySettingsConfig.currencyName;
    }

    public void doReward(Player player) {
        if (rewardType.equals(RewardType.MONETARY)) {
            double c = this.questReward * GuildRank.currencyBonusMultiplier(GuildRank.getGuildPrestigeRank(player));
            PlayerData.addCurrencyDayCount(player.getUniqueId(), c);
            double r = PlayerData.getCurrencyDayRatio(player.getUniqueId());
            EconomyHandler.addCurrency(player.getUniqueId(), c * r);
            if(r < 1.0) {
                player.sendMessage("今日猎杀博格收益已达到上限，当前收益为:§c" + (r < 0.0001 ? "0.0%" : (String.format("%.2f", r * 100 - 0.005) + "%")));
            }
            player.sendMessage(QuestMenuConfig.rewardMessage
                    .replace("$reward", String.format("%.2f", c * r))
                    .replace("$currencyName", EconomySettingsConfig.currencyName));
        }
    }

}
