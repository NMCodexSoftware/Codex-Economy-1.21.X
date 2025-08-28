package org.codex.codexeconomy;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.codex.codexeconomy.block.ModBlocks;
import org.codex.codexeconomy.data.EconomyState;
import org.codex.codexeconomy.item.ModItemGroups;
import org.codex.codexeconomy.item.ModItems;
import org.codex.codexeconomy.network.PlayerDataSyncS2CPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mojang.brigadier.arguments.IntegerArgumentType;

public class CodexEconomy implements ModInitializer {
	public static final String MOD_ID = "codexeconomy";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		// Register the packet for client sync
		PayloadTypeRegistry.playS2C().register(PlayerDataSyncS2CPacket.ID, PlayerDataSyncS2CPacket.CODEC);

		// Sync player data on login
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.getPlayer();
			ServerWorld world = player.getServerWorld();
			EconomyState state = EconomyState.get(world);
			// sync full PlayerData
			org.codex.codexeconomy.network.EconomyNetworkingHelper.syncPlayerData(player, state.getOrCreate(player.getUuid()));
		});

		// Register commands
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

			// /balance
			dispatcher.register(CommandManager.literal("balance").executes(context -> {
				ServerPlayerEntity player = context.getSource().getPlayer();
				ServerWorld world = player.getServerWorld();
				double balance = EconomyState.get(world).getBalance(player.getUuid());
				context.getSource().sendFeedback(() -> Text.literal("Balance: $" + balance), false);
				return 1;
			}));

			// /pay <player> <amount>
			dispatcher.register(CommandManager.literal("pay")
					.then(CommandManager.argument("target", EntityArgumentType.player())
							.then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
									.executes(context -> {
										ServerPlayerEntity sender = context.getSource().getPlayer();
										ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");

										if (sender.getUuid().equals(target.getUuid())) {
											context.getSource().sendError(Text.literal("❌ You cannot pay yourself!"));
											return 0;
										}

										ServerWorld world = sender.getServerWorld();
										EconomyState state = EconomyState.get(world);

										int amount = IntegerArgumentType.getInteger(context, "amount");
										double senderBalance = state.getBalance(sender.getUuid());

										if (senderBalance < amount) {
											context.getSource().sendError(Text.literal("❌ You don't have enough money!"));
											return 0;
										}

										double targetBalance = state.getBalance(target.getUuid());

										// Sync-aware setters
										state.setBalance(sender.getUuid(), senderBalance - amount, sender);
										state.setBalance(target.getUuid(), targetBalance + amount, target);

										sender.sendMessage(Text.literal("✅ You paid $" + amount + " to " + target.getName().getString()
												+ ". New balance: $" + (senderBalance - amount)), false);
										target.sendMessage(Text.literal("💰 You received $" + amount + " from " + sender.getName().getString()
												+ "! New balance: $" + (targetBalance + amount)), false);

										return 1;
									})
							)
					)
			);

			// /addmoney
			dispatcher.register(CommandManager.literal("addmoney")
					// /addmoney <amount> (self)
					.then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
							.executes(context -> {
								ServerPlayerEntity player = context.getSource().getPlayer();
								ServerWorld world = player.getServerWorld();
								int amount = IntegerArgumentType.getInteger(context, "amount");

								EconomyState state = EconomyState.get(world);
								double balance = state.getBalance(player.getUuid());
								state.setBalance(player.getUuid(), balance + amount, player);

								context.getSource().sendFeedback(
										() -> Text.literal("You received $" + amount + "! New balance: $" + (balance + amount)),
										false
								);

								return 1;
							})
					)
					// /addmoney <player> <amount>
					.then(CommandManager.argument("target", EntityArgumentType.player())
							.then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
									.executes(context -> {
										ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
										ServerWorld world = target.getServerWorld();
										int amount = IntegerArgumentType.getInteger(context, "amount");

										EconomyState state = EconomyState.get(world);
										double balance = state.getBalance(target.getUuid());
										state.setBalance(target.getUuid(), balance + amount, target);

										context.getSource().sendFeedback(
												() -> Text.literal("Added $" + amount + " to " + target.getName().getString()
														+ ". New balance: $" + (balance + amount)),
												false
										);

										target.sendMessage(Text.literal("You received $" + amount
												+ "! New balance: $" + (balance + amount)), false);

										return 1;
									})
							)
					)
			);

			// /setbalance <player> <amount>
			dispatcher.register(CommandManager.literal("setbalance")
					.requires(source -> source.hasPermissionLevel(2))
					.then(CommandManager.argument("target", EntityArgumentType.player())
							.then(CommandManager.argument("amount", IntegerArgumentType.integer(0))
									.executes(context -> {
										ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
										ServerWorld world = target.getServerWorld();
										int amount = IntegerArgumentType.getInteger(context, "amount");

										EconomyState state = EconomyState.get(world);
										state.setBalance(target.getUuid(), amount, target);

										context.getSource().sendFeedback(
												() -> Text.literal("✔ Set " + target.getName().getString() + "'s balance to $" + amount),
												true
										);

										target.sendMessage(Text.literal("⚖ Your balance has been set to $" + amount), false);

										return 1;
									})
							)
					)
			);

			// /takemoney <player> <amount>
			dispatcher.register(CommandManager.literal("takemoney")
					.requires(source -> source.hasPermissionLevel(2))
					.then(CommandManager.argument("target", EntityArgumentType.player())
							.then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
									.executes(context -> {
										ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
										ServerWorld world = target.getServerWorld();
										int amount = IntegerArgumentType.getInteger(context, "amount");

										EconomyState state = EconomyState.get(world);
										double balance = state.getBalance(target.getUuid());
										double newBalance = Math.max(0, balance - amount);
										state.setBalance(target.getUuid(), newBalance, target);

										context.getSource().sendFeedback(
												() -> Text.literal("💸 Took $" + amount + " from " + target.getName().getString()
														+ ". New balance: $" + newBalance),
												true
										);

										target.sendMessage(Text.literal("❌ $" + amount + " has been taken from your account. New balance: $" + newBalance), false);

										return 1;
									})
							)
					)
			);

		});
	}
}
