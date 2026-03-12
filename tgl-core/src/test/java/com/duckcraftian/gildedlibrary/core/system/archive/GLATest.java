package com.duckcraftian.gildedlibrary.core.system.archive;

import com.duckcraftian.gildedlibrary.api.system.archive.asset.GLAAssetReader;
import com.duckcraftian.gildedlibrary.api.system.archive.asset.GLAAssetWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.tinylog.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GLATest {

    private static final Path pluginDir = Path.of(System.getProperty("user.dir"), "build", "plugin_test");
    private static final Path output = Path.of(System.getProperty("user.dir"), "build", "test_archive.gla");
    private static final String[] subDirs = {"textures", "models", "shaders", "scripts"};
    private static GLAAssetReader reader;
    private static final String[] lyrics = {
            """
If this is my crown
I am its slave
This is my kingdom I built upon a sandy shore
It's sinking deeper
Still they're knocking at my door
Watching, waiting for me to break
Upon a throne of snakes
I won't let them in
I won't let them in

How the hell could I know
I was their scapegoat
Misdirection and woe
One day I know I'll die alone
How cruel it is
To suffer in silence
Undiscovered
My 11th hour
Is this what it takes
To wear the crown of the king
Is this living
I'm starting to feel

The more we weep
The lesser suffer
And cower
The less they see
The greater power
A king on the ivory tower

I will decide
With so much power comes veering eyes
Under the spotlight we're devoured
The greatest lie is the lie we tell ourselves
As a means to an end
No one else can understand
How numbing it is to hide
What makes us human

A throne of needles and blame
All for me
You only see what you want to believe
No
Blinded by faith
Until it all crumbles
In a moment
So I bare the burden
This is what it takes
To rule a kingdom of snakes
Is this living
I'm starting to feel

The more we weep
The lesser suffer
And cower
The less they see
The greater power
A king on the ivory tower

Death can take me
I don't want it
The weight of the fucking world
Watch as I crash down from the ivory tower
Pedestals crumble before the overwhelming judgement of the masses

Now watch me hit the ground
Now watch me hit the
Ground
I'm fucking sick of it
Masking, it's taxing me
You want to watch me fall
I pray I hit the ground
Hard enough so you can watch me die here
""",
            """
Rising from the ashes of a decimated legacy,
We'll never be, couldn't be, what you wanted us to be.
Tried and tested, disrespected, always underestimated,
Cast aside, well fuck your pride piss on your graves and where you fucking died.

Ripping through the womb of a lethargic, bloated, dying mother,
Left to fester, no placenta, born a bastard and dissenter.
Forced to watch as lesser brothers gained the favour of foul mother,
Our wings were tore before we had the chance to prove that we could soar.

Lied to my face, callous embrace,
Your medicine, now how's that taste?
Maces, faces, leave no trace if you're the kings, we're the aces.
Stir the pot, parting shot, do I care? Probably not.
Little liars leech vampires, waste of piss to calm your petty fires.

You don't, know a good thing you've got until its gone.
You have, no place here I make my own luck,
I could watch the world, burn around me and I still wouldn't give a fuck.

Lied to my face, callous embrace,
Your medicine, now how's that taste?
Maces, faces, leave no trace if you're the kings, we're the aces.
Stir the pot, parting shot, do I care? Probably not.
Little liars leech vampires, waste of piss to calm your fires.

Third time I'm lucky, three times the charm,
I'm in the middle where the storm is calm,
now I am dangerous, now I am armed,
don't get this twisted I do mean you harm.

No one will cry when your blood has been spilled,
I'm not playing you cunts this is kill or be killed.
I'm a bastard, an orphan, denied by creator,
If I am a God then you'll meet your Un-maker.

The sickest motherfucker, you've ever seen,
the carnage I'll cause, the world will not believe.
I'd wipe the slate clean, and shatter the dream,
destroy any trace where your foul flesh, has been.

Decisions decisions, I'll make my incisions,
I'll slice them and dice them with expert precision, your lazy derision,
has caused this division, now I'm to bend over assume the position?

FUCK THAT!

Six years!

Sick fears!

But hailed as a messiah!

Not respected just rejected, misdirected and neglected.
Shunned by all, well fuck you all, opposing me? Expect to fall.
""",
            """
It bleeds! It breathes!

What stands before us, is not a machine
It breathes, it will bleed and it will dream!

Its body is covered in hundreds of wires.
and a mouth that attempts to speak, it attempts to lie
Only murmurs, collapse from its jaws.

And a world, a world without,
A world without you.

but I rise. the dead will pride
it breathes, beyond this life,

so sleep, sleep among us, hesitate no more

En....tomb-men....of-a-ma...chine (entombment of a machine)
We kneel and we plead for no mourning ahead of us,
With only delayed movements, from its figure, we all begin to strain.

Entombment of a machine
Entombment of a machine

What stands before us is not a machine
What stands before us is not a machine

My legs weaken at the site of this damaged program,
This program kept you breathing, it kept you alive.
These circuits diffused once more.

Its body is covered in hundreds of wires.

Only murmurs collapse from its Scream

Entombment of a Machine

But I saw It Die.
But I saw it die
But I Saw It Dead.
But I saw It Die.
I saw it die
I Watched it DIE!
""",
            """
Fear Me
I am destruction of innocence
I am the violence embedded in flesh
I am the pain in the bones of the mortal shell
The dark heart of the earth
I am hell

I am hell

Fear Me
I am destruction of innocence
I am the violence embedded in flesh
I am the pain in the bones of the mortal shell
The dark heart of the earth
I am hell

Tear at the throats of the
Fathers of deception
Let the blood of the cowards
Flow as the wines
False claims of retribution
May they choke on their lies

Lay waste the relics
Silence the hymns of deceit

You will know pain
You will see the true face of panic
Devastation now and forever
Reign of Darkness

The masses one with the cursed
Godless in time
No longer a helpless slave
To the mark of the divine

Lay waste the age of man
Return to the Earth

You will know pain
You will see the true face of panic
Devastation
Now and forever
Reign of Darkness

Fear me
I am the destruction of innocence
I am the violence embedded in flesh
I am the pain in the bones of the mortal shell
The dark heart of the Earth
I am hell
""",
            """
We're no strangers to love
You know the rules and so do I
A full commitment's what I'm thinking of
You wouldn't get this from any other guy

I just wanna tell you how I'm feeling
Gotta make you understand

Never gonna give you up
Never gonna let you down
Never gonna run around and desert you
Never gonna make you cry
Never gonna say goodbye
Never gonna tell a lie and hurt you

We've known each other for so long
Your heart's been aching, but you're too shy to say it
Inside, we both know what's been going on
We know the game and we're gonna play it

And if you ask me how I'm feeling
Don't tell me you're too blind to see

Never gonna give you up
Never gonna let you down
Never gonna run around and desert you
Never gonna make you cry
Never gonna say goodbye
Never gonna tell a lie and hurt you

Never gonna give you up
Never gonna let you down
Never gonna run around and desert you
Never gonna make you cry
Never gonna say goodbye
Never gonna tell a lie and hurt you

(Ooh, give you up)
(Ooh, give you up)
Never gonna give, never gonna give
(Give you up)
Never gonna give, never gonna give
(Give you up)

We've known each other for so long
Your heart's been aching, but you're too shy to say it
Inside, we both know what's been going on
We know the game and we're gonna play it

I just wanna tell you how I'm feeling
Gotta make you understand

Never gonna give you up
Never gonna let you down
Never gonna run around and desert you
Never gonna make you cry
Never gonna say goodbye
Never gonna tell a lie and hurt you

Never gonna give you up
Never gonna let you down
Never gonna run around and desert you
Never gonna make you cry
Never gonna say goodbye
Never gonna tell a lie and hurt you

Never gonna give you up
Never gonna let you down
Never gonna run around and desert you
Never gonna make you cry
Never gonna say goodbye
Never gonna tell a lie and hurt you
"""};

    @BeforeAll
    static void setup() {
        for (int i = 0; i < 5; i++) {
            for (String subDir : subDirs) {
                File f = new File(pluginDir.toString() + "/" + subDir);
                f.mkdirs();
                try (FileWriter writer = new FileWriter(f.toPath().toString() + "/doc_" + (i + 1) + ".txt")) {
                    writer.write(lyrics[i]);
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        List<Path> files;
        try {
            files = Files.walk(pluginDir).filter(path -> !Files.isDirectory(path)).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GLAAssetWriter writer = new GLAAssetWriter();
        writer.write(files, pluginDir, output);
        try {
            reader = new GLAAssetReader(output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGLAFileExists() {
        assertTrue(Files.exists(output));
    }

    @Test
    void testGLAReaderContainsFile() {

        for (int i = 0; i < 5; i++) {
            for (String dir : subDirs) {
                assertTrue(reader.getIndex().containsKey(dir + "/doc_" + (i + 1) + ".txt"));
            }
        }

    }

    @Test
    void testGLAReaderFileContents() {
        // Making Sure the File exists within the archive
        Optional<InputStream> file = reader.getFile("textures/doc_1.txt");
        assertTrue(file.isPresent());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.get()))) {

            for (String line : br.lines().toList())
                Logger.debug(line);

        } catch (Exception e) {
            Assertions.fail("Couldn't read file properly");
        }
    }

}
