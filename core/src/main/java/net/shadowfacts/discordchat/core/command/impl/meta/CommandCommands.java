package net.shadowfacts.discordchat.core.command.impl.meta;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.shadowfacts.discordchat.api.IConfig;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.command.ICommand;
import net.shadowfacts.discordchat.api.command.ICommandManager;
import net.shadowfacts.discordchat.api.permission.IPermissionManager;
import net.shadowfacts.discordchat.api.command.exception.CommandException;
import net.shadowfacts.discordchat.api.permission.Permission;

import java.util.stream.Collectors;

/**
 * @author shadowfacts
 */
public class CommandCommands implements ICommand {

	private IDiscordChat discordChat;
	private IConfig config;
	private ICommandManager commandManager;
	private IPermissionManager permissionManager;

	public CommandCommands(IDiscordChat discordChat) {
		this.discordChat = discordChat;
		this.config = discordChat.getConfig();
		this.commandManager = discordChat.getCommandManager();
		this.permissionManager = discordChat.getPermissionManager();
	}

	@Override
	public String getName() {
		return "commands";
	}

	@Override
	public Permission getMinimumPermission() {
		return config.getMinimumPermission(getName());
	}

	@Override
	public void execute(String[] args, User author, TextChannel channel) throws CommandException {
		discordChat.sendMessage("Commands (you have permission for **bolded** ones): ", channel);
		discordChat.sendMessage(String.join(", ", commandManager.getCommands().stream()
				.map(cmd -> {
					Permission min = cmd.getMinimumPermission();
					if (permissionManager.has(author, channel.getGuild(), min)) {
						return "**" + cmd.getName() + "**";
					} else {
						return cmd.getName();
					}
				})
				.collect(Collectors.toList())), channel);
	}

	@Override
	public String getDescription() {
		return "Lists all available commands";
	}

	@Override
	public String getUsage() {
		return "";
	}

}
