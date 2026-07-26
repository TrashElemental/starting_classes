package net.trashelemental.starting_classes.util;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.trashelemental.starting_classes.class_system.ClassBlacklist;

public class ClassBlacklistReloadListener extends SimplePreparableReloadListener<Void> {

    @Override
    protected Void prepare(ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        return null;
    }

    @Override
    protected void apply(Void pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        ClassBlacklist.load(pResourceManager);
    }

    @Override
    public String getName() {
        return "Starting Classes Blacklist";
    }
}
