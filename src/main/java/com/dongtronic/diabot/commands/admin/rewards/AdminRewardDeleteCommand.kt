package com.dongtronic.diabot.commands.admin.rewards

import com.dongtronic.diabot.commands.DiabotCommand
import com.dongtronic.diabot.data.RewardDAO
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Role
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import java.util.*

class AdminRewardDeleteCommand(category: Command.Category, parent: Command?) : DiabotCommand(category, parent) {

    private val logger = LoggerFactory.getLogger(AdminRewardDeleteCommand::class.java)

    init {
        this.name = "delete"
        this.help = "Delete role reward"
        this.guildOnly = true
        this.aliases = arrayOf("d", "del", "r", "rm", "remove")
    }

    override fun execute(event: CommandEvent) {
        val args = event.args.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        if (args.size != 2) {
            throw IllegalArgumentException("Required and reward role IDs are required")
        }

        if (!StringUtils.isNumeric(args[0]) || !StringUtils.isNumeric(args[1])) {
            throw IllegalArgumentException("Role IDs must be numeric")
        }

        val requiredId = args[0]
        val rewardId = args[1]

        val requiredRole = event.jda.getRoleById(requiredId)
                ?: throw IllegalArgumentException("Role $requiredId does not exist")
        val rewardRole = event.jda.getRoleById(rewardId)
                ?: throw IllegalArgumentException("Role $rewardId does not exist")

        RewardDAO.getInstance().removeSimpleReward(event.guild.id, requiredId, rewardId)

        event.reply("Removed reward **${rewardRole.name}** for **${requiredRole.name}**")
    }


}
