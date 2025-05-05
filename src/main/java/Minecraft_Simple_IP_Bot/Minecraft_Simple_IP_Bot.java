package Minecraft_Simple_IP_Bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.SessionRecreateEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;


public final class Minecraft_Simple_IP_Bot extends JavaPlugin implements EventListener {

    private JDA client;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        String botToken = this.getConfig().getString("DISCORD.BOT_TOKEN");
        String channelId = this.getConfig().getString("DISCORD.CHANNEL_ID");

        if (botToken == null || botToken.isEmpty() || channelId == null || channelId.isEmpty()) {
            getLogger().warning("Bot token or channel ID missing in config. Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        try {
            client = JDABuilder.createDefault(botToken)
                    .setStatus(OnlineStatus.DO_NOT_DISTURB)
                    .addEventListeners(this)
                    .build();
        } catch (Exception e) {
            getLogger().severe("Failed to login to Discord: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
        }
    }


    @Override
    public void onDisable() {
        if (client != null) {
            client.shutdown();
        }
    }


    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ReadyEvent) {
            try {
                sendPublicIP(); //magic happens here
            } catch (Exception e) {
                getLogger().severe("Error retrieving/sending public IP: " + e.getMessage());
            }
        }

        if (event instanceof SessionRecreateEvent) {
            try {
                sendPublicIP(); //magic happens here
            } catch (Exception e) {
                getLogger().severe("Error retrieving/sending public IP while reconnecting: " + e.getMessage());
            }
        }
    }


    private void sendPublicIP() {
        String channelId = this.getConfig().getString("DISCORD.CHANNEL_ID");
        TextChannel channel = client.getTextChannelById(Objects.requireNonNull(channelId));

        if (channel == null) {
            getLogger().warning("Discord channel not found.");
            return;
        }
        try {
            String publicIP = getPublicIP();
            if (publicIP != null) {
                long updateMessageId = this.getConfig().getLong("DISCORD.UPDATE_MESSAGE_ID");
                String Port = this.getConfig().getString("DISCORD.Port");
                String updateMessage = this.getConfig().getString("DISCORD.UPDATE_MESSAGE");
                LocalDateTime currentDateTime = LocalDateTime.now().withNano(0);
                String currentDateTimeString = currentDateTime.toString();
                currentDateTimeString = currentDateTimeString.replace("T", " ");
                String updatedMessage;

                if (updateMessage != null && Port != null) {
                    updatedMessage = updateMessage.replace("%server_ip%", publicIP).replace("%port%", Port).replace("%currentdatetime%", currentDateTimeString);
                }
                else {
                    getLogger().info("Empty message, please fix your config.yml file. Keep in mind to include %server_ip%, %port%, %currentdatetime%");
                    return;
                }

                if (updateMessageId == 0) {
                    channel.sendMessage(updatedMessage).queue();
                    getLogger().info("Sent public IP to Discord: " + publicIP + " and Port:  " + Port);
                }

                else {
                    channel.editMessageById(updateMessageId, MessageEditData.fromContent(updatedMessage)).queue();
                    getLogger().info("Updated the public IP to Discord: " + publicIP + " and Port: " + Port);
                }
            }
        }
         catch (Exception e) {
            getLogger().severe("Error retrieving/sending public IP: " + e.getMessage());
        }
    }


    private String getPublicIP() {
        try {
            URL url = new URL("https://checkip.amazonaws.com/");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            return in.readLine();
        } catch (Exception e) {
            getLogger().severe("Error fetching public IP: " + e.getMessage());
            return null;
        }
    }
}