/*
 * Copyright 2023 RTAkland
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package cn.rtast.viewdimension

import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.scoreboard.Team
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class ViewDimension : DedicatedServerModInitializer {
    override fun onInitializeServer() {
        ServerTickEvents.START_SERVER_TICK.register { tick ->
            val scoreboard = tick.scoreboard
            if (!scoreboard.teamNames.contains(Dimension.TheNether.id)) {
                scoreboard.addTeam(Dimension.TheNether.id)
                scoreboard.getTeam(Dimension.TheNether.id)!!.suffix = makeText("the_nether")
            }
            if (!scoreboard.teamNames.contains(Dimension.TheEnd.id)) {
                scoreboard.addTeam(Dimension.TheEnd.id)
                scoreboard.getTeam(Dimension.TheEnd.id)!!.suffix = makeText("the_end")
            }
            if (!scoreboard.teamNames.contains(Dimension.Overworld.id)) {
                scoreboard.addTeam(Dimension.Overworld.id)
                scoreboard.getTeam(Dimension.Overworld.id)!!.suffix = makeText("overworld")
            }

            val playerList = tick.playerManager.playerList
            playerList.forEach { p ->
                val currentDimension = p.world.dimensionKey.value.path
                scoreboard.addPlayerToTeam(p.entityName.toString(), Team(scoreboard, currentDimension))
            }
        }
    }

    private fun makeText(dimension: String): MutableText {
        val header = Text.literal("  <").styled { it.withBold(true).withItalic(true).withColor(Formatting.GRAY) }
        val dimensionText: Text = when (dimension) {
            "overworld" -> Text.literal("主世界")
                .styled { it.withBold(true).withItalic(true).withColor(Formatting.GREEN) }

            "the_nether" -> Text.literal("下界")
                .styled { it.withBold(true).withItalic(true).withColor(Formatting.RED) }

            else -> Text.translatable("末地")
                .styled { it.withBold(true).withItalic(true).withColor(Formatting.DARK_PURPLE) }
        }
        val footer = Text.literal(">  ").styled { it.withBold(true).withItalic(true).withColor(Formatting.GRAY) }
        return header.append(dimensionText).append(footer)
    }

    enum class Dimension(val id: String) {
        TheNether("the_nether"), TheEnd("the_end"), Overworld("overworld")
    }

}
