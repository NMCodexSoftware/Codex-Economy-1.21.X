package org.codex.codexeconomy;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.codex.codexeconomy.block.ModBlocks;
import org.codex.codexeconomy.data.EconomyState;
import org.codex.codexeconomy.item.ModItemGroups;
import org.codex.codexeconomy.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.argument.EntityArgumentType;

import java.text.NumberFormat;
import java.util.*;

public class CodexEconomy implements ModInitializer {
	public static final String MOD_ID = "codexeconomy";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();



		// Commands
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {


			// /balance
			dispatcher.register(CommandManager.literal("balance").executes(context -> {
				ServerPlayerEntity player = context.getSource().getPlayer();
				ServerWorld world = player.getServerWorld();

				EconomyState state = EconomyState.get(world);
				double balance = state.getBalance(player.getUuid());

				context.getSource().sendFeedback(
						() -> Text.literal("Balance: $" + balance),
						false
				);
				return 1;
			}));

			// /addmoney
			dispatcher.register(CommandManager.literal("addmoney")
					// Case 1: /addmoney <amount> (self)
					.then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
							.executes(context -> {
								ServerPlayerEntity player = context.getSource().getPlayer();
								ServerWorld world = player.getServerWorld();

								int amount = IntegerArgumentType.getInteger(context, "amount");

								EconomyState state = EconomyState.get(world);
								double balance = state.getBalance(player.getUuid());
								state.setBalance(player.getUuid(), balance + amount);

								context.getSource().sendFeedback(
										() -> Text.literal("You received $" + amount
												+ "! New balance: $" + (balance + amount)),
										false
								);

								return 1;
							})
					)
					// Case 2: /addmoney <player> <amount>
					.then(CommandManager.argument("target", EntityArgumentType.player())
							.then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
									.executes(context -> {
										ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
										ServerWorld world = target.getServerWorld();

										int amount = IntegerArgumentType.getInteger(context, "amount");

										EconomyState state = EconomyState.get(world);
										double balance = state.getBalance(target.getUuid());
										state.setBalance(target.getUuid(), balance + amount);

										// Feedback to command sender
										context.getSource().sendFeedback(
												() -> Text.literal("Added $" + amount + " to " + target.getName().getString()
														+ ". New balance: $" + (balance + amount)),
												false
										);

										// Feedback to target
										target.sendMessage(Text.literal("You received $" + amount
												+ "! New balance: $" + (balance + amount)), false);

										return 1;
									})
							)
					)
			);

			// /pay <player> <amount>
			dispatcher.register(CommandManager.literal("pay")
					.then(CommandManager.argument("target", EntityArgumentType.player()) // 👈 autocomplete here
							.then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
									.executes(context -> {
										ServerPlayerEntity sender = context.getSource().getPlayer();
										ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");

										// Prevent paying yourself
										if (sender.getUuid().equals(target.getUuid())) {
											context.getSource().sendError(Text.literal("❌ You cannot pay yourself!"));
											return 0;
										}

										ServerWorld world = sender.getServerWorld();
										EconomyState state = EconomyState.get(world);

										int amount = IntegerArgumentType.getInteger(context, "amount");
										double senderBalance = state.getBalance(sender.getUuid());

										// Check funds
										if (senderBalance < amount) {
											context.getSource().sendError(Text.literal("❌ You don't have enough money!"));
											return 0;
										}

										// Transfer money
										state.setBalance(sender.getUuid(), senderBalance - amount);
										double targetBalance = state.getBalance(target.getUuid());
										state.setBalance(target.getUuid(), targetBalance + amount);

										// Feedback
										sender.sendMessage(Text.literal("✅ You paid $" + amount + " to " + target.getName().getString()
												+ ". New balance: $" + (senderBalance - amount)), false);

										target.sendMessage(Text.literal("💰 You received $" + amount + " from " + sender.getName().getString()
												+ "! New balance: $" + (targetBalance + amount)), false);

										return 1;
									})
							)
					)
			);

			// /setbalance <player> <amount>
			dispatcher.register(CommandManager.literal("setbalance")
					.requires(source -> source.hasPermissionLevel(2)) // Only allow server ops
					.then(CommandManager.argument("target", EntityArgumentType.player())
							.then(CommandManager.argument("amount", IntegerArgumentType.integer(0))
									.executes(context -> {
										ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
										ServerWorld world = target.getServerWorld();

										int amount = IntegerArgumentType.getInteger(context, "amount");

										EconomyState state = EconomyState.get(world);
										state.setBalance(target.getUuid(), amount);

										// Feedback to admin
										context.getSource().sendFeedback(
												() -> Text.literal("✔ Set " + target.getName().getString()
														+ "'s balance to $" + amount),
												true
										);

										// Feedback to target
										target.sendMessage(Text.literal("⚖ Your balance has been set to $" + amount), false);

										return 1;
									})
							)
					)
			);

			// /takemoney <player> <amount>
			dispatcher.register(CommandManager.literal("takemoney")
					.requires(source -> source.hasPermissionLevel(2)) // Only allow server ops
					.then(CommandManager.argument("target", EntityArgumentType.player())
							.then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
									.executes(context -> {
										ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
										ServerWorld world = target.getServerWorld();

										int amount = IntegerArgumentType.getInteger(context, "amount");

										EconomyState state = EconomyState.get(world);
										double balance = state.getBalance(target.getUuid());

										// Deduct, but don't allow negative balances
										double newBalance = Math.max(0, balance - amount);
										state.setBalance(target.getUuid(), newBalance);

										// Feedback to admin
										context.getSource().sendFeedback(
												() -> Text.literal("💸 Took $" + amount + " from "
														+ target.getName().getString()
														+ ". New balance: $" + newBalance),
												true
										);

										// Feedback to target
										target.sendMessage(Text.literal("❌ $" + amount
												+ " has been taken from your account. New balance: $" + newBalance), false);

										return 1;
									})
							)
					)
			);

			// /baltop
			dispatcher.register(CommandManager.literal("baltop").executes(context -> {
				ServerPlayerEntity sender = context.getSource().getPlayer();
				ServerWorld world = sender.getServerWorld();

				EconomyState state = EconomyState.get(world);

				Map<UUID, Double> balances = new HashMap<>(state.getAllBalances());

				// Sort by balance descending
				List<Map.Entry<UUID, Double>> sorted = balances.entrySet().stream()
						.sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
						.toList();

				NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

				StringBuilder message = new StringBuilder("===== 💰 Top 10 Balances =====\n");

				int limit = Math.min(10, sorted.size());
				for (int i = 0; i < limit; i++) {
					UUID uuid = sorted.get(i).getKey();
					double balance = sorted.get(i).getValue();

					ServerPlayerEntity online = world.getServer().getPlayerManager().getPlayer(uuid);
					String name = (online != null) ? online.getName().getString() : uuid.toString().substring(0, 8);

					message.append((i + 1))
							.append(". ")
							.append(name)
							.append(" - ")
							.append(currencyFormat.format(balance))
							.append("\n");
				}

				// Add separator
				message.append("==============================\n");

				// Sender’s rank
				double senderBalance = state.getBalance(sender.getUuid());
				int senderRank = -1;
				for (int i = 0; i < sorted.size(); i++) {
					if (sorted.get(i).getKey().equals(sender.getUuid())) {
						senderRank = i + 1;
						break;
					}
				}

				if (senderRank != -1) {
					message.append("Your rank: #")
							.append(senderRank)
							.append(" with ")
							.append(currencyFormat.format(senderBalance));
				} else {
					message.append("You are not ranked yet (balance: ")
							.append(currencyFormat.format(senderBalance))
							.append(")");
				}

				// Send the entire message once
				context.getSource().sendFeedback(() -> Text.literal(message.toString()), false);

				return 1;
			}));
		});
	}
}