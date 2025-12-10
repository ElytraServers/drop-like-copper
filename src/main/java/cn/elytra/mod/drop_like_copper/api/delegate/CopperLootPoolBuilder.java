package cn.elytra.mod.drop_like_copper.api.delegate;

import cn.elytra.mod.drop_like_copper.mixins.LootPoolBuilderAccessor;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class CopperLootPoolBuilder implements LootPoolBuilderAccessor {

    protected final LootPool.Builder delegate;
    protected final LootPoolBuilderAccessor accessor;

    public final ArrayList<LootPoolEntryContainer> entries = new ArrayList<>();
    public final ArrayList<LootItemCondition> conditions = new ArrayList<>();
    public final ArrayList<LootItemFunction> functions = new ArrayList<>();

    protected CopperLootPoolBuilder(LootPool.Builder delegate) {
        this.delegate = delegate;
        this.accessor = (LootPoolBuilderAccessor) delegate;
    }

    public static CopperLootPoolBuilder empty() {
        return new CopperLootPoolBuilder(new LootPool.Builder());
    }

    public static CopperLootPoolBuilder fromExisting(LootPool pool) {
        return fromExisting(CopperLootPool.of(pool));
    }

    public static CopperLootPoolBuilder fromExisting(CopperLootPool pool) {
        CopperLootPoolBuilder builder = empty();
        builder.entries.addAll(pool.getEntries());
        builder.conditions.addAll(pool.getConditions());
        builder.functions.addAll(pool.getFunctions());
        return builder.setRolls(pool.getDelegate().getRolls())
            .setBonusRolls(pool.getDelegate().getBonusRolls())
            .setName(pool.getDelegate().getName());
    }

    public LootPool.Builder getDelegate() {
        return delegate;
    }

    public LootPool build() {
        // update the values
        setEntries(ImmutableList.<LootPoolEntryContainer>builder().addAll(entries));
        setConditions(ImmutableList.<LootItemCondition>builder().addAll(conditions));
        setFunctions(ImmutableList.<LootItemFunction>builder().addAll(functions));
        return delegate.build();
    }

    public CopperLootPoolBuilder setBonusRolls(NumberProvider bonusRolls) {
        delegate.setBonusRolls(bonusRolls);
        return this;
    }

    public CopperLootPoolBuilder setRolls(NumberProvider rolls) {
        delegate.setRolls(rolls);
        return this;
    }

    @SuppressWarnings("DataFlowIssue") // name can be null
    public CopperLootPoolBuilder setName(@Nullable String name) {
        delegate.name(name);
        return this;
    }

    public CopperLootPoolBuilder add(LootPoolEntryContainer.Builder<?> builder) {
        this.entries.add(builder.build());
        return this;
    }

    public CopperLootPoolBuilder when(LootItemCondition.Builder builder) {
        this.conditions.add(builder.build());
        return this;
    }

    public CopperLootPoolBuilder apply(LootItemFunction.Builder builder) {
        this.functions.add(builder.build());
        return this;
    }

    /**
     * @deprecated the value won't be updated until {@link #build()} is called.
     */
    @Deprecated
    @Override
    public ImmutableList.Builder<LootPoolEntryContainer> getEntries() {
        return accessor.getEntries();
    }

    @Override
    public void setEntries(ImmutableList.Builder<LootPoolEntryContainer> value) {
        accessor.setEntries(value);
    }

    /**
     * @deprecated the value won't be updated until {@link #build()} is called.
     */
    @Deprecated
    @Override
    public ImmutableList.Builder<LootItemCondition> getConditions() {
        return accessor.getConditions();
    }

    @Override
    public void setConditions(ImmutableList.Builder<LootItemCondition> value) {
        accessor.setConditions(value);
    }

    /**
     * @deprecated the value won't be updated until {@link #build()} is called.
     */
    @Deprecated
    @Override
    public ImmutableList.Builder<LootItemFunction> getFunctions() {
        return accessor.getFunctions();
    }

    @Override
    public void setFunctions(ImmutableList.Builder<LootItemFunction> value) {
        accessor.setFunctions(value);
    }
}
