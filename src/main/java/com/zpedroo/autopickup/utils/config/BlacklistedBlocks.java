package com.zpedroo.autopickup.utils.config;

import com.zpedroo.autopickup.utils.FileUtils;
import org.bukkit.Material;

import java.util.List;

public class BlacklistedBlocks {

    public static final List<String> BLACKLISTED_BLOCKS = FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Blacklisted-Blocks");

    public static boolean isBlacklistedBlock(Material material) {
        return BLACKLISTED_BLOCKS.contains(material.toString());
    }
}