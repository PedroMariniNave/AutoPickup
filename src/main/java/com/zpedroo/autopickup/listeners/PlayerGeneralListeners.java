package com.zpedroo.autopickup.listeners;

import com.zpedroo.autopickup.utils.config.BlacklistedBlocks;
import com.zpedroo.customenchants.api.BlockExplodeEvent;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PlayerGeneralListeners implements Listener {

    private final List<Material> FORTUNE_MATERIALS = Arrays.asList(
            Material.DIAMOND_ORE, Material.EMERALD_ORE,
            Material.COAL_ORE, Material.LAPIS_ORE
    );

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        Block block = event.getBlock();
        if (block == null || BlacklistedBlocks.isBlacklistedBlock(block.getType())) return;

        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) return;

        final ItemStack item = player.getItemInHand();
        if (block.getDrops().isEmpty()) return;

        pickupBlockDrops(player, block, item);
        if (item != null && !item.containsEnchantment(Enchantment.SILK_TOUCH)) {
            player.giveExp(event.getExpToDrop());
            event.setExpToDrop(0);
        }

        block.setType(Material.AIR);
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ItemStack item = event.getItem();

        pickupBlockDrops(player, block, item);
    }

    private void pickupBlockDrops(Player player, Block block, ItemStack item) {
        if (item != null && item.containsEnchantment(Enchantment.SILK_TOUCH)) {
            player.getInventory().addItem(new ItemStack(block.getType()));
        } else {
            for (ItemStack drop : block.getDrops()) {
                if (hasFortuneDrops(block.getType()) && item != null && item.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                    int level = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                    int multiplier = new Random().nextInt(level + 2) + 1;
                    drop.setAmount(drop.getAmount() * multiplier);
                }

                if (player.getInventory().firstEmpty() != -1) {
                    player.getInventory().addItem(drop);
                    continue;
                }

                player.getWorld().dropItemNaturally(player.getLocation(), drop);
            }
        }
    }

    private boolean hasFortuneDrops(Material type) {
        return FORTUNE_MATERIALS.contains(type);
    }
}