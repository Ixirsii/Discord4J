/*
 * This file is part of Discord4J.
 *
 * Discord4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Discord4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Discord4J.  If not, see <http://www.gnu.org/licenses/>.
 */
package discord4j.core.spec;

import discord4j.core.object.SoundboardSound;
import discord4j.core.object.entity.Guild;
import discord4j.discordjson.json.EmojiData;
import discord4j.discordjson.json.ImmutableSoundboardSoundCreateRequest;
import discord4j.discordjson.json.ImmutableSoundboardSoundModifyRequest;
import discord4j.discordjson.json.SoundboardSoundCreateRequest;
import discord4j.discordjson.json.SoundboardSoundModifyRequest;
import discord4j.discordjson.possible.Possible;
import org.immutables.value.Value;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Value.Immutable
public interface SoundboardSoundCreateSpecGenerator extends AuditSpec<SoundboardSoundCreateRequest> {

    String name();

    String sound();

    Possible<Optional<Double>> volume();

    Possible<Optional<EmojiData>> emoji();

    @Override
    default SoundboardSoundCreateRequest asRequest() {
        ImmutableSoundboardSoundCreateRequest.Builder builder = SoundboardSoundCreateRequest.builder();
        if (!emoji().isAbsent() && Possible.flatOpt(emoji()).isPresent()) {
            EmojiData emoji = Possible.flatOpt(emoji()).get();
            builder.emojiId(Possible.of(emoji.id()));
            builder.emojiName(Possible.ofNullable(emoji.name()));
        }
        builder.name(name());
        builder.sound(sound());
        builder.volume(volume());
        return builder.build();
    }

}

@Value.Immutable(builder = false)
abstract class SoundboardSoundCreateMonoGenerator extends Mono<SoundboardSound> implements SoundboardSoundCreateSpecGenerator {

    abstract Guild guild();

    @Override
    public void subscribe(CoreSubscriber<? super SoundboardSound> actual) {
        guild().createSoundboardSound(SoundboardSoundCreateSpec.copyOf(this)).subscribe(actual);
    }

    @Override
    public abstract String toString();
}
