package com.dongtronic.diabot.commands

import com.dongtronic.diabot.data.AdminDAO
import com.dongtronic.diabot.data.RewardDAO
import com.dongtronic.diabot.util.CommandUtils
import com.dongtronic.diabot.util.RoleUtils
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Role
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import java.util.*

class AdminCommand(category: Command.Category) : DiabotCommand(category, null) {

    private val logger = LoggerFactory.getLogger(AdminCommand::class.java)

    init {
        this.name = "admin"
        this.help = "Administrator commands"
        this.guildOnly = true
        this.aliases = arrayOf("a")
        this.examples = arrayOf()
        this.userPermissions = arrayOf(Permission.ADMINISTRATOR)
        this.children = arrayOf(
                AdminUsernameCommand(category, this),
                AdminRewardsCommand(category, this),
                AdminChannelsCommand(category, this))
    }

    override fun execute(event: CommandEvent) {
        val args = event.args.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        if (args.isEmpty()) {
            event.replyError("Please specify a command")
            return
        }

        event.replyError("Unknown command: ${args[0]}")

    }




}
