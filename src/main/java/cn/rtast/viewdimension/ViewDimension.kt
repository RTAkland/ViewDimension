/*
 * Copyright 2024 RTAkland
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

import net.fabricmc.api.ModInitializer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class ViewDimension : ModInitializer {

    companion object {

        fun replacePlayerName(player: ServerPlayerEntity): Text {
            val dimension = player.world.dimension.effects.path
            val oldName = player.name.string
            val dimensionText = when (dimension) {
                "overworld" -> Text.literal(" <").append(Text.translatable("vdim.overworld")).append(Text.literal(">"))
                    .styled { it.withColor(Formatting.GREEN).withItalic(true) }
                "the_nether" ->Text.literal(" <").append(Text.translatable("vdim.thenether")).append(Text.literal(">"))
                    .styled { it.withColor(Formatting.DARK_RED).withItalic(true) }
                "the_end" -> Text.literal(" <").append(Text.translatable("vdim.theend")).append(Text.literal(">"))
                    .styled { it.withColor(Formatting.DARK_PURPLE).withItalic(true) }
                else -> Text.literal(" <$dimension>")
                    .styled { it.withColor(Formatting.GRAY).withItalic(true) }
            }
            return Text.literal(oldName).append(dimensionText)
        }

    }

    override fun onInitialize() {
        println("ViewDimension loaded!")
    }
}