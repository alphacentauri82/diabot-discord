package com.dongtronic.diabot.commands.nightscout

import com.dongtronic.diabot.commands.DiabotCommand
import com.dongtronic.diabot.data.NightscoutDAO
import com.dongtronic.diabot.util.CommandUtils
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory

class NightscoutAdminShortAddCommand(category: Category, parent: Command?) : DiabotCommand(category, parent) {

    private val logger = LoggerFactory.getLogger(NightscoutAdminShortAddCommand::class.java)

    init {
        this.name = "add"
        this.help = "Add a short response channel"
        this.guildOnly = true
        this.ownerCommand = false
        this.aliases = arrayOf("a")
        this.category = category
        this.examples = arrayOf(this.parent!!.name + " add")
    }

    override fun execute(event: CommandEvent) {
        try {
            if (!CommandUtils.requireAdminChannel(event)) {
                return
            }

            val args = event.args.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            if (args.size != 1) {
                event.replyError("Please include exactly one channel ID")
                return
            }

            val channel = if (event.message.mentionedChannels.size == 0) {
                if (!StringUtils.isNumeric(args[0])) {
                    throw IllegalArgumentException("Channel ID must be numeric")
                }

                val channelId = args[0]
                event.jda.getTextChannelById(channelId)
                        ?: throw IllegalArgumentException("Channel `$channelId` does not exist")
            } else {
                event.message.mentionedChannels[0]
            }

            logger.info("Adding channel ${channel.id} as short channel for ${event.guild.id}")

            NightscoutDAO.getInstance().addShortChannel(event.guild.id, channel.id)

            event.replySuccess("Added channel **${channel.name}** (`${channel.id}`) as short reply channel")
        } catch (ex: Exception) {
            event.replyError(ex.message)
            return
        }
    }
}
