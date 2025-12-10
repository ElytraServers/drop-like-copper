# Drop Like Copper

![](src/main/resources/drop_like_copper.png)

Have you ever noticed that the Copper ores drops more Raw Copper? It always makes inventories filled with bunch of
coppers, which, in most cases, is useless.

## As Mod

Drop Like Copper changes the looting behavior of other ores, making them drop as much as coppers do.

## As Lib

By the way, Drop Like Copper can also be used as a library for looting stuff, lmk if you want more features.

Calling `DropLikeCopperAPI.setLibraryMode()` will disable the default behavior of Drop Like Copper.

It introduced few events for you to manipulate the looting table, like `LootingEvent.LootingPool`.
You can find an example in `cn.elytra.mod.drop_like_copper.internal.DropLikeCopperCore`, which is the default behavior.

And some handy delegate classes in `cn.elytra.mod.drop_like_copper.api.delegate` when manipulating them.
