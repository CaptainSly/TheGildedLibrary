package com.duckcraftian.gildedlibrary.core.system.archive;

import com.duckcraftian.gildedlibrary.api.system.archive.GLAEntry;
import com.duckcraftian.gildedlibrary.api.system.archive.GLAReader;
import com.duckcraftian.gildedlibrary.api.system.archive.GLAWriter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GLATest {

    private final Path output = Path.of(System.getProperty("user.dir"), "build", "test_archive.gla");
    private final String[] subDirs = {"textures", "models", "shaders", "scripts"};

    @Test
    void testGLAWrite() {
        Path pluginDir = Path.of(System.getProperty("user.dir"), "build", "plugin_test");

        List<Path> files = new ArrayList<>();


        String[] copypastas = {"""
                 Obligatory Didn't happen today, happened a few days ago.
                
                Throwaway for obvious reasons, some things are intentionally vague, I don't want this connected to myself. On mobile
                
                I (M23) don't go to the doctor very often because it is expensive and if you live in America you already know.
                
                I also like edging and its usually what I do a lot. Fast forward to the 18th and I have no work so I do a pretty intense edging session before going to bed, which I continued on the 19th until after lunch when my doctors appointment was.
                
                Just a normal checkup, he was looking through my files or something (I'm not a doctor) and asked if I ever had a prostate exam. I said no and I did not know what it was. He said that I should just in case I had prostate cancer. Eventually I concede and he tells me what he is going to do.
                
                Please note I had little idea what a prostate was, I just knew it was something you could reach up the butt. I never new it could make you orgasm.
                
                He does the exam and when he finds the prostate I just start cumming. This man touches my prostate and I'm here like a God damn cum machine and this is the most I've ever made. It gets all over the floor and some of his equipment.
                
                I was obviously embarrassed and only managed to get out a meek "sorry". He just stopped the exam and I pulled up my pants. Worst part is he did this before the checkup so we still spent a solid 15 minutes of mostly silence while he checked everything else. (The room had to be cleaned first though so that was another 20 minutes.
                
                TLDR: Edged before prostate exam, gets prostate exam, become cum machine and still have to be at the office a while.
                
                Edit: Grammar\\s
                """,

                """
                I've always been a big fan of Dolly Parton, but after a recent encounter I started to reevaluate the squeaky clean image I have of her.
                
                I happened to see her tour bus parked outside a department store in Houston, and I decided to try my luck to get an autograph from the legend herself. So I knocked on the bus door, and a large bodyguard opened. Behind him I could see her standing with her arms folded, and a furious look on her face.
                
                She then slapped the bodyguard on the ass and said "Let's show him how we do it back in Tennessee." after which he violently pulled me into the bus.
                
                Her bodyguard then proceeded to hold my arms from behind and push his knee into my back with all his might. While I was screaming from pain, Dolly Parton kept chanting "I'm happy when you're sad! I'm happy when you're sad! I'm happy when you're sad!" over and over for what seemed like five minutes.
                
                When I finally collapsed onto the ground from exhaustion, Dolly leaned down over me and said "If you wanna live to see tomorrow, you better get outta this town REAL quick, baby". From my perspective all I could see was two massive jugs and a bloodhound-looking face, with the facelift scar tissue behind the ears ready to burst at any second.
                
                After that, she poured a bottle of beer on my head and ripped ass so rancidly that even the bodyguard started to retch. I was then thrown out on the curb, where I laid for a while before the bodyguard came over and whispered into my ear "I kid you not, this is her on a good day." He then put a hundred dollar bill into my back pocket and said behind his breath "At least you get to go home, while I have to go back in there." The door closed with a thud, and I hurried the hell back to my car.
                
                I realize I overstepped by knocking on her tour bus, and to some degree invading her privacy, but the way she handled the situation was very excessive.
                """,

                """
                You will never be a real Dwarf. You have no beard, you have no pickaxe, you have no alcohol dependence. You are an slightly-below-average height human twisted by ale and love of gold into a crude mockery of Armok's perfection. All the “artifacts” you can make are shoddy and -well-crafted- at best. Behind your back people mistake you for a knife-eared leaf-lover. Your "kin" are disgusted and ashamed of you, your fellow Guildmembers laugh at your inability to hold your liquor behind your back. Goblins are utterly unafraid of you, your hand-me-down Dwarven bronze armor is awkward and too tight a fit, your weapon skills dabbling, and you can't even enter martial trances. Generations of honing their craftdwarfship have allowed Dwarves to create artifact cat bone floor grates with incredible efficiency. Even your -chert earrings- that menace with spikes of crundle bone look uncanny and unnatural to a Dwarf. Your inability to get strange moods is a dead giveaway. And even if you tried being a miner and managed to get a spare copper pick assigned to you, the overseer will change their mind and put you on rock-hauling duty the second they get a glance of your pitiful mining speed. You will never be a legendary cheesemaker. You will never be a legendary fish dissector. You down a small mug of pathetic 20 proof beer every single morning and tell yourself it’s going to be ok, but deep inside you feel the damage to your inferior human liver creeping up like an elven ambush, ready to drive you stark raving mad. Eventually it’ll be too much to bear - you’ll be stricken by melancholy, walk to the surface, climb up to the side of a cliff, and plunge to your doom. Your kin will find you, heartbroken but relieved that they no longer have to deal with poor quality crafts clogging up the stockpiles. They’ll bury you with a wooden casket on the surface, and every visiting performer and scholar for the rest of eternity will know a human is buried there. Your body will become fertilizer for trees, and all that will remain of your legacy is a skeleton that will probably get reanimated by some invading necromancer and then get melted by the magma traps. This is your fate. This is what you chose. There is no turning back.\\s
                """,

                """
 Sometimes it's innocuous stuff like eating the last slice of pizza, leaving me with the empty box, when she's marathoning her reality TV obsessions.Then there's the time she doxxed and swatted her ex's new girlfriend after seeing one innocent Instagram story of them at a coffee shop. She claimed it was "just girl jealousy" and that the SWAT team showing up was "hilarious karma." The poor girl had to move apartments, changed her number, and still has panic attacks from the whole ordeal. My gf bragged about it in our group chat like it was a funny anecdote, then when the cops showed up at our door, she just batted her eyelashes and said "I'm literally just a girl" while they were reading her the charges.

Or the time she "accidentally" leaked her coworker's nudes to the entire office Slack after the girl got promoted instead of her. She said the files "just popped up" on her phone and she "panicked and hit share all." The coworker ended up quitting in humiliation, and HR called it one of the worst violations they'd ever seen. My gf got fired, obviously, but when I asked why she did it, she pouted and went "I'm literally just a girl, jealousy hits different, okay?"

And don't get me started on the family reunion where she spiked her cousin's drink with laxatives because he "looked at her funny" during charades. The guy spent the entire event locked in the bathroom while everyone else pretended not to notice the smell. She filmed the whole thing for her private story, captioning it "justice for girlies." When his mom confronted her, my gf just shrugged and said "I'm literally just a girl, I can't help being petty."Every time I try to talk about how messed up it is, she hits me with the big eyes, fake tears, and "I'm literally just a girl" like that's supposed to erase felonies, ruined lives, and the fact that I'm dating a walking war crime in pink bows. I don't know how much longer I can take this.\\s""",

                """
 Handshakes, hugs

You want me outside? You wanna see me with pancakes and drugs?

Take your fat ass to sleep

I'm jumpin' out of the candy-coated grim reaper Jeep

I put you to sleep, I put you six feet deep

I'm just in the ground, I blaze a pound, I let my top down

I could've played for Washington Redskins, six points for a touchdown

I might give you a six point diamond, it's a karat, this is apparent

I'm uh, I got more knowledge than both your parents combined

I graduated never, I didn't go to high school in '99

Everything was fine, I bang Ginuwine

Uh, no homo, I'm talkin' about in the back of my trunk

That's CD deck, other players wanna check, I eat Chex mix

Haters wanna get in the mix, I ball, I got a fresh set of kicks

I play for the Phoenix Suns, I graduated in 1991

Damn, that motherfucker older than the motherfuckin' moon plus the sun

He got older than the damn solar system

I come through, Uh, I might pop trunk on your sister

I eat fried gristle, and bacon with eggs, and toast

Everybody, "Damn, that motherfucker right there, he ball the most

He ball from coast to coast, he ball across seven seas"

You talkin' 'bout, "Oh, I'm in a gang"

Motherfucker, you're on your damn knees 'cause you gay

I pull up sittin' sideways with Sway

We at a Chinese buffet, eatin' on a Monday, it's a Tuesday

Make it feel like it's damn Ruby Tuesday, it was a Saturday

But it don't even matter anyway

I come through with diamonds and sapphire across my chest, figure I was Candy Ken Griffin Junior

I come through, I played down south in Oklahoma Sooner

I could've played tailback, halfback, or hatchback

I'm in a four-door hatchback

Damn, motherfucker could've played for the Dallas Mavericks

But they found crack in my jacket

They found marijuana in my socks

These motherfuckers, damn

Mark Cuban said, "Man, you gon' have to box one of these players

'Cause they gon' take your position"

Diamonds gon' glisten, wood wheels twistin'

Sign on the back, let's just go fishin'

Let's go to sleep\\s"""};

        for (int i = 0; i < 5; i++) {
            for (String subDir : subDirs) {
                File f = new File(pluginDir.toString() + "/" + subDir);
                f.mkdirs();
                try (FileWriter writer = new FileWriter(f.toPath().toString() + "/doc_" + i + ".txt")) {
                    writer.write(copypastas[i]);
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            Stream<Path> paths = Files.walk(pluginDir).filter(path -> !Files.isDirectory(path));
            files = paths.toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GLAWriter.write(files, pluginDir, output, GLAWriter.ArchiveType.ASSET_ARCHIVE);
        assertTrue(Files.exists(output));
    }

    @Test
    void testGLARead() {
        GLAReader reader = new GLAReader(output);

        for (int i = 0; i < 5; i++) {
            for (String dir : subDirs) {
                assertTrue(reader.getIndex().containsKey(dir + "/doc_" + i + ".txt"));
            }
        }
    }


}
